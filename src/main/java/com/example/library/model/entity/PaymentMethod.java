package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "payment_method")
public class PaymentMethod extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(PaymentMethod.class);
    private long id;
    private String code;
    private String name;
    private String className;
    private String serviceName;
    private String validity;
    private List<Subscription> subscriptions;
    private List<Payment> payments;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)   
    @Column(name="id_pm")
    public long getId() {
        return id;
    }

    @Column(name="code_pm")
    public String getCode() {
        return code;
    }

    @Column(name="name_pm")
    public String getName() {
        return name;
    }

    @Column(name="service_class_pm")
    public String getClassName() {
        return className;
    }

    @Column(name="service_name_pm")
    public String getServiceName() {
        return serviceName;
    }

    @Column(name="validity_pm")
    public String getValidity() {
        return validity;
    }

    @OneToMany(mappedBy = "paymentMethod")
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @OneToMany(mappedBy = "paymentMethod")
    public List<Payment> getPayments() {
        return payments;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String toString() {
        return "PaymentMethod [id : "+id+" , code : "+code+", name : "+name+", className : "+className+", "+
        "serviceName : "+serviceName+", validity : "+validity+"]";
    }
}