package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Message;
import com.scm.entities.User;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    // Verify Email... 

    @Autowired
    private UserService userService;

    @RequestMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        System.out.println("Verifying Email...");

        User user = userService.getUserByEmailToken(token);
        if(user != null && user.getEmailToken().equals(token)) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            userService.updateUser(user);

            session.setAttribute("message", Message.builder()
                                                .content("Account is Verified!")
                                                .type(MessageType.green)
                                                .build());
        }
        else {
            session.setAttribute("message", Message.builder()
                                                .content("Something went Wrong: Account Verification Failed!")
                                                .type(MessageType.red)
                                                .build());
        }

        return "login";
    }
}
