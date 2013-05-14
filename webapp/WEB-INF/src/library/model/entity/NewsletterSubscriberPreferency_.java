package library.model.entity;

import java.io.Serializable;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(NewsletterSubscriberPreferency.class)
public class NewsletterSubscriberPreferency_ implements Serializable {
    public static volatile SingularAttribute<NewsletterSubscriber, NewsletterSubscriberPreferencyPK> newsletterSubscriberPreferencyPK;
    public static volatile SingularAttribute<NewsletterSubscriberPreferency, NewsletterSubscriber> newsletterSubscriber;
    public static volatile SingularAttribute<NewsletterSubscriberPreferency, NewsletterPreferencyCategory> newsletterPreferencyCategory;
    public static volatile SingularAttribute<NewsletterSubscriberPreferency, Long> preferency;
    public static volatile SingularAttribute<NewsletterSubscriberPreferency, String> value;
}