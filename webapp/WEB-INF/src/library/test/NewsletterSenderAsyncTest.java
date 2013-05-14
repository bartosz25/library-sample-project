package library.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import library.async.NewsletterSenderAsync;
import library.model.entity.Admin;
import library.model.entity.Newsletter;
import library.model.entity.NewsletterSubscriber;
import library.model.repository.AdminRepository;
import library.model.repository.NewsletterRepository;
import library.model.repository.NewsletterSubscriberRepository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Actually, this class sends only the first mails specified in setMaxReceivers() method. 
 */
// @TODO : handle multithreading 
public class NewsletterSenderAsyncTest extends AbstractControllerTest {
    @Autowired
    private NewsletterSenderAsync newsletterSenderAsync;
    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @Autowired
    private AdminRepository adminRepository;
    private Newsletter newsletter;
    
    @Before 
    public void initNewsletters() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE , -1);
        
        Admin admin = adminRepository.findOne(1l);
        
        newsletter = new Newsletter();
        newsletter.setAdmin(admin);
        newsletter.setCreated(new Date());
        newsletter.setSendTime(calendar.getTime());
        newsletter.setState(Newsletter.STATE_NOT_SEND);
        newsletter.setTitle("Test #1");
        newsletter.setText("Test #1 body");
        newsletter.setPreferencies("");
        newsletter = newsletterRepository.save(newsletter);
        System.out.println("Newsletter was added " + newsletter);
        System.out.println("All newsletters  " + newsletterRepository.findAll());

        List<String> emails = new ArrayList<String>();
        emails.add("bartkonieczny+33333@gmail.com");
        emails.add("bartkonieczny+33332@gmail.com");
        emails.add("bartkonieczny+33331@gmail.com");
        emails.add("bartkonieczny+33330@gmail.com");
        emails.add("bartkonieczny+33329@gmail.com");
        emails.add("bartkonieczny+33328@gmail.com");
        emails.add("bartkonieczny+33327@gmail.com");
        emails.add("bartkonieczny+33326@gmail.com");
        emails.add("bartkonieczny+33325@gmail.com");
        emails.add("bartkonieczny+33324@gmail.com");
        emails.add("bartkonieczny+33323@gmail.com");
        emails.add("bartkonieczny+33322@gmail.com");
        for (String email : emails) {
            NewsletterSubscriber newsletterSubscriber = new NewsletterSubscriber();
            newsletterSubscriber.setEmail(email);
            newsletterSubscriber.setPassword(email);
            newsletterSubscriber.setState(NewsletterSubscriber.STATE_CONFIRMED);
            newsletterSubscriber = newsletterSubscriberRepository.save(newsletterSubscriber);
            System.out.println("NewsletterSubscriber was added " + newsletterSubscriber);
        }
    }
     
    @Test
    public void testSending() {
        System.out.println("Launched tests for sending newsletter at " + new Date());
        newsletterSenderAsync.setIsTest(true);
        newsletterSenderAsync.setMaxReceivers(2);
        newsletterSenderAsync.getNewsletters();
        newsletterSenderAsync.sendNewsletters();
    }
}