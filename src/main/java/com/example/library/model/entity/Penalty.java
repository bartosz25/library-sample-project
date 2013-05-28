package com.example.library.model.entity;

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
import javax.persistence.Transient;
@Entity
@Table(name = "penalty")
public class Penalty extends ParentEntity implements Serializable {
    private PenaltyPK penaltyPK;
    // private int time;
    private Subscriber subscriber;
    private Payment payment;
    private PaymentMethod paymentMethod;
    private double amount;
    private int state;
    private String about;
    private String checkboxLabel;
    public final static int STATE_WAITING = 0;
    public final static int STATE_PAYED = 1;
    public final static int STATE_PENDING = 2;
    private double amountPerDay = 2.5;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "time", column = @Column(name = "time_pe", nullable = false)),
        @AttributeOverride(name = "idSubscriber", column = @Column(name = "subscriber_id_su", nullable = false))
    })
    public PenaltyPK getPenaltyPK() {
        return penaltyPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su", nullable = false, insertable = false, updatable = false)
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id_pa")
    public Payment getPayment() {
        return payment;
    }

    @Column(name="amount_pe")
    public double getAmount() {
        return amount;
    }

    @Column(name="state_pe")
    public int getState() {
        return state;
    }

    @Column(name="about_pe")
    public String getAbout() {
        return about;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id_pm")
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Transient
    public String getCheckboxLabel() {
        if (checkboxLabel != null) return checkboxLabel;
        setCheckboxLabel(about + " " + amount);
        return checkboxLabel;
    }

    @Transient
    public double getPenaltyAmount(int days) {
        return days*amountPerDay;
    }

    public void setPenaltyPK(PenaltyPK penaltyPK) {
        this.penaltyPK = penaltyPK;
    }

    public void setPenaltyPK(long time, long idSubscriber) {
        this.penaltyPK = new PenaltyPK();
        this.penaltyPK.setTime(time);
        this.penaltyPK.setIdSubscriber(idSubscriber);
    }

    public void setCheckboxLabel(String checkboxLabel) {
        this.checkboxLabel = checkboxLabel;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String toString() {
        return "Penalty [penaltyPK : "+penaltyPK+", subscriber :"+subscriber+" , "+
        "payment :"+payment+" , paymentMethod :"+paymentMethod+", amount :"+amount+" , state :"+state+" ]";
    }
}