package com.therame.service;

import com.therame.model.User;

public interface UserService {

    User findUserByEmail(String email);

    /**
     * Creates a new user
     *
     * @param user the user definition
     * @return the created user
     */
    User createUser(User user);
}
