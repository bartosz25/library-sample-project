package library.service.impl;
 
import java.util.ArrayList;
import java.util.List;

import library.model.entity.Lang;
import library.model.entity.NewsletterSubscriber;
import library.model.entity.NewsletterSubscriberPreferency;
import library.model.repository.NewsletterSubscriberPreferencyRepository;
import library.service.NewsletterSubscriberPreferencyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newsletterSubscriberPreferencyService")
public class NewsletterSubscriberPreferencyServiceImpl implements NewsletterSubscriberPreferencyService {
    final Logger logger = LoggerFactory.getLogger(NewsletterSubscriberPreferencyServiceImpl.class);
    @Autowired
    private NewsletterSubscriberPreferencyRepository newsletterSubscriberPreferencyRepository;
    
    public List<String> getSavedPreferencies(NewsletterSubscriber newsletterSubscriber) {
        List<String> preferencies = new ArrayList<String>();
        List<NewsletterSubscriberPreferency> preferenciesDb = newsletterSubscriberPreferencyRepository.getByNewsletterSubscriber(newsletterSubscriber.getId());
        logger.info("=========> found newsletter preferencies from the database " + preferenciesDb);
        // TODO : gérer Lang dynamiquement
        Lang lang = new Lang();
        lang.setId(2l);
        for (NewsletterSubscriberPreferency preferency : preferenciesDb) {
            String code = preferency.getNewsletterPreferencyCategory().getCode()+"-"+preferency.getPreferency();
            if (!preferency.getNewsletterPreferencyCategory().getCode().equals("BOOKCATE") && !preferency.getNewsletterPreferencyCategory().getCode().equals("WRITERFA")) {
                code += "-"+lang.getId();
            }
            logger.info("=====> adding to preferencies " + code);
            preferencies.add(code);
        }
        return preferencies;
    }
}