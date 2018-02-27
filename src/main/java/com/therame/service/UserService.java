package com.therame.service;

import com.therame.model.User;
import com.therame.view.UserView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    Optional<User> findUserById(UUID uuid);
    Optional<UserView> findById(UUID id);
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

    List<UserView> findAllUsersByNameAndType(String name, List<User.Type> typeFilters);
}
