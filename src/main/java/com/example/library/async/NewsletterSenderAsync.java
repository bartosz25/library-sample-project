package com.example.library.async;

import java.util.Date;
import java.util.List;

import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Newsletter;
import com.example.library.model.entity.NewsletterSend;
import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.repository.NewsletterRepository;
import com.example.library.service.NewsletterSendService;
import com.example.library.service.NewsletterService;
import com.example.library.service.NewsletterSubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Newsletter peut avoir un des 3 états : 0 - ne pas envoyé, 1 - en cours d'envoi, 2 -  
 * insértion des e-mails dans newsletter_send_tmp terminé, 3 - envoi terminé.
 * 
 * Les tâches à faire sont : 
 * - récupérer les 5 newsletters
 * - récupérer les 50 premiers destinataires
 * 
 * Sender est une classe qui envoie les newsletters. Elle est appelée dans chaque nouveau 
 * Thread. Une fois appelée, elle va chercher les destinataires dans une méthode synchronisée.
 * Ensuite, ces destinataires (newsletter subscriber + id du newsletter + date d'ajout) sont  
 * insérés dans une tableau temporaire (newsletter_send_tmp). Une fois le chargement de tous les
 * destinataires terminés, la table est vidée une ligne par une 
 * 
 * Une autre méthode annotée avec @Scheduled va récupérer les données renseignées dans 
 * newsletter_send_tmp et déclencher l'envoi du mail. Les mails sont alors envoyés dans les
 * packages par 50.  
 * 
 */

// TODO 
/*
mysql> show processlist;
+-----+------+-----------------+---------+---------+------+-------+------------------+
| Id  | User | Host            | db      | Command | Time | State | Info             |
+-----+------+-----------------+---------+---------+------+-------+------------------+
|  72 | root | localhost:51570 | NULL    | Query   |    0 | NULL  | show processlist |
| 273 | root | localhost:53294 | com.example.library | Sleep   |  365 |       | NULL             |
| 274 | root | localhost:53295 | com.example.library | Sleep   |  468 |       | NULL             |
| 275 | root | localhost:53296 | com.example.library | Sleep   |    1 |       | NULL             |
| 278 | root | localhost:53306 | com.example.library | Sleep   |  467 |       | NULL             |
| 279 | root | localhost:53305 | com.example.library | Sleep   |    7 |       | NULL             |
| 280 | root | localhost:53307 | com.example.library | Sleep   |  467 |       | NULL             |
+-----+------+-----------------+---------+---------+------+-------+------------------+
7 rows in set (0.00 sec)
*/
// Try to remove the oldiest sleeping connections (for exemple where Time > 400)
 
@Service
public class NewsletterSenderAsync {
    final Logger logger = LoggerFactory.getLogger(NewsletterSenderAsync.class);
    private static final String LOG_PREFIX = "[AsyncTask]";
    private boolean newslettersGot = false;
    private boolean isTest = false;
    private int maxNewsletters = 5;
    private int maxReceivers = 50;
    @Autowired
    private NewsletterService newsletterService;
    @Autowired
    private NewsletterSubscriberService newsletterSubscriberService;
    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private NewsletterSendService newsletterSendService;
    
    /**
     * Parts which gets newsletters to send and creates the receivers lists.
     */
    @Scheduled(fixedRate = 1800000)
    // @Scheduled(fixedRate = 50000) // 50 seconds for tests
    public void getNewsletters() {
        NewsletterInitializer newsletterInitializer = new NewsletterInitializer(new Date().getTime());
        if (isTest) {
            newsletterInitializer.run();
        } else { 
            Thread initializer = new Thread(newsletterInitializer);
            initializer.start();
        }
    }

    private  void getReceivers(Newsletter newsletter) {
	try {
        newsletter.setState(Newsletter.STATE_SENDING);
        newsletterRepository.save(newsletter);
        // logger.info("============> " + newsletter.getPreferenciesList());
        List<Object[]> newsletterSubscribers = newsletterSubscriberService.getByCriteria(newsletter.getPreferenciesList(), newsletter, maxReceivers);
        if (newsletterSubscribers.size() == 0) {
            newsletter.setState(Newsletter.STATE_ALMOST_ENDED);
            newsletterRepository.save(newsletter);
            logger.info("RESULT : No more receivers found. Newsletter ("+newsletter+") was marked as almost ended");
        } else {
            logger.info("RESULT : ==========> found subscribers " + newsletterSubscribers);
            for (Object[] newsletterSubscriberObj : newsletterSubscribers) {
                if (newsletterSendService.addToSendList(((NewsletterSubscriber) newsletterSubscriberObj[0]), newsletter) == null) {
                    // logger.error("An exception occured on inserting newsletter to send : " + newsletterSubscriber);
                }
            }
        }
	} catch(Exception ex) {
	logger.info("RESULT EXCEPTION getReceivers : " + ex.getMessage());
	}
    }
    
    private class NewsletterInitializer implements Runnable {
        private int time = 0;
        private long id;
        
        private NewsletterInitializer(long id) {
            this.id = id;
        }
        
        public void run() {
            logger.info(LOG_PREFIX+" NewsletterInitializer ["+id+"] launched at " + new Date());
            try {
                Page<Object[]> newsletters = newsletterService.getNewslettersToSend(new Date(), Newsletter.STATE_ALMOST_ENDED, maxNewsletters);
                    logger.info(LOG_PREFIX+" Found "+newsletters.getSize()+ " newsletters to send");
                    for (Object[] newsletterObj : newsletters.getContent()) {
                        Newsletter newsletter = (Newsletter) newsletterObj[0];
                        logger.info(LOG_PREFIX+" Found Newsletter to send " + newsletter);
                        getReceivers(newsletter);
                    }
            } catch (Exception e) {
                logger.error(LOG_PREFIX+" An exception occured on initializing newsletter", e);
            }
        }
    }
    
    /** 
     * This scheduled task gets the list of receivers from newsletter_send table whom  
     * state_nse field is 2 by incremented offset for maxReceivers value.
     * It shouldn't be launched at the same fixedRate as getNewsletters() method.
     * It's launched every 120 seconds.
     */
    // @Scheduled(fixedRate = 1800000)
    @Scheduled(fixedRate = 120000)
    public void sendNewsletters() {
        logger.info(LOG_PREFIX + " Launching sendNewsletters() at " + new Date());
        // newsletter getting was launched, we can get receivers list and send newsletters to 
        // them
        Page<Object[]> receivers = newsletterSendService.getToSend(0, maxReceivers);
        logger.info(LOG_PREFIX+" Found total receivers to send " + receivers.getNumberOfElements());
        for (Object[] receiver : receivers.getContent()) {
            NewsletterSend newsletterSend = (NewsletterSend) receiver[0];
            newsletterSend.setNewsletter((Newsletter) receiver[1]); // avoid null on test
            newsletterSend.setNewsletterSubscriber((NewsletterSubscriber) receiver[2]); // avoid null on test 
            try {
                Thread.sleep(3000); // 3 seconds before a new sending
                logger.info(LOG_PREFIX+" Sending newsletter : " + newsletterSend);
                newsletterSubscriberService.sendToSubcriber(newsletterSend);
            } catch (Exception e) {
                logger.error(LOG_PREFIX+" An exception occured on sending newsletter ["+newsletterSend.getNewsletter().getId()+"]"+
                " to " + newsletterSend.getNewsletterSubscriber().getEmail(), e);
            }
        }
    }
    
    /**
     * Setters are mainly used for testing purposes.
     */
    public void setMaxReceivers(int maxReceivers) {
        this.maxReceivers = maxReceivers;
    }
    
    public void setMaxNewsletters(int maxNewsletters) {
        this.maxNewsletters = maxNewsletters;
    }
    
    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }
}




