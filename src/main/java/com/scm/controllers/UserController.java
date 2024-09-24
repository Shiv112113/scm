package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/user")
public class UserController {


    Logger logger = LoggerFactory.getLogger(UserController.class);

    // User Dashboard Page... 
    @RequestMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        System.out.println("User Dashboard Loading...");

        return "user/dashboard";
    }

    // User Profile Page... 
    @RequestMapping("/profile")
    public String userProfile() {
        System.out.println("User Profile Loading...");

        return "user/profile";
    }
    
    // User Add Contact Page...

    // User View Contact Page... 

    // User Edit Contact...

    // User Delete Contact...
}
