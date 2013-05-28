package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ChatPK implements Serializable {
    private long time;
    private long idSubscriber;
    private long idAdmin;

    public ChatPK() {}

    // @Column(name = "time_ch")
    public long getTime() {
	    return time;
    }

    // @Column(name = "subscriber_id_su")
    public long getIdSubscriber() {
	    return idSubscriber;
    }

    // @Column(name = "admin_id_ad")
    public long getIdAdmin() {
	    return idAdmin;
    }
	
    public void setIdSubscriber(long idSubscriber) {
	    this.idSubscriber = idSubscriber;
    }

    public void setIdAdmin(long idAdmin) {
	    this.idAdmin = idAdmin;
    }

    public void setTime(long time) {
	    this.time = time;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        ChatPK chatPK = (ChatPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idSubscriber, chatPK.getIdSubscriber())
            .append(idAdmin, chatPK.getIdAdmin())
            .append(time, chatPK.getTime())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idSubscriber)
            .append(idAdmin)
            .append(time)
            .toHashCode();
    }

    public String toString() {
        return time+"-"+idSubscriber+"-"+idAdmin;
    }
}