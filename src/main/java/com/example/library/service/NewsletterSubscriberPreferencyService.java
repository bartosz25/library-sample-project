package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.NewsletterSubscriber;

public interface NewsletterSubscriberPreferencyService {
    public List<String> getSavedPreferencies(NewsletterSubscriber newsletterSubscriber);
}