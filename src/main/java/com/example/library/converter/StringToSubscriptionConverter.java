package com.example.library.converter;


import com.example.library.model.entity.Subscription;
import com.example.library.model.repository.SubscriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class StringToSubscriptionConverter implements Converter<String, Subscription> {
    final Logger logger = LoggerFactory.getLogger(StringToSubscriptionConverter.class);
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription convert(String idString) {
        try {
            long id = Long.parseLong(idString.trim());
            return subscriptionRepository.findOne(id);
        } catch (Exception e) {
            logger.error("An error occured on transformi String ("+idString+") to long", e);
        }
        return null;
    }
}