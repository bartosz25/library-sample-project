package library.service;

import library.model.entity.Newsletter;
import library.model.entity.NewsletterSend;
import library.model.entity.NewsletterSubscriber;

import org.springframework.data.domain.Page;


public interface NewsletterSendService {
    public NewsletterSend addToSendList(NewsletterSubscriber newsletterSubscriber, Newsletter newsletter);
    public Page<Object[]> getToSend(int offset, int limit);
}