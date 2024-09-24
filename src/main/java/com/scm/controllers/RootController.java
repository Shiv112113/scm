package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {
    
/* Key Points :-
    1. @ControllerAdvice is ideal for global exception handling, data binding configurations, and model enhancements.
    2. It enhances all controllers or a specific subset of them.
    3. Often used with @ExceptionHandler, @ModelAttribute, and @InitBinder annotations within the class it annotates.
*/

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication) {
        if(authentication == null) return;

        System.out.println("Adding logged in user info to the model...");
        String username = Helper.getEmailOfLoggedUser(authentication);
        logger.info("User logged in {}",  username);

        User user = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", user);
        System.out.println(user);

        System.out.println(user.getName());
        System.out.println(user.getEmail());
    }

}
