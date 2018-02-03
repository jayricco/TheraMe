package com.therame.service;

import com.therame.model.User;

import java.util.Optional;

public interface UserService {

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
}
