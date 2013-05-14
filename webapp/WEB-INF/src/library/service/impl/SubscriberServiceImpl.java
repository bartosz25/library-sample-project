package library.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.model.entity.Question;
import library.model.entity.Subscriber;
import library.model.entity.Suggestion;
import library.model.repository.NewsletterSubscriberRepository;
import library.model.repository.QuestionRepository;
import library.model.repository.SubscriberRepository;
import library.model.repository.SubscriptionRepository;
import library.model.repository.SuggestionRepository;
import library.security.Cryptograph;
import library.security.SaltCellar;
import library.service.BorrowingService;
import library.service.ChatService;
import library.service.PenaltyService;
import library.service.SubscriberService;
import library.service.SubscriptionService;
import library.tools.ImageTool;
import library.tools.MailerTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TODOS : 
 * - régler NullPointerException http://localhost:8080/subscription/subscribe pour kuba
 */
@Service("subscriberService")
// @Repository
// @Transactional
public class SubscriberServiceImpl implements SubscriberService {
    private static final String TPL_REGISTER = "register";
    private static final String TPL_CONFIRM = "confirm";
    private static final String TPL_REVIVAL = "revival";
    final Logger logger = LoggerFactory.getLogger(SubscriberServiceImpl.class);
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private BorrowingService borrowingService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MailerTool mailerTool;
    @Autowired
    private ImageTool imageTool;
    @Autowired 
    private Cryptograph cryptograph; 
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SaltCellar saltCellar;

    @Override
    @PreAuthorize("isAnonymous() or hasRole('ROLE_ADMIN')")
    public Subscriber findNonConfirmedById(long id) {
        return subscriberRepository.findNonConfirmedById(id);
    }
    
