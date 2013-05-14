package library.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TODOS :
 * - modifier la clé primaire en giver-receiver-subscription
 */
@Entity
@Table(name = "subscription_transfer")
public class SubscriptionTransfer extends ParentEntity implements Serializable {
    public static final int STATE_PENDING = 0;
    public static final int STATE_ACCEPTED = 1;
    public static final int STATE_DENIED = 2;
    private SubscriptionTransferPK subscriptionTransferPK;
    private Subscriber giver;
    private Subscriber receiver;
    private Subscription subscription;
    private Date date;
    private int state;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idGiver", column = @Column(name = "subscriber_id_su", nullable = false)),
        @AttributeOverride(name = "idReceiver", column = @Column(name = "to_subscriber", nullable = false))
    })
    public SubscriptionTransferPK getSubscriptionTransferPK() {
        return subscriptionTransferPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su", nullable = false, insertable = false, updatable = false)
    public Subscriber getGiver() {
        return giver;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_subscriber", nullable = false, insertable = false, updatable = false)
    public Subscriber getReceiver() {
       return receiver;
    }

    @Column(name="date_st")
    public Date getDate() {
        return date;
    }

    @Column(name="state_st")
    public int getState() {
        return state;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id_sub")
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscriptionTransferPK(SubscriptionTransferPK subscriptionTransferPK) {
        this.subscriptionTransferPK = subscriptionTransferPK;
    }

    public void setSubscriptionTransferPK(Subscriber giver, Subscriber receiver) {
        SubscriptionTransferPK subscriptionTransferPK = new SubscriptionTransferPK();
        subscriptionTransferPK.setIdGiver(giver.getId());
        subscriptionTransferPK.setIdReceiver(receiver.getId());
        this.subscriptionTransferPK = subscriptionTransferPK;
    }

    public void setGiver(Subscriber giver) {
        this.giver = giver;
    }

    public void setReceiver(Subscriber receiver) {
       this.receiver = receiver;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String toString() {
        return "SubscriptionTransfer [subscriptionTransferPK : "+subscriptionTransferPK+", giver :"+giver+","+ 
        "receiver :"+receiver+" , date : "+date+", subscription : "+subscription+"]";
    }
}