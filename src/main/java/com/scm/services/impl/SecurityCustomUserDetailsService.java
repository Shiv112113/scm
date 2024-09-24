package com.scm.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService{
    // UserDetailsService interface carries a method used by Spring Security -
    // to load user by username, all we need to use userRepo to find username(i.e Email) -
    // from DataBase and pass it through default method loadUserByUsername 
    // used by Spring Security for further implimentations...

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with Email : " + username));
    }

}