    @Override
    @PreAuthorize("isAnonymous()")
    public Subscriber confirm(Subscriber subscriber) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        Subscriber result = null;
        try {
            result = subscriberRepository.save(subscriber);
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_CONFIRM);
            mailData.put("to", result.getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("login", result.getLogin());
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            mailerTool.send();
            transactionManager.commit(status);
        } catch(Exception e) {
            result = null;
            transactionManager.rollback(status);
        }
        return result;
    }
    
    @Override
    @PreAuthorize("isAnonymous()")
    public Subscriber save(Subscriber subscriber) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        logger.info("============> Transaction Manager is " + transactionManager);
        Subscriber result = null;
        try {
            String password = passwordEncoder.encodePassword(subscriber.getPassword(), saltCellar.getSaltFromString(subscriber.getLogin()));
            subscriber.setPassword(password);
            subscriber.setAvatar("");
            logger.info("Set new encoded password " + password);
            subscriber.setBookingNb(Subscriber.BOOKINGS_NB);
            result = subscriberRepository.save(subscriber);
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_REGISTER);
            mailData.put("to", result.getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("login", result.getLogin());
            tplVars.put("idCrypted", cryptograph.encrypt(Long.toString(result.getId())));
            tplVars.put("idDecrypted", cryptograph.decrypt((String)tplVars.get("idCrypted")));
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            mailerTool.send();
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("=====================> Exception catched ", e);
            result = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return result;
    }
    
    @Override
    public Subscriber loadByUsername(String login) {
        Subscriber subscriber = subscriberRepository.loadByUsername(login);
        subscriber.setOldEmail(subscriber.getEmail());
        return subscriber;
    }
    
    @Override
    @PreAuthorize("(#subscriber.login == principal.username) or hasRole('ROLE_ADMIN')")
    public void updateEmail(Subscriber subscriber) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            subscriberRepository.updateEmail(subscriber.getEmail(), subscriber.getId());
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on updating e-mail", e);
            transactionManager.rollback(status);
            throw new Exception(e);
        }
    }
    
    @Override
    @PreAuthorize("(#subscriber.login == principal.username) or hasRole('ROLE_ADMIN')")
    public void updatePassword(Subscriber subscriber) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            String password = passwordEncoder.encodePassword(subscriber.getPassword(), saltCellar.getSaltFromString(subscriber.getLogin()));
            subscriber.setPassword(password);
            subscriberRepository.updatePassword(subscriber.getPassword(), subscriber.getId());
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on updating password", e);
            transactionManager.rollback(status);
            throw new Exception(e);
        }
    }
    
    @Override
    public Subscriber getById(long id) {
        return subscriberRepository.findOne(id);
    }
    
    @Override
    public void revive(int days) {
        List<Subscriber> subscribers = subscriberRepository.getNonConfirmedByDays(days, Subscriber.IS_NOT_CONFIRMED);
        logger.info("Found non confirmed subscribers " + subscribers);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            for (Subscriber subscriber : subscribers) {
                subscriber.setRevival(days);
                subscriberRepository.save(subscriber);
            }            
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("=====================> Exception catched ", e);
            transactionManager.rollback(status);
        }
        // send e-mails      
        for (Subscriber subscriber : subscribers) {
            try {
                HashMap<String, Object> mailData = new HashMap<String, Object>();
                mailData.put("config", TPL_REVIVAL);
                mailData.put("to", subscriber.getEmail());
                Map<String, Object> tplVars = new HashMap<String, Object>();
                tplVars.put("subscriber", subscriber);
                tplVars.put("idCrypted", cryptograph.encrypt(Long.toString(subscriber.getId())));
                mailerTool.setMailData(mailData);
                mailerTool.setVars(tplVars);
                mailerTool.send();
            } catch (Exception e) {
                logger.error("Unable to send subscriber revival e-mail", e);
            }
        }
    }
    
    @Override
    public Subscriber addAvatar(Subscriber subscriber) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Map<String, Object> uploadResult = imageTool.uploadFile("avatar", subscriber.getAvatarFile(), subscriber.getLogin());
            if (((Boolean)uploadResult.get("uploadResult")) == true) {
                subscriber.setAvatar((String)uploadResult.get("fileBasename"));
                subscriber = subscriberRepository.save(subscriber);
            
                transactionManager.commit(status);
            } else {
                subscriber = null;
            }
        } catch(Exception e) {
            logger.error("An error occured on adding subscriber avatar", e);
            transactionManager.rollback(status);
            subscriber = null;
            throw new Exception(e);
        }
        return subscriber;
    }
    
    @Override
    public BigDecimal getSpentMoney(Subscriber subscriber, boolean withPenalties) {
        if (!withPenalties) return new BigDecimal(""+subscriptionService.getSumBySubscriber(subscriber));
        else {
            logger.info("===> Subscriber is " + subscriber);
            BigDecimal subscriptions = new BigDecimal(""+subscriptionService.getSumBySubscriber(subscriber));
            BigDecimal penalties = new BigDecimal(""+penaltyService.getSumBySubscriber(subscriber));
            logger.info("Found subscriptions " + subscriptions);
            logger.info("Found penalties " + penalties);
            // return (subscriptions - penalties);
            return subscriptions.subtract(penalties);
        }
    }

    @Override
    public int getActivityPoints(Subscriber subscriber, BigDecimal spentMoney) {
        List<Suggestion> suggestions = suggestionRepository.foundBySubscriber(subscriber);
        List<Question> questions = questionRepository.foundBySubscriber(subscriber);
        int points = 0;
        if (newsletterSubscriberRepository.foundBySubscriber(subscriber) != null) points++;
        if (suggestions != null && suggestions.size() > 0) points++;
        if (borrowingService.countBorrowedBySubscriber(subscriber, null, null) > 0) points++;
        if (questions != null && questions.size() > 0) points++;
        if (chatService.getLastEntryBySubscriber(subscriber) != null) points++;
        logger.info("===> "+chatService.getLastEntryBySubscriber(subscriber));
        if (spentMoney.doubleValue() < 0) points--; 
        logger.info("=points" + points);
        return points;
    }
}