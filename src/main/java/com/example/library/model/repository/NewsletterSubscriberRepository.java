package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterSubscriberRepository extends CrudRepository<NewsletterSubscriber, Long> {
    @Query("SELECT ns FROM NewsletterSubscriber ns WHERE ns.email = :email")
    public NewsletterSubscriber getByMail(@Param("email") String email);

    @Query("SELECT ns FROM NewsletterSubscriber ns WHERE ns.email = :email AND ns.state = :state")
    public NewsletterSubscriber getByMailAndState(@Param("email") String email, @Param("state") int state);

    @Query("SELECT ns FROM NewsletterSubscriber ns WHERE ns.email = :email AND ns.password = :password AND ns.state = :state")
    public NewsletterSubscriber getByMailAndEncodedPassword(@Param("email") String email, @Param("password") String password, @Param("state") int state);

    @Query("SELECT ns FROM NewsletterSubscriber ns")
    public List<NewsletterSubscriber> getAll();

    @Query("SELECT ns FROM NewsletterSubscriber ns WHERE ns.subscriber = :subscriber")
    public NewsletterSubscriber foundBySubscriber(@Param("subscriber") Subscriber subscriber);

    // @Query("SELECT ns FROM NewsletterSubscriber ns JOIN ns.preferencies nsp WHERE CONCAT_WS('_', nsp.newsletterSubscriberPreferencyPK.idNewsletterPreferencyCategory, nsp.preferency) IN :codes GROUP BY ns.id")
    // public List<NewsletterSubscriber> getByPreferencies(@Param("codes") List<String> codes, @Param("count") int count);

    // @Query("SELECT ns FROM NewsletterSubscriber ns JOIN ns.preferencies nsp WHERE CONCAT_WS('_', nsp.newsletterSubscriberPreferencyPK.idNewsletterPreferencyCategory, nsp.preferency) IN :codes AND ns.lastNewsletter != :newsletter GROUP BY ns.id HAVING COUNT(1) = :count ")
    // public List<NewsletterSubscriber> getByPreferenciesToSend(@Param("codes") List<String> codes, @Param("newsletter") Newsletter newsletter, @Param("count") int count);
}