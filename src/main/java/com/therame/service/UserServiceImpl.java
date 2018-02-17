package com.therame.service;

import java.util.*;

import javax.transaction.Transactional;

import com.google.common.collect.ImmutableList;
import com.therame.model.DetailedUserDetails;
import com.therame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.therame.model.User;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findUserById(UUID uuid) {
        return Optional.of(userRepo.findOne(uuid));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        userRepo.save(user);
        return user;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser(user);
        return user;
    }

    @Override
    public User createRootUser(User user) {
        user.setPassword(null);
        saveUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        saveUser(user);
        return user;
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
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    @Override
    public boolean doesUserExist(User user) {
        return findUserByEmail(user.getEmail()).isPresent();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> optionalUser = findUserByEmail(userName);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new DetailedUserDetails(user, ImmutableList.of(new SimpleGrantedAuthority(user.getType().name())));
        } else {
            throw new UsernameNotFoundException(userName);
        }
    }
}
