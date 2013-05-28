package com.example.library.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.example.library.model.entity.Subscriber;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.service.SubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {
    final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
    @Autowired
    private SubscriberService subscriberService;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Looking for user by username " + username);
        Subscriber subscriber = subscriberService.loadByUsername(username);
        if (subscriber != null) {
            logger.info("Subscriber(s) was found : " + subscriber);
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new GrantedAuthorityImpl(subscriber.getRole()));
            /*UserDetails userDetails = new User(subscriber.getLogin(), subscriber.getPassword(),
                                               subscriber.ifConfirmed(),
                                                true, true, !subscriber.ifBlacklisted(), 
                                                authorities
                                               );*/
            AuthenticationFrontendUserDetails userDetails = new AuthenticationFrontendUserDetails(subscriber.getLogin(), subscriber.getPassword(),
                                               subscriber.ifConfirmed(),
                                                true, true, !subscriber.ifBlacklisted(), 
                                                authorities
                                               );
            userDetails.setId(subscriber.getId());
            userDetails.setBookingNb(subscriber.getBookingNb());
            logger.info("Created new UserDetails instance : " + userDetails);
            return userDetails;
        }
        logger.info("Subscriber wasn't found : null returned" + subscriber);
        return null;
    }
}