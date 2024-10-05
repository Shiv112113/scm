package com.scm.services;

import com.scm.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    User getUserByEmail(String email);
    User getUserByEmailToken(String token);

    List<User> getAllUsers();


    // Add here all the methods related to User Service/Business Logics...
}
