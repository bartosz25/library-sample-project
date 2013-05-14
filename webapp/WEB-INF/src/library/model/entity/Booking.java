package library.model.entity;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;

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
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name="booking")
// à activer si un nouveau groupe apparaît @CSRFConstraint(message = "{error.csrf.invalid}", groups = {Default.class})
public class Booking extends ParentEntity implements Serializable {
    public static final int STATE_FREEZE = 0;
    public static final int STATE_ACTIVE = 1;
    private BookingPK bookingPK;
    private BookCopy bookCopy;
    private Subscriber subscriber;
    private Date bookingDate;
    private Date addedDate;
    private int state;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idBookCopy", column = @Column(name = "book_copy_id_bc", nullable = false)),
        @AttributeOverride(name = "idSubscriber", column = @Column(name = "subscriber_id_su", nullable = false))
    })
    public BookingPK getBookingPK() {
        return bookingPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id_bc", nullable = false, insertable = false, updatable = false)
    public BookCopy getBookCopy() {
        return bookCopy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id_su", nullable = false, insertable = false, updatable = false)
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @Temporal(DATE)
    @NotNull(message = "{error.booking.dateEmpty}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_boo")
    public Date getBookingDate() {
        return bookingDate;
    }

    @Temporal(TIMESTAMP)
    // @NotNull(message = "{error.booking.addedDateEmpty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "added_boo")
    public Date getAddedDate() {
        return addedDate;
    }

    @Column(name="state_boo")
    public int getState() {
        return state;
    }

    public void setBookingPK(BookingPK bookingPK) {
        this.bookingPK = bookingPK;
    }

    public void setBookingPK(BookCopy bookCopy, Subscriber subscriber) {
        BookingPK bookingPK = new BookingPK();
        bookingPK.setIdBookCopy(bookCopy.getId());
        bookingPK.setIdSubscriber(subscriber.getId());
        this.bookingPK = bookingPK;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setBookingDate(Date bookingDate) {
       this.bookingDate = bookingDate;
    }

    public void setAddedDate(Date addedDate) {
       if (addedDate == null) addedDate = new Date();
       this.addedDate = addedDate;
    }

    public void setState(int state) {
       this.state = state;
    }

    public String toString() {
        return "Booking [bookingPK = "+bookingPK+", bookingDate = "+bookingDate+", addedDate = "+addedDate+", state = "+state+"]";
    }
}