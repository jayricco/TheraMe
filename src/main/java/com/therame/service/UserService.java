package com.therame.service;

import com.therame.model.User;
import com.therame.view.UserView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    Optional<User> findUserById(UUID uuid);
    Optional<UserView> findById(UUID id);
    /**
     * Finds a user with the specified email
     *
     * @param email the email
     * @return the user, absent if none exists
     */
    Optional<User> findUserByEmail(String email);
    Optional<User> findByConfirmationToken(String confirmationToken);
    User createUser(User user);
    User createRootUser(User user);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUserById(UUID uuid);
    void deleteAllUsers();
    List<User> getAllUsers();
    boolean doesUserExist(User user);


    /**
     * Finds all users that match the specified type filters
     *
     * @param typeFilters the whitelist of type filters
     * @return the matching user views
     */
    List<UserView> findAllUsersByNameAndType(String name, List<User.Type> typeFilters);
}
