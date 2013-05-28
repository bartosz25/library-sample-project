package com.example.library.service;

import java.math.BigDecimal;

import com.example.library.model.entity.Subscriber;

public interface SubscriberService {
    // public List<Subscriber> findById();
    public Subscriber getById(long id);
    public Subscriber save(Subscriber subscriber) throws Exception;
    public Subscriber confirm(Subscriber subscriber);
    public Subscriber findNonConfirmedById(long id);
    public Subscriber loadByUsername(String login);
    public Subscriber addAvatar(Subscriber subscriber) throws Exception;
    public void updateEmail(Subscriber subscriber) throws Exception;
    public void updatePassword(Subscriber subscriber) throws Exception;
    public void revive(int days);
    public BigDecimal getSpentMoney(Subscriber subscriber, boolean withPenalties);
    public int getActivityPoints(Subscriber subscriber, BigDecimal debt);
    // public Subscriber findByPrincipal(AuthenticationFrontendUserDetails user);
    // public void delete(Subscriber subscriber);
}