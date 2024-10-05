package com.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Message;
import com.scm.helpers.MessageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
                
                HttpSession session = request.getSession();

                if(exception instanceof DisabledException) {
                    // i.e User is Disabled...

                    session.setAttribute("message", Message.builder()
                                                        .content("Account is Diasbled! Check Email for Verification Link....")
                                                        .type(MessageType.red)
                                                        .build());
                    response.sendRedirect("/login");
                }
                else {
                    session.setAttribute("message", Message.builder()
                                                        .content("Invalid username and password")
                                                        .type(MessageType.red)
                                                        .build());
                    response.sendRedirect("/login?error");
                }
    }

}
