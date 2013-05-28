package com.example.library.service;

import java.util.List;

import com.example.library.form.NewsPreferenciesCredentialsForm;
import com.example.library.model.entity.Newsletter;
import com.example.library.model.entity.NewsletterSend;
import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.entity.Subscriber;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.webflow.core.collection.LocalAttributeMap;

public interface NewsletterSubscriberService {
    public NewsletterSubscriber getByMail(String mail);
    public NewsletterSubscriber addFromFlow(LocalAttributeMap conversationScope, UsernamePasswordAuthenticationToken user, String password);
    public NewsletterSubscriber getByMailAndPlainTextPassword(String mail, String password);
    public boolean modifySubscription(NewsPreferenciesCredentialsForm newsPreferenciesCredentialsForm, Subscriber subscriber);
    public List<Object[]> getByCriteria(List<String> preferencies, Newsletter newsletter, int limit);
    public NewsletterSend sendToSubcriber(NewsletterSend newsletterSend) throws Exception;
}