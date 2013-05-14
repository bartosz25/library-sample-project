package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODOS : 
 * - transformer subscriber et lastNewsletter en @OneToOne
 */
@Entity
@Table(name="newsletter_subscriber")
public class NewsletterSubscriber extends ParentEntity implements Serializable {
    public final static int STATE_NOT_CONFIRMED = 0;
    public final static int STATE_CONFIRMED = 1;
    public final static int STATE_DELETED = 2;
    private long id;
    private Subscriber subscriber;
    private String email;
    private String password;
    private Date created;
    private int state;
    private List<NewsletterSend> sendNewsletters;
    private List<NewsletterSubscriberPreferency> preferencies;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_ns")
    public long getId() {
        return id;
    }

    @ManyToOne()
    @JoinColumn(name = "subscriber_id_su")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @NotEmpty(message = "{error.newsletterSubscriber.emailEmpty}")
    @Column(name="email_ns")
    public String getEmail() {
        return email;
    }

    @NotEmpty(message = "{error.newsletterSubscriber.passwordEmpty}")
    @Column(name="password_ns")
    public String getPassword() {
        return password;
    }

    @Temporal(TIMESTAMP)
    @NotNull(message = "{error.newsletterSubscriber.dateEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="date_ns")
    public Date getCreated() {
        return created;
    }

    @Column(name="state_ns")
    public int getState() {
        return state;
    }

    @OneToMany(mappedBy = "newsletterSubscriber")
    public List<NewsletterSend> getSendNewsletters() {
        return sendNewsletters;
    }

    @OneToMany(mappedBy = "newsletterSubscriber")
    public List<NewsletterSubscriberPreferency> getPreferencies() {
        return preferencies;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated(Date created) {
        if (created == null) created = new Date();
        this.created = created;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setSendNewsletters(List<NewsletterSend> sendNewsletters) {
        this.sendNewsletters = sendNewsletters;
    }

    public void setPreferencies(List<NewsletterSubscriberPreferency> preferencies) {
        this.preferencies = preferencies;
    }

    public String toString() {
        return "NewsletterSubscriber [id = "+id+", subscriber = "+subscriber+","+
        "email ="+email+", password ="+password+", created ="+created+"]";
    }
}