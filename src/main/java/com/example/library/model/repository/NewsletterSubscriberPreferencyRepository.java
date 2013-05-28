package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.NewsletterSubscriberPreferency;
import com.example.library.model.entity.NewsletterSubscriberPreferencyPK;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterSubscriberPreferencyRepository extends CrudRepository<NewsletterSubscriberPreferency, NewsletterSubscriberPreferencyPK> {
    @Query("SELECT nsp FROM NewsletterSubscriberPreferency nsp  WHERE nsp.newsletterSubscriberPreferencyPK.idNewsletterSubscriber = :idNewsletterSubscriber")
    public List<NewsletterSubscriberPreferency> getByNewsletterSubscriber(@Param("idNewsletterSubscriber") long idNewsletterSubscriber);

    @Modifying
    @Query("DELETE FROM NewsletterSubscriberPreferency nsp WHERE nsp.newsletterSubscriberPreferencyPK.idNewsletterSubscriber = :idNewsletterSubscriber")
    public void deleteBySubscriber(@Param("idNewsletterSubscriber") long idNewsletterSubscriber);
}