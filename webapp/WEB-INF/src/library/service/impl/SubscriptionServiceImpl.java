package library.service.impl;

import java.util.Date;
import java.util.List;

import library.form.SubscribeForm;
import library.model.entity.Payment;
import library.model.entity.Subscriber;
import library.model.entity.Subscription;
import library.model.entity.SubscriptionTransfer;
import library.model.repository.PaymentMethodRepository;
import library.model.repository.PaymentRepository;
import library.model.repository.SubscriptionRepository;
import library.model.repository.SubscriptionTransferRepository;
import library.service.SubscriptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TODOS :
 * - cas quand les dates chevauchent dans SubscriptionTransfer
 */
@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionTransferRepository subscriptionTransferRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Subscription saveSubscribeNotValid(SubscribeForm subscribeForm) throws Exception {
        Subscription subscription = new Subscription();
        try {
            subscription.setSubscriber(subscribeForm.getSubscriber());
            subscription.setPayment(null);
            subscription.setType(subscribeForm.getTypeChecked());
            subscription.calculAmount();
            subscription.setStart(subscribeForm.getStartDate());
            subscription.calculEnd();
            // subscription.setPaymentMethod(paymentMethodRepository.findOne(subscribeForm.getModeChecked()));
            subscription.setPaymentMethod(subscribeForm.getModeChecked());
            subscription = subscriptionRepository.save(subscription);
        } catch(Exception e) {
            logger.error("An exception occured on saving SubscriptionRepository", e);
            subscription = null;
            throw new Exception(e);
        }
        return subscription;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'PAYMENT_ACCEPT')")
    public Subscription addPayment(Subscription subscription, Payment payment) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            payment = paymentRepository.save(payment);
            subscription.setPayment(payment);
            subscription = subscriptionRepository.save(subscription);
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving SubscriptionRepository", e);
            subscription = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return subscription;
    }

    @Override
    public List<Subscription> getOverlapSubscriptionBySubscriber(Subscriber subscriber, Date start, Date end) {
        List<Subscription> subscriptions = subscriptionRepository.getOverlapSubscriptionBySubscriber(subscriber, start, end);
        logger.info("Found user overlap subscriptions for ("+start+" and " + end + ") : " + subscriptions);
        return subscriptions;
    }
    
    @Override
    @PreAuthorize("hasRole('ROLE_USER') and principal.id == subscriptionTransfer.subscriptionTransferPK.idGiver")
    public Subscription transferSubscription(SubscriptionTransfer subscriptionTransfer) {
        Subscription subscription = subscriptionTransfer.getSubscription();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            subscriptionTransfer.setState(SubscriptionTransfer.STATE_ACCEPTED);
            subscriptionTransferRepository.save(subscriptionTransfer);
            subscription.setSubscriber(subscriptionTransfer.getReceiver());
            // TODO : Si les dates chevauchent, il faut faire la date d'abonnement à partir de la date de la fin de son abonemment précédent
            subscription = subscriptionRepository.save(subscription);
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving SubscriptionRepository", e);
            subscription = null;
            transactionManager.rollback(status);
        }
        return null;
    }
    
    @Override
    public double getSumBySubscriber(Subscriber subscriber) {
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriber(subscriber);
        if (subscriptions != null && subscriptions.size() > 0) {
            return subscriptionRepository.getSumBySubscriber(subscriber);
        }
        return 0.0d;
    }
}