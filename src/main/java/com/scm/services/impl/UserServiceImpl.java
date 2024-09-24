package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // Set Auto-Generated userId..
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        // Set Password Encoder..
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set User Role..
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());

        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        // Getting reference of same user from Database to update...
        User user2 = userRepo.findById(user.getId())
                        .orElseThrow( () -> new ResourceNotFoundException("User not found !!!"));

        // Update user2 with user...
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user.setPhoneNumberVerified(user.isPhoneNumberVerified());
        user.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        // Save the User with updated data in user2 in Database...
        User saveUser = userRepo.save(user2);
        return Optional.ofNullable(saveUser);
    }

    @Override
    public void deleteUser(String id) {
        // Getting reference of same user from Database to delete...
        User user2 = userRepo.findById(id)
                        .orElseThrow( () -> new ResourceNotFoundException("User not found !!!"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        // Getting reference of same user from Database by userId to check its existance...
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        // Getting reference of same user from Database by email to check its existance...
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

}