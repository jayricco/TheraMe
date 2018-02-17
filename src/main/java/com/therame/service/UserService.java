package com.therame.service;

import com.therame.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    Optional<User> findUserById(UUID uuid);
    /**
     * Finds a user with the specified email
     *
     * @param email the email
     * @return the user, absent if none exists
     */
    Optional<User> findUserByEmail(String email);
    User createUser(User user);
    User createRootUser(User user);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUserById(UUID uuid);
    void deleteAllUsers();
    List<User> getAllUsers();
    boolean doesUserExist(User user);
}
