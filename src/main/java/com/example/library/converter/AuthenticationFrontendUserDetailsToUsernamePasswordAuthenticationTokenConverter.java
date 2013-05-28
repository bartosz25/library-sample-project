package com.example.library.converter;

import com.example.library.security.AuthenticationFrontendUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class AuthenticationFrontendUserDetailsToUsernamePasswordAuthenticationTokenConverter implements Converter<AuthenticationFrontendUserDetails, UsernamePasswordAuthenticationToken> {
    final Logger logger = LoggerFactory.getLogger(AuthenticationFrontendUserDetailsToUsernamePasswordAuthenticationTokenConverter.class);
    
    public UsernamePasswordAuthenticationToken convert(AuthenticationFrontendUserDetails user) {
        logger.info("User to convert :"+user);
        if (user == null) return null;
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );
        return userToken;
    }
}