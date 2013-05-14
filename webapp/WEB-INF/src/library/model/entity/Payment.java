package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODOS : 
 * - rajouter des validateurs pour certains types : double
 * - régler le problème du paiement PENDING (si PENDING et en même temps un administrateur le valide deux fois, l'utilisateur risque d'être débité deux fois) : il paye par PayPal mais la notification n'arrive pas immédiatement. Du coup il fait un virement, validé 1 jour plus tard par l'administrateur. La notification PayPal se débloque 3 jours après et le client est débité deux fois. Peut-être il faut le détecter au niveau de validation de notification.
 */

@Entity
@Table(name = "payment")
public class Payment extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Payment.class);
    private long id;
    // private int mode;
    private PaymentMethod paymentMethod;
    private String reference;
    private Date date;
    private int type;
    private double amount;
    private List<Subscription> subscriptions;
    private List<PaymentMethod> paymentMethods;

    @Id
    @GeneratedValue(strategy = IDENTITY)   
    @Column(name="id_pa")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id_pm")
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @NotEmpty(message = "{error.payment.referenceEmpty}")
    @Size(min = 1, max = 255, message = "{error.payment.referenceSize}")
    @Column(name="ref_pa")
    public String getReference() {
        return reference;
    }

    @Column(name="date_pa")
    public Date getDate() {
        return date;
    }

    @Column(name="type_pa")
    public int getType() {
        return type;
    }

    @Column(name="amount_pa")
    public double getAmount() {
        return amount;
    }

    @OneToMany(mappedBy = "subscriber")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Transient
    public int getSubscriptionType() {
        return 1;
    }

    @Transient
    public int getPenaltyType() {
        return 2;
    }

    @Transient
    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String toString() {
        return "Payment [id :"+id+" , reference : "+reference+","+ 
        " date :"+date+" , type :"+type+", paymentMethod : "+paymentMethod+" ]";
    }
}