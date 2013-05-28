package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO : 
 * - vérifier (aussi vérifier PageContent.java) s'il faut appliquer la relation OneToOne ou OneToMany
 */
@Entity
@Table(name="borrowing")
public class Borrowing extends ParentEntity implements Serializable {
    private final static int BORROWING_LIMIT = 7;
    public final static int ALERT_NO = 0;
    public final static int ALERT_CRITICAL = 5;
    public final static int PENALIZED_NO = 0;
    public final static int PENALIZED_YES = 1;
    private long id;
    private BookCopy bookCopy;
    private Subscriber subscriber;
    private Date dateFrom;
    private Date dateTo;
    private int alert;
    private int penalized;
    private Date lastActionDate;
    private boolean insertPenalty;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_bor")
    public long getId() {
        return id;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="book_copy_id_bc")
    public BookCopy getBookCopy() {
        return bookCopy;
    }

    @OneToOne(fetch=FetchType.LAZY)
    // @OneToOne()
    @JoinColumn(name="subscriber_id_su")
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @Temporal(DATE)
    @NotNull(message = "{error.borrowing.dateFromEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name="date_bor")
    public Date getDateFrom() {
        return dateFrom;
    }

    @Temporal(DATE)
    @NotNull(message = "{error.borrowing.dateToEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name="return_bor")
    public Date getDateTo() {
        return dateTo;
    }

    @Column(name="return_alert_bor")
    public int getAlert() {
        return alert;
    }

    @Column(name="penalized_bor")
    public int getPenalized() {
        return penalized;
    }

    @Temporal(DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name="last_action_bor")
    public Date getLastActionDate() {
        return lastActionDate;
    }

    public boolean hasAlert() {
        return alert > ALERT_NO;
    }

    public boolean hasCriticalAlert() {
        return alert > ALERT_CRITICAL;
    }

    public boolean wasPenalized() {
        return penalized == PENALIZED_YES;
    }

    public boolean canInsertPenalty() {
        return (hasAlert() && !wasPenalized() && hasCriticalAlert());
    }

    @Transient
    public boolean getInsertPenalty() {
        return insertPenalty;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public void setDateTo(Date dateTo, boolean calculDate) {
        if (calculDate) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTo);
            calendar.add(Calendar.DATE, BORROWING_LIMIT);
            dateTo = calendar.getTime();
        }
        this.dateTo = dateTo;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public void setPenalized(int penalized) {
        this.penalized = penalized;
    }

    public void setInsertPenalty(boolean insertPenalty) {
        this.insertPenalty = insertPenalty;
    }

    public String toString() {
        return "Borrowing [id = "+id+", BookCopy = "+bookCopy+", Subscriber = "+subscriber+", dateFrom = "+dateFrom+", dateTo = "+dateTo+" , alert = "+alert+", penalized = "+penalized+", lastActionDate = "+lastActionDate+"]";
    }
}