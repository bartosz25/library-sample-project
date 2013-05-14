package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NewsletterSendPK implements Serializable {
    private long idNewsletter;
    private long idSubscriber;

    public NewsletterSendPK() {}

    // @Column(name = "newsletter_id_ne")
    public long getIdNewsletter() {
	    return idNewsletter;
    }

    // @Column(name = "newsletter_subscriber_id_ns")
    public long getIdSubscriber() {
	    return idSubscriber;
    }
	
    public void setIdNewsletter(long idNewsletter) {
	    this.idNewsletter = idNewsletter;
    }

    public void setIdSubscriber(long idSubscriber) {
	    this.idSubscriber = idSubscriber;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        NewsletterSendPK newsletterSendPK = (NewsletterSendPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idNewsletter, newsletterSendPK.getIdNewsletter())
            .append(idSubscriber, newsletterSendPK.getIdSubscriber())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idNewsletter)
            .append(idSubscriber)
            .toHashCode();
    }

    public String toString() {
        return idNewsletter+"-"+idSubscriber;
    }
}