// TOUT CELA EST @deprecated
/**
 * Voici un autre algorithme qui peut résoudre ce problème d'envoi de newsletters.
 * 1. On démarre la classe comme avant, au démarrage de Spring.
 * 2. On détermine le nombre de newsletters qui peuvent être envoyés d'un seul lancement.
 * 3. On récupère les newsletters qui doivent être envoyés (statut différent de Newsletter.STATE_SEND) => placement dans une liste
 * 4. On récupère les destinataires potentiels. Deux critères rentrent en compte :
 * -  critères du newsletter
 * -  destinataire ne peut pas avoir déjà reçu le newsletter en question
 * 5. 
 * 
 * 
 * 
 * 
 */

// // The '?' character is allowed for the day-of-month and day-of-week fields. It is used to specify "no specific value". This is useful when you need to specify something in one of the two fields, but not the other. See the examples below (and CronTrigger JavaDoc) for clarification. (http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06)
    // // @Scheduled(cron = "0 0 * * * ?") // calledy every day at midnight
    // // @Scheduled(fixedRate = 1800000) // every 30 minutes
    // public void send()
    // {
        // logger.info("=========> Newsletter sender started " + new Date());
        // Thread checker = new Thread(new CheckForNewsletters());
        // checker.start();
    // }
    
    // private class CheckForNewsletters implements Runnable
    // {
        // public void run()
        // {
            // logger.info("==============> Starting thread for newsletter checker at " + new Date());
            // logger.info("==============>  newsletterService is " + newsletterService);
            // while(isRunning)
            // {
                // try
                // {
                    // logger.info("============> looking for new newsletters at " + new Date());
                    // // Thread.sleep(600000); // every 10 minutes
                    // incrementCounter();
                    // Thread.sleep(5000); // every 5 seconds
                    // List<Object[]> newsletters = newsletterService.getNewslettersToSend(new Date(), Newsletter.STATE_SEND);
                    // logger.info("=> Found "+newsletters.size()+ " newsletters to send");
                    // for(Object[] resultObj : newsletters)
                    // {
                        // Newsletter newsletter = (Newsletter)resultObj[0];
                        // logger.info("=> Found Newsletter to send " + newsletter);
                        // Thread newsletterThread = new Thread(new NewsletterSender(newsletter));
                        // newsletterThread.start();
                    // }
                // }
                // catch(InterruptedException e)
                // {
                    // logger.error("An InterruptedException occured on looking for new newsletters to send", e);
                // }
                // catch(Exception ex)
                // {
                    // logger.error("An exception occured on looking for new newsletters to send", ex);
                // }
            // }
        // }
    // }
    
    // private class NewsletterSender implements Runnable
    // {
        // private Newsletter newsletter;
        
        // public NewsletterSender(Newsletter newsletter)
        // {
            // this.newsletter = newsletter;
        // }
        
        // public void run()
        // {
            // logger.info("========> running NewsletterSender for newsletter " + newsletter);
            // newsletter.setState(Newsletter.STATE_SENDING);
            // newsletterRepository.save(newsletter);
            
            // // TODO : récupérer avec LIMIT OFFSET
            // // TODO : faire tout (charger toutes les fetchs) pour éviter LazyInitializationException de Hibernate
            // logger.info("============> " + newsletter.getPreferenciesList());
            // List<NewsletterSubscriber> newsletterSubscribers = newsletterSubscriberService.getByCriteria(newsletter.getPreferenciesList(), newsletter, maxReceivers);
            // // List<NewsletterSubscriber> newsletterSubscribers = newsletterSubscriberRepository.getAll();
            // if(newsletterSubscribers.size() == 0)
            // {
                // newsletter.setState(Newsletter.STATE_SEND);
                // newsletterRepository.save(newsletter);
                // logger.info("No more receivers found. Newsletter ("+newsletter+") was marked as send");
            // }
            // else
            // {
                // logger.info("==========> found subscribers " + newsletterSubscribers);
                // for(NewsletterSubscriber newsletterSubscriber : newsletterSubscribers)
                // {
                    // try
                    // {
                        // if(!newsletterSubscriberService.sendToSubcriber(newsletterSubscriber, newsletter))
                        // {
                            // logger.error("An error occured on sending newsletter ("+newsletter+") to subscriber ("+newsletterSubscriber+")");
                        // }
                    // }
                    // catch(Exception ex)
                    // {
                        // logger.error("An exception occured on looking for new newsletters to send", ex);
                    // }
                // }
            // }
        // }
    // }

    // private synchronized void incrementCounter()
    // {
        // logger.info("========> Incrementing counter");
        // runs++;
        // if(runs == maxRuns)
        // {
            // isRunning = false;
            // logger.info("Newsletter sending ends at " + new Date());
        // }
    // }
