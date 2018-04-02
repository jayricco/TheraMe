package com.therame.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.google.common.collect.ImmutableList;
import com.therame.model.DetailedUserDetails;
import com.therame.model.Provider;
import com.therame.model.UserRepository;
import com.therame.view.UserView;
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

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
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
