package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.helpers.OAuthAuthenticationSuccessHandler;
import com.scm.services.impl.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler successHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
     /*
        DaoAuthenticationProvider actually responsible for taking userDetails-
        from userDetailsService and password Encoder to validate passowrd in userDetails.
        STEP 1 => Signin Page passes a UsernamePasswordAuthenticationToken to the AuthenticationManager, 
        which is implemented by ProviderManager. 
        STEP 2 => The ProviderManager is configured to use an AuthenticationProvider- 
        of type DaoAuthenticationProvider.
        STEP 3 => Now ProviderManager has both credentials from user input and from database-
        now it only needs to Authenticate the data if its valid or not.
     */

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Need a custom userDetailsService object containing User Details...
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // Need a custom PasswordEncoder object to set password encoder...
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // Return credentials to ProviderManager...
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Here we Authorize the links that which should be public or which should require login...
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/signup").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });
    
        // Here we set our custom login page or default one (provided by spring security)...
        
        /*
            Default login form...
            httpSecurity.formLogin(Customizer.withDefaults());
        */

        //Custom login form...
        httpSecurity.formLogin(loginForm -> {

            loginForm.loginPage("/login")
                     .loginProcessingUrl("/authenticate")
                     .successForwardUrl("/user/dashboard")
                     .usernameParameter("email")
                     .passwordParameter("password");

        });

        // Ideally should not be disabled but logout doesnot work if it's enabled...
        // To make it work while its enabled we need to create POST method for "/logout" in pageControllers...
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {

            logoutForm.logoutUrl("/logout")
                      .logoutSuccessUrl("/login?logout=true");

        });


        // OAuth2 Configuration...
        httpSecurity.oauth2Login(oauth -> {

            oauth.loginPage("/login");

            // Impliments Custom class OAuthAuthenticationSuccessHandler to extract relevent user data from
            // respecctive oauth2 provider(i.e google, github etc.) and store in database before redirect...
            oauth.successHandler(successHandler);

        });



        return httpSecurity.build();
    }



// User Creation and Login using java code with In-memory service...
/*
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("admin123")
                                .roles("ADMIN", "USER")
                                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
                                .username("user")
                                .password("user123")
                                .build();

        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1, user2);
        return inMemoryUserDetailsManager;

    }
*/


}
