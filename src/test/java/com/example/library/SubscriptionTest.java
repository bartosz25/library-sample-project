package com.example.library.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.SubscriberRepository;
import com.example.library.service.SubscriberService;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

// http://stackoverflow.com/questions/6577015/why-is-junit-giving-me-a-failed-to-load-applicationcontext-error

// pour lancer des tests : java -cp junit-4.7.jar org.junit.runner.JUnitCore

// Si une erreur se produit du type "Junit + Error creating bean" il faire import des beans une par une, en injectant à chaque fois le bean manquant, générant l'erreur en question
// Pour ant test, c'est System.out.println() qui sert à imprimer une chaîne de caractères dans le fichier final

// TODO : implémenter @Security, @PreAuthorize etc.
// TODO : implémenter tests pour Spring Web Flow
/**
 * TODOS : 
 * - tests pour SubscriptionTransferService.transferToUser
 * - tests pour SubscriptionService.saveSubscribeNotValid, SubscriptionService.addPayment, SubscriptionService.transferSubscription, SubscriptionService.getOverlapSubscriptionBySubscriber
 * - tests pour PenaltyService.saveNotValid, PenaltyService.addPayment
 * - tests pour NewsletterSubscriberService.getByCriteria
 * - tests pour ChatService.addNew
 * - tests pour BorrowingService.borrowBookCopy, BorrowingService.getBorrowingByIdAndUser, BorrowingService.returnBookCopy, BorrowingService.getDelayedBooks, BorrowingService.checkDelayed
 * - tests pour BookingService.hasBooking, BookingService.bookBookCopy, BookingService.alreadyBooked
 * - tests pour BookCopyService.getOneNotBorrowed
 * - tests pour les contrôleurs qui dépendent d'une servlet
 */
 // Utilise @Before public class SimpleInterestCalculatorJUnit4Tests { 
    // private InterestCalculator interestCalculator; 
    // @Before 
    // public void init() { 
        // interestCalculator = new SimpleInterestCalculator(); 
        // interestCalculator.setRate(0.05); 
    // } 
    // @Test 
    // public void calculate() { 
        // double interest = interestCalculator.calculate(10000, 2); 
        // assertEquals(interest, 1000.0, 0); 
    // }
 // Voir ça aussi 
   // @Test(expected = DuplicateAccountException.class) 
    // public void createDuplicateAccount() { 
        // accountDao.createAccount(existingAccount); 
    // } 
// Des choses sympa comme executeSqlScript()
// t38LegacyTests extends 
        // AbstractTransactionalSpringContextTests { 
    // private AccountService accountService; 
    // private static final String TEST_ACCOUNT_NO = "1234"; 
    // public void setAccountService(AccountService accountService) { 
        // this.accountService = accountService; 
    // } 
    // protected void onSetUpInTransaction() throws Exception { 
        // executeSqlScript("classpath:/bank.sql",true); 
        // accountService.createAccount(TEST_ACCOUNT_NO); 
        // accountService.deposit(TEST_ACCOUNT_NO, 100); 
// @ContextConfiguration(locations={"file:///D:/resin-4.0.32/webapps/ROOT/META-INF/spring/test-config.xml"})
// @RunWith(SpringJUnit4ClassRunner.class)
public class SubscriptionTest extends AbstractControllerTest
{
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private SubscriberService subscriberService;
    
    /**
     * Test case for stanard subscription flow : 
     * - submitting data
     * - looking for user by username
     * - looking for non confirmed user by his id
     * - confirming subscriber
     */
    @Test
    public void subscribe() {
        System.out.println("Testing subscribe() : subscription");
        
        AnonymousAuthenticationToken anonymousUser = new AnonymousAuthenticationToken(
                        "anonymous", "anonymous", new 
                        ArrayList(Arrays.asList(new GrantedAuthorityImpl("ROLE_ANONYMOUS"))));
        SecurityContextHolder.getContext().setAuthentication(anonymousUser);
        
        Subscriber subscriber = new Subscriber();
        subscriber.setLogin("testNonConfirmed");
        subscriber.setEmail("bartkonieczny+33@gmail.com");
        subscriber.setConfirmed(Subscriber.IS_NOT_CONFIRMED);
        subscriber.setBlacklisted(Subscriber.IS_NOT_BLACKLISTED);
        
        try {
            subscriber = subscriberService.save(subscriber);
        } catch(Exception e) {
            System.out.println("An error occured on saving subscriber : " + e.getMessage());
        }
        System.out.println("After saving " + subscriber);
        Assert.assertNotNull("Subscriber is null", subscriber);
        
        Subscriber loadedSubscriber = subscriberService.loadByUsername(subscriber.getLogin());
        Assert.assertNotNull("Subscriber was not loaded correctly", loadedSubscriber);
        
        Subscriber notConfirmedSubscriber = subscriberService.findNonConfirmedById(subscriber.getId());
        Assert.assertNotNull("Not confirmed subscriber was not loaded correctly", notConfirmedSubscriber);
        
        Subscriber confirmedSubscriber = subscriberService.confirm(subscriber);
        Assert.assertNotNull("Confirmed subscriber is null", confirmedSubscriber);

        System.out.println("Testing subscribe() : subscription");
    }

}