package com.therame.service;

import com.therame.model.User;
import com.therame.view.UserView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<UserView> findById(UUID id);

    /**
     * Finds a user with the specified email
     *
     * @param email the email
     * @return the user, absent if none exists
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Creates a new user
     *
     * @param user the user definition
     * @return the created user
     */
    User createUser(User user);

    /**
     * Finds all users that match the specified type filters
     *
     * @param typeFilters the whitelist of type filters
     * @return the matching user views
     */
    List<UserView> findAllUsersByNameAndType(String name, List<User.Type> typeFilters);

    Optional<User> findUserByInitCode(String initCode);

    Optional<User> updatePasswordForInitCode(String initCode, String password);

    void sendPasswordResetEmail(String email);

    User updateUser(User user);

    User deactivateUser(UUID userID);
}
