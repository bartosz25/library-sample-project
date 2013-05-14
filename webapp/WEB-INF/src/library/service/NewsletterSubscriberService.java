package library.service;

import java.util.List;

import library.form.NewsPreferenciesCredentialsForm;
import library.model.entity.Newsletter;
import library.model.entity.NewsletterSend;
import library.model.entity.NewsletterSubscriber;
import library.model.entity.Subscriber;

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