package com.example.library.service;

import java.util.Date;
import java.util.List;

import com.example.library.form.SubscribeForm;
import com.example.library.model.entity.Payment;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Subscription;
import com.example.library.model.entity.SubscriptionTransfer;

public interface SubscriptionService {
    // public List<Subscription> findById();
    public Subscription saveSubscribeNotValid(SubscribeForm subscribeForm) throws Exception;
    public Subscription addPayment(Subscription subscription, Payment payment) throws Exception;
    public Subscription transferSubscription(SubscriptionTransfer subscriptionTransfer);
    public List<Subscription> getOverlapSubscriptionBySubscriber(Subscriber subscriber, Date start, Date end);
    public double getSumBySubscriber(Subscriber subscriber);
    // public void delete(Subscription subscription);
}