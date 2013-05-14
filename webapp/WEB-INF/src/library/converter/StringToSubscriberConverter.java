package library.converter;


import library.model.entity.Subscriber;
import library.model.repository.SubscriberRepository;
import library.service.SubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class StringToSubscriberConverter implements Converter<String, Subscriber> {
    final Logger logger = LoggerFactory.getLogger(StringToSubscriberConverter.class);
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private SubscriberRepository subscriberRepository;

    public Subscriber convert(String login) {
        Subscriber subscriber = subscriberService.loadByUsername(login);
        if (subscriber == null) {
            try {
                long id = Long.parseLong(login.trim());
                subscriber = subscriberRepository.findOne(id);
            } catch (Exception e) {
                logger.error("An error occured on transformi String ("+login+") to long", e);
            }
        }
        return subscriber;
    }
}