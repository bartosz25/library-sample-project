package library.service;

import java.util.List;

import library.model.entity.NewsletterSubscriber;

public interface NewsletterSubscriberPreferencyService {
    public List<String> getSavedPreferencies(NewsletterSubscriber newsletterSubscriber);
}