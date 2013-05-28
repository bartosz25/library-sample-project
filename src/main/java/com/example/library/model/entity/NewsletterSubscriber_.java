package com.example.library.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

// to know more : http://docs.oracle.com/javaee/6/tutorial/doc/gjiup.html

@StaticMetamodel(NewsletterSubscriber.class)
public class NewsletterSubscriber_ implements Serializable {
    public static volatile SingularAttribute<NewsletterSubscriber, Long> id;
    public static volatile SingularAttribute<NewsletterSubscriber, Newsletter> lastNewsletter;
    public static volatile SingularAttribute<NewsletterSubscriber, Subscriber> subscriber;
    public static volatile SingularAttribute<NewsletterSubscriber, String> email;
    public static volatile SingularAttribute<NewsletterSubscriber, String> password;
    public static volatile SingularAttribute<NewsletterSubscriber, Date> created;
    public static volatile SingularAttribute<NewsletterSubscriber, Integer> state;
    public static volatile ListAttribute<NewsletterSubscriber, NewsletterSend> sendNewsletters;
    public static volatile ListAttribute<NewsletterSubscriber, NewsletterSubscriberPreferency> preferencies;
}