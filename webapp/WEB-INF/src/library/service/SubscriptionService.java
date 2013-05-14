package library.service;

import java.util.Date;
import java.util.List;

import library.form.SubscribeForm;
import library.model.entity.Payment;
import library.model.entity.Subscriber;
import library.model.entity.Subscription;
import library.model.entity.SubscriptionTransfer;

public interface SubscriptionService {
    // public List<Subscription> findById();
    public Subscription saveSubscribeNotValid(SubscribeForm subscribeForm) throws Exception;
    public Subscription addPayment(Subscription subscription, Payment payment) throws Exception;
    public Subscription transferSubscription(SubscriptionTransfer subscriptionTransfer);
    public List<Subscription> getOverlapSubscriptionBySubscriber(Subscriber subscriber, Date start, Date end);
    public double getSumBySubscriber(Subscriber subscriber);
    // public void delete(Subscription subscription);
}