// }

// // import java.util.Date;
// // import java.util.List;
// // import java.util.ArrayList;

// // public class Newsletter
// // {
    // // boolean isRunning = true;
    // // private int runs = 0;
    // // private final int maxRuns = 5;
    
    // // public static void main(String[] args)
    // // {
    // // Newsletter n = new Newsletter(); 
    // // n.send();
    // // }
    
	// // private void send()
	// // {
	    // // System.out.println("=========> Newsletter sender started " + new Date());
        // // Thread checker = new Thread(new CheckForNewsletters());
        // // checker.start();
	// // }
	
	// // private synchronized void incrementCounter()
	// // {
	    // // runs++;
	    // // if(runs == maxRuns)
        // // {
            // // isRunning = false;
            // // System.out.println("Newsletter sending ends at " + new Date());
        // // }
	// // }
	
    // // private class CheckForNewsletters implements Runnable
    // // {
        // // public void run()
        // // {
            // // System.out.println("==============> Starting thread for newsletter checker at " + new Date());
            // // while(isRunning)
            // // {
                // // try
                // // {
                    // // System.out.println("============> looking for new newsletters at " + new Date());
                    // // // Thread.sleep(600000); // every 10 minutes
                    // // Thread.sleep(5000); // every 5 seconds
                    // // List<String> newsletters = new ArrayList<String>();
                    // // newsletters.add("newsletter 1");
                    // // newsletters.add("newsletter 2");
                    // // // newsletters.add("newsletter 3");
                    // // incrementCounter();
                    // // // System.out.println("=> Found Newsletters to send " + newsletters);
                    // // for(String newsletter : newsletters)
                    // // {
                        // // Thread newsletterThread = new Thread(new NewsletterSender(newsletter));
                        // // newsletterThread.start();
                    // // }
                // // }
                // // catch(InterruptedException e)
                // // {
                    // // System.out.println("An exception occured on looking for new newsletters to send" + e.getMessage());
                // // }
                // // }
           // // }
    // // }
    
    // // private class NewsletterSender implements Runnable
    // // {
        // // private String newsletter;
        
        // // public NewsletterSender(String newsletter)
        // // {
            // // this.newsletter = newsletter;
        // // }
        
        // // public void run()
        // // {
            // // double identifiant = Math.random();
            // // System.out.println("[newsletter_"+identifiant+"] running NewsletterSender for newsletter " + newsletter); 
            // // // TODO : récupérer avec LIMIT OFFSET
            // // // List<NewsletterSubscriber> newsletterSubscribers = newsletterSubscriberService.getByCriteria(newsletter.getPreferenciesList(), newsletter);
            // // List<String> senders = new ArrayList<String>();
            // // senders.add("bartkonieczny@gmail.com");
            // // senders.add("bartkonieczny@yahoo.fr");
            // // // senders.add("koniczynka57@wp.pl");
            // // // senders.add("b_konieczny@op.pl");
            // // // senders.add("other");
            // // // senders.add("other-email");
            // // for(String sender : senders)
            // // {
                // // try
                // // {
                    // // System.out.println("[newsletter_"+identifiant+"] Sending Newsletter to " + sender + "at " + new Date());
                    // // // Thread.sleep(15000); // wait 15 seconds before sending the next newsletter
                    // // Thread.sleep(5000); // wait 5 seconds before sending the next newsletter
                // // }
                // // catch(InterruptedException e)
                // // {
                    // // System.out.println("[newsletter_"+newsletter+"] An exception occured on sending a new newsletters" + e.getMessage());
                // // }
            // // }
        // // }
    // // }
    
    
    
// // }