package com.example.library.converter;


import com.example.library.model.entity.Subscriber;
import com.example.library.service.SubscriberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.User;

public class UserToSubscriberConverter implements Converter<User, Subscriber> {
    @Autowired
    private SubscriberService subscriberService;

    public Subscriber convert(User user) {
        return subscriberService.loadByUsername(user.getUsername());
    }
}