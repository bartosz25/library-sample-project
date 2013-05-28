package com.example.library.service;

import com.example.library.model.entity.Newsletter;
import com.example.library.model.entity.NewsletterSend;
import com.example.library.model.entity.NewsletterSubscriber;

import org.springframework.data.domain.Page;


public interface NewsletterSendService {
    public NewsletterSend addToSendList(NewsletterSubscriber newsletterSubscriber, Newsletter newsletter);
    public Page<Object[]> getToSend(int offset, int limit);
}