package library.model.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "newsletter_subscriber_preferency")
public class NewsletterSubscriberPreferency extends ParentEntity implements Serializable {
    private NewsletterSubscriberPreferencyPK newsletterSubscriberPreferencyPK;
    private NewsletterSubscriber newsletterSubscriber;
    private NewsletterPreferencyCategory newsletterPreferencyCategory;
    private long preferency;
    private String value;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idNewsletterPreferencyCategory", column = @Column(name = "newsletter_preferency_category_id_npc", nullable = false)),
        @AttributeOverride(name = "idNewsletterSubscriber", column = @Column(name = "newsletter_subscriber_id_ns", nullable = false))
    })
    public NewsletterSubscriberPreferencyPK getNewsletterSubscriberPreferencyPK() {
        return newsletterSubscriberPreferencyPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_preferency_category_id_npc", nullable = false, insertable = false, updatable = false)
    public NewsletterPreferencyCategory getNewsletterPreferencyCategory() {
        return newsletterPreferencyCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_subscriber_id_ns", nullable = false, insertable = false, updatable = false)
    public NewsletterSubscriber getNewsletterSubscriber() {
        return newsletterSubscriber;
    }

    @Column(name="preferency_nsp")
    public long getPreferency() {
        return preferency;
    }

    @Column(name="value_nsp")
    public String getValue() {
        return value;
    }

    public void setNewsletterSubscriberPreferencyPK(NewsletterSubscriberPreferencyPK newsletterSubscriberPreferencyPK) {
        this.newsletterSubscriberPreferencyPK = newsletterSubscriberPreferencyPK;
    }

    public void setNewsletterSubscriberPreferencyPK(long idNewsletterPreferencyCategory, long idNewsletterSubscriber) {
        this.newsletterSubscriberPreferencyPK = new NewsletterSubscriberPreferencyPK();
        this.newsletterSubscriberPreferencyPK.setIdNewsletterPreferencyCategory(idNewsletterPreferencyCategory);
        this.newsletterSubscriberPreferencyPK.setIdNewsletterSubscriber(idNewsletterSubscriber);
    }

    public void setNewsletterPreferencyCategory(NewsletterPreferencyCategory newsletterPreferencyCategory) {
        this.newsletterPreferencyCategory = newsletterPreferencyCategory;
    }

    public void setNewsletterSubscriber(NewsletterSubscriber newsletterSubscriber) {
        this.newsletterSubscriber = newsletterSubscriber;
    }

    public void setPreferency(long preferency) {
        this.preferency = preferency;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "NewsletterSubscriberPreferency [newsletterSubscriberPreferencyPK : "+newsletterSubscriberPreferencyPK+", newsletterPreferencyCategory :"+newsletterPreferencyCategory+" , "+
        "newsletterSubscriber :"+newsletterSubscriber+" , value :"+value+"]";
    }
}