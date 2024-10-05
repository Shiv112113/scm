package com.scm.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import com.scm.helpers.ProviderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "user")
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements UserDetails {
    // we need to implement UserDetails in User class to make it UserDetails, 
    // because Spring Security internally binded with UserDetails interface,
    // and can fetch data easily with pre-defined methods in UserDetails interface.
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Getter(AccessLevel.NONE)
    private String password;

    @Column(length = 500)
    private String profilePic;

    private String phoneNumber;

    // Status...
    @Getter(AccessLevel.NONE)
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneNumberVerified = false;

    // login Provider...  (SELF, GOOGLE, GITHUB, etc...)
    @Enumerated(value = EnumType.STRING)
    private ProviderType provider = ProviderType.SELF;
    private String providerUserId;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        // Converting something like ( List of roles[USERS, ADMIN] )
        // to ( Collection of SimpleGrantedAuthority[roles{ADMIN, USERS}] )
        Collection<SimpleGrantedAuthority> roles = roleList.stream()
                                .map(role -> new SimpleGrantedAuthority(role))
                                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
     }

    @Override
    public String getPassword() {
        return this.password;
    }


}
