package library.model.entity;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "newsletter_send")
public class NewsletterSend extends ParentEntity implements Serializable {
    private NewsletterSendPK newsletterSendPK;
    private Newsletter newsletter;
    private NewsletterSubscriber newsletterSubscriber;
    private Date sendDate;
    private int state;
    public final static int STATE_NOT_OPENED = 0;
    public final static int STATE_OPENED = 1;
    public final static int STATE_TO_SEND = 2;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idNewsletter", column = @Column(name = "newsletter_id_ne", nullable = false)),
        @AttributeOverride(name = "idSubscriber", column = @Column(name = "newsletter_subscriber_id_ns", nullable = false))
    })
    public NewsletterSendPK getNewsletterSendPK() {
        return newsletterSendPK;
    }

    @ManyToOne()
    @JoinColumn(name = "newsletter_id_ne", nullable = false, insertable = false, updatable = false)
    public Newsletter getNewsletter() {
        return newsletter;
    }

    @ManyToOne()
    @JoinColumn(name = "newsletter_subscriber_id_ns", nullable = false, insertable = false, updatable = false)
    public NewsletterSubscriber getNewsletterSubscriber() {
        return newsletterSubscriber;
    }

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="date_nse")
    public Date getSendDate() {
        return sendDate;
    }

    @Column(name="state_nse")
    public int getState() {
        return state;
    }

    public void setNewsletterSendPK(NewsletterSendPK newsletterSendPK) {
        this.newsletterSendPK = newsletterSendPK;
    }

    public void setNewsletterSendPK(long idNewsletter, long idSubscriber) {
        this.newsletterSendPK = new NewsletterSendPK();
        this.newsletterSendPK.setIdNewsletter(idNewsletter);
        this.newsletterSendPK.setIdSubscriber(idSubscriber);
    }

    public void setNewsletterSubscriber(NewsletterSubscriber newsletterSubscriber) {
        this.newsletterSubscriber = newsletterSubscriber;
    }

    public void setNewsletter(Newsletter newsletter) {
        this.newsletter = newsletter;
    }

    public void setSendDate(Date sendDate) {
        if (sendDate == null) sendDate = new Date();
        this.sendDate = sendDate;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toString() {
        return "NewsletterSend [newsletterSendPK : "+newsletterSendPK+", newsletterSubscriber :"+newsletterSubscriber+" , "+
        "newsletter :"+newsletter+" , sendDate :"+sendDate+", state :"+state+" ]";
    }
}