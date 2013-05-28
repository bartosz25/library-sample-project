package com.example.library.form;

import java.util.List;

import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODOS : 
 * - dans JSP, champ Receiver, on a la méthode toString() du Subscriber et non pas le login
 */
public class TransferForm {
    final Logger logger = LoggerFactory.getLogger(TransferForm.class);
    private Subscriber receiver;
    private Subscriber subscriber;
    private List<Subscription> subscriptions;
    private Subscription subscriptionChecked;
    
    public void setReceiver(Subscriber receiver) {
        this.receiver = receiver;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setSubscriptionChecked(Subscription subscriptionChecked) {
        this.subscriptionChecked = subscriptionChecked;
    }

    public Subscriber getReceiver() {
        return receiver;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public Subscription getSubscriptionChecked() {
        return subscriptionChecked;
    }

    public String toString() {
        return "TransferForm [receiver : "+receiver+", subscriber : "+subscriber+"]";
    }
}