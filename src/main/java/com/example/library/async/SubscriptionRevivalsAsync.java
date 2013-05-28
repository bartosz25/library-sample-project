package com.example.library.async;

import com.example.library.service.SubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionRevivalsAsync {
    final Logger logger = LoggerFactory.getLogger(SubscriptionRevivalsAsync.class);
    @Autowired
    private SubscriberService subscriberService;

    @Scheduled(fixedRate = 7200000) // called every 2 hours
    public void reviveSubscribers() {
        logger.info("=========> Revive non actived subscribers");
        subscriberService.revive(5);
        logger.info("========> Revive send end");
        
    }
}