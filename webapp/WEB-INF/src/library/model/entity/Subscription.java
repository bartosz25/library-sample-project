package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO : 
 * - régler Field error in object 'subscribeForm' on field 'startDate': rejected value [sdqdsd]; codes [typeMismatch.subscribeForm.startDate,typeMismatch.startDate,typeMismatch.jav
a.util.Date,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [subscribeForm.startDate,startDate]; arguments []; defau
lt message [startDate]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.util.Date' for property 'startDate'; nested
 exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type java.lang.String to type @javax.persistence.Temporal @org.springfr
amework.format.annotation.DateTimeFormat java.util.Date for value 'sdqdsd'; nested exception is java.lang.IllegalArgumentException: Invalid format: "sdqdsd"] quand on insère un texte dans le champ "date"
 * - rajouter type : actif / inactif 
 */
@Entity
@Table(name = "subscription")
public class Subscription extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Subscription.class);
    private long id;
    private Subscriber subscriber;
    private Payment payment;
    private int type;
    private int paymentMode;
    private double amount;
    private Date start;
    private Date end;
    private double weeklyPrice = 20.00d;
    private double monthlyPrice = 40.00d;
    private PaymentMethod paymentMethod;
    private List<SubscriptionTransfer> subscriptionTransfers;
    // TODO : exporter depuis la base ou un fichier de config
    private final static Map<Integer, HashMap<String, Object>> types = new HashMap<Integer, HashMap<String, Object>>() {{
        put(1, new HashMap<String, Object>() {{
                   put("code", "7days");
                   put("duration", 1);
                   put("entity", Calendar.DATE);
                   put("discount", 0.00d);
               }}
        );
        put(2, new HashMap<String, Object>() {{
                   put("code", "14days");
                   put("duration", 2);
                   put("entity", Calendar.DATE);
                   put("discount", 5.00d);
               }}
        );
        put(3, new HashMap<String, Object>() {{
                   put("code", "1month");
                   put("duration", 1);
                   put("entity", Calendar.MONTH);
                   put("discount", 10.00d);
               }}
        );
        put(4, new HashMap<String, Object>() {{
                   put("code", "6months");
                   put("duration", 6);
                   put("entity", Calendar.MONTH);
                   put("discount", 15.00d);
               }}
        );
        put(5, new HashMap<String, Object>() {{
                   put("code", "12months");
                   put("duration", 12);
                   put("entity", Calendar.MONTH);
                   put("discount", 20.00d);
               }}
        );
    }};
    
    @Id
    @GeneratedValue(strategy = IDENTITY)   
    @Column(name="id_sub")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id_pa")
    public Payment getPayment() {
        return payment;
    }

    @Column(name="type_sub")
    public int getType() {
        return type;
    }

    @Column(name="amount_sub")
    public double getAmount() {
        return amount;
    }

    @Temporal(DATE)
    @NotNull(message = "{error.subscription.dateStartInvalid}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="start_sub")
    public Date getStart() {
        return start;
    }

    @Temporal(DATE)
    @NotNull(message = "The date can't be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="end_sub")
    public Date getEnd() {
        return end;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id_pm")
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @OneToMany(mappedBy = "subscription")
    public List<SubscriptionTransfer> getSubscriptionTransfers() {
        return subscriptionTransfers;
    }

    @Transient
    public HashMap<String, Object> getPaymentConfig(int type) {
        if (type > 0) this.type = type;
        if (types.containsKey(this.type)) return types.get(this.type);
        return null;
    }

    @Transient
    public static Map<Integer, HashMap<String, Object>> getTypes() {
        return types;
    }

    public double calculAmount() {
        try {
            return calculAmount(this.type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return -1.0;
    }

    public double calculAmount(int type) throws Exception {
        HashMap<String, Object> config = getPaymentConfig(type);
        if (config == null) {
            throw new Exception("Payment config was not found");
        }
        double price = weeklyPrice;
        if ((Integer) (config.get("entity")) == Calendar.MONTH) price = monthlyPrice;
        double amount = price * (Integer)config.get("duration");
        double discount = (price*(Double)config.get("discount"))/100;
        amount = amount - discount;
        setAmount(amount);
        return amount;
    }

    public Date calculEnd() throws Exception {
        HashMap<String, Object> config = getPaymentConfig(type);
        if (config == null) {
            throw new Exception("Payment config was not found");
        }
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(getStart());
        calendarFrom.add((Integer)config.get("entity"), (Integer)config.get("duration"));
        Date endDate = calendarFrom.getTime();
        setEnd(endDate);
        return endDate;
    }

    @Transient
    public String getFormLabel() {
        return (String) types.get(type).get("code");
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {   
        this.end = end;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setSubscriptionTransfers(List<SubscriptionTransfer> subscriptionTransfers) {
        this.subscriptionTransfers = subscriptionTransfers;
    }

    public String toString() {
        return "Subscription [id : "+id+", subscriber : "+subscriber+", payment : "+payment+","+ 
        "type : "+type+" , amount : "+amount+", start : "+start+", end : " + end + ", paymentMethod : "+paymentMethod+"]";
    }
}