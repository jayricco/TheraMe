package com.therame.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.google.common.collect.ImmutableList;
import com.therame.model.DetailedUserDetails;
import com.therame.model.Provider;
import com.therame.repository.jpa.UserRepository;
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
    public Optional<User> findUserById(UUID id) {
        User user = userRepo.findOne(id);
        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
    @Override
    public Optional<UserView> findUserAsView(UUID id) {
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
    public Optional<User> findByConfirmationToken(String confirmationToken) {
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public List<User> searchByName(String name) {
        return null;
    }

    @Override
    public User createRootUser(User user) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // fix this
        return userRepo.getOne(user.getId());
    }

    @Override
    public void deleteUserById(UUID uuid) {
        userRepo.delete(uuid);
    }

    @Override
    public void deleteAllUsers() {
        userRepo.deleteAll();
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

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public boolean doesUserExist(User user) {
        return false;
    }
}
