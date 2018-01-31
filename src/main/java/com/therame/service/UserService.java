package com.therame.service;

import com.therame.model.User;

public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user);
}
