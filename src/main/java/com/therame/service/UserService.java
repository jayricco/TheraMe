package com.therame.service;

import com.therame.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    Optional<User> findUserById(Long id);
    /**
     * Finds a user with the specified email
     *
     * @param email the email
     * @return the user, absent if none exists
     */
    Optional<User> findUserByEmail(String email);
    void createUser(User user);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUserById(Long id);
    void deleteAllUsers();
    List<User> getAllUsers();
    boolean doesUserExist(User user);
}
