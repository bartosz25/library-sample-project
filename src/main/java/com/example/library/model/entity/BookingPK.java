package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BookingPK implements Serializable {
    private long idBookCopy;
    private long idSubscriber;

    public BookingPK() {
    }

    // @Column(name = "book_copy_id_bc")
    public long getIdBookCopy() {
	    return idBookCopy;
    }

    // @Column(name = "subscriber_id_su")
    public long getIdSubscriber() {
	    return idSubscriber;
    }

    public void setIdBookCopy(long idBookCopy) {
	    this.idBookCopy = idBookCopy;
    }

    public void setIdSubscriber(long idSubscriber) {
	    this.idSubscriber = idSubscriber;
    }

    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        BookingPK bookingPK = (BookingPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idBookCopy, bookingPK.getIdBookCopy())
            .append(idSubscriber, bookingPK.getIdSubscriber())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idBookCopy)
            .append(idSubscriber)
            .toHashCode();
    }
}