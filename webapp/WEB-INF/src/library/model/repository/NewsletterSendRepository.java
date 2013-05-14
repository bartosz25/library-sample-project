package library.model.repository;

import java.util.Date;

import library.model.entity.NewsletterSend;
import library.model.entity.NewsletterSendPK;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterSendRepository extends CrudRepository<NewsletterSend, NewsletterSendPK> {
    @Query("SELECT ns FROM NewsletterSend ns WHERE ns.newsletterSendPK.idNewsletter = :idNewsletter AND ns.newsletterSendPK.idSubscriber = :idNewsletterSubscriber")
    public NewsletterSend getByNewsletterAndNewsletterSubscriber(@Param("idNewsletter") long idNewsletter, @Param("idNewsletterSubscriber") long idNewsletterSubscriber);

    // We need to load all relations; otherwise, the Exception "could not initialize proxy - no Session"
    // occurrs.
    @Query("SELECT ns, n, nsub, a, s FROM NewsletterSend ns JOIN ns.newsletter n JOIN ns.newsletterSubscriber nsub JOIN n.admin a LEFT JOIN nsub.subscriber s WHERE ns.state = :state")
    public Page<Object[]> getReceiversToSend(@Param("state") int state, Pageable pageable);
}