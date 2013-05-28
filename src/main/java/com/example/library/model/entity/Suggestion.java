package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "suggestion")
public class Suggestion extends ParentEntity implements Serializable {
    public static final int STATE_NEW = 1;
    public static final int STATE_READ = 2;
    public static final int STATE_ACCEPTED = 3;
    public static final int STATE_REFUSED = 4;
    private long id;
    private Subscriber subscriber;
    private String title;
    private int quantity;
    private int state;
    private Date deliveryDate;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_sug")
    public long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @NotEmpty(message = "{error.suggestion.titleEmpty}")
    @Size(min = 1, max = 155, message = "{error.suggestion.titleSize}")
    @Column(name="title_sug")
    public String getTitle() {
        return title;
    }

    @Column(name="quantity_sug")
    public int getQuantity() {
        return quantity;
    }

    @Column(name="state_sug")
    public int getState() {
        return state;
    }

    @Column(name="delivery_sug")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String toString() {
        return "Suggestion [id :"+id+" , subscriber :"+subscriber+" , title :"+title+" ,"+
        "quantity :"+quantity+" , state :"+state+" , deliveryDate :"+deliveryDate+" ]";
    }
}