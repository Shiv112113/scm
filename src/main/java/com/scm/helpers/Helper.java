package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {
            
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                System.out.println("Getting Email from Google..");

                username = oauth2User.getAttribute("email".toString());
            }
            else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting Email from GitHub..");

                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                                                : oauth2User.getAttribute("login").toString()+"@github.com";
            }

            return username;
        }
        else {
            System.out.println("Getting Email from Local DataBase..");
            return authentication.getName();   
        }   
    }

}
