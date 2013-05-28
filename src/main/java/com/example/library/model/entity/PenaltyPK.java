package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PenaltyPK extends ParentEntity implements Serializable {

    private long time;
    private long idSubscriber;

    public PenaltyPK() {}

    // @Column(name = "time_pe")
    public long getTime() {
	    return time;
    }

    // @Column(name = "subscriber_id_su")
    public long getIdSubscriber() {
	    return idSubscriber;
    }
	
    public void setIdSubscriber(long idSubscriber) {
	    this.idSubscriber = idSubscriber;
    }

    public void setTime(long time) {
	    this.time = time;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        PenaltyPK penaltyPK = (PenaltyPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idSubscriber, penaltyPK.getIdSubscriber())
            .append(time, penaltyPK.getTime())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idSubscriber)
            .append(time)
            .toHashCode();
    }

    public String toString() {
        return time+"-"+idSubscriber;
    }
}