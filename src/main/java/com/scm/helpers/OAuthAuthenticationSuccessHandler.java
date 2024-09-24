package com.scm.helpers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.User;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        logger.info("OAuthAuthenticationSuccessHandler called...");

        DefaultOAuth2User oauth2User = (DefaultOAuth2User)authentication.getPrincipal();

        // oauth2User.getAttributes().forEach((key, value) -> {
        //     logger.info("{} {}", key, value);
        // });

        // Creating user and Storing user data in database...
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setPassword("password");
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Identify the Provider...
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            // get google attributes..
            user.setEmail(oauth2User.getAttribute("email").toString());
            user.setEmailVerified(true);
            user.setProfilePic(oauth2User.getAttribute("picture").toString());
            user.setName(oauth2User.getAttribute("name").toString());
            user.setProvider(ProviderType.GOOGLE);
            user.setProviderUserId(oauth2User.getName().toString());
            
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            // get github attributes..
            String email = oauth2User.getAttribute("email");
            if (email == null) {
                user.setEmail(oauth2User.getAttribute("login").toString()+"@github.com");
                user.setEmailVerified(false);
            }
            else {
                user.setEmail(oauth2User.getAttribute("email").toString());
                user.setEmailVerified(true);
            }

            user.setProfilePic(oauth2User.getAttribute("avatar_url").toString());
            user.setName(oauth2User.getAttribute("name").toString());
            user.setProvider(ProviderType.GITHUB);
            user.setProviderUserId(oauth2User.getAttribute("id").toString());
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {
            // get linked attributes..
        }
        else {
            logger.info("OAuthAuthenticationSuccessHandler : Unknown Provider");
        }



        User getUserFromDB = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (getUserFromDB == null) {
            userRepo.save(user);
            logger.info("User saved : " + user.getEmail());
        }


        // response.sendRedirect("/user/profile");
        // OR
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
