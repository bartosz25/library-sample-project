package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NewsletterSubscriberPreferencyPK implements Serializable {
    private long idNewsletterPreferencyCategory;
    private long idNewsletterSubscriber;

    public NewsletterSubscriberPreferencyPK() { }

    // @Column(name = "newsletter_preferency_category_id_npc")
    public long getIdNewsletterPreferencyCategory() {
	    return idNewsletterPreferencyCategory;
    }

    // @Column(name = "newsletter_subscriber_id_ns")
    public long getIdNewsletterSubscriber() {
	    return idNewsletterSubscriber;
    }
	
    public void setIdNewsletterPreferencyCategory(long idNewsletterPreferencyCategory) {
	    this.idNewsletterPreferencyCategory = idNewsletterPreferencyCategory;
    }

    public void setIdNewsletterSubscriber(long idNewsletterSubscriber) {
	    this.idNewsletterSubscriber = idNewsletterSubscriber;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        NewsletterSubscriberPreferencyPK newsletterSubscriberPreferencyPK = (NewsletterSubscriberPreferencyPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idNewsletterPreferencyCategory, newsletterSubscriberPreferencyPK.getIdNewsletterPreferencyCategory())
            .append(idNewsletterSubscriber, newsletterSubscriberPreferencyPK.getIdNewsletterSubscriber())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idNewsletterPreferencyCategory)
            .append(idNewsletterSubscriber)
            .toHashCode();
    }

    public String toString() {
        return idNewsletterPreferencyCategory+"-"+idNewsletterSubscriber;
    }
}