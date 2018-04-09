package com.therame.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.google.common.collect.ImmutableList;
import com.therame.model.DetailedUserDetails;
import com.therame.model.Provider;
import com.therame.model.UserRepository;
import com.therame.util.Base64Converter;
import com.therame.util.InitializationMessageBuilder;
import com.therame.view.UserView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.therame.model.User;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${therame.media.host.url}")
    private String hostUrl;

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = javaMailSender;
    }

    @Override
    public Optional<UserView> findById(UUID id) {
        User user = userRepo.findOne(id);
        if (user != null) {
            return Optional.of(user.toView());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        String initCode = Base64Converter.toUrlSafeString(UUID.randomUUID());
        user.setInitCode(initCode);
        user = userRepo.save(user);

        mailSender.send(InitializationMessageBuilder.buildInitializationMessage(user, hostUrl));
        return user;
    }

    @Override
    public List<UserView> findAllUsersByNameAndType(String name, List<User.Type> typeFilters) {
        DetailedUserDetails currentUser = (DetailedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Provider currentUserProvider = currentUser.getUser().getProvider();

        return userRepo.findByNameAndType(name, typeFilters).stream()
                .filter(user -> currentUserProvider == null || currentUserProvider.equals(user.getProvider()))
                .map(User::toView)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserByInitCode(String initCode) {
        return userRepo.findByInitCode(initCode);
    }

    @Override
    @Transactional
    public Optional<User> updatePasswordForInitCode(String initCode, String password) {
        Optional<User> optionalUser = userRepo.findByInitCode(initCode);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setInitCode(null);
            user.setPassword(passwordEncoder.encode(password));
        }

        return optionalUser;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> optionalUser = findUserByEmail(userName);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Give sentinel a special role
            if (user.getType() == User.Type.ADMIN && user.getProvider() == null) {
                return new DetailedUserDetails(user, ImmutableList.of(new SimpleGrantedAuthority(user.getType().name()),
                        new SimpleGrantedAuthority("SENTINEL")));
            } else {
                return new DetailedUserDetails(user, ImmutableList.of(new SimpleGrantedAuthority(user.getType().name())));
            }
        } else {
            throw new UsernameNotFoundException(userName);
        }
    }
}
