package com.example.library.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticationUserDetails extends User {
    final Logger logger = LoggerFactory.getLogger(AuthenticationUserDetails.class);
    private long id = 0l;
    
    public AuthenticationUserDetails(String username, String password, boolean enabled, 
        boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, 
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}