package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SubscriptionTransferPK implements Serializable {
    private long idGiver;
    private long idReceiver;

    public SubscriptionTransferPK() {}

    // @Column(name = "subscriber_id_su")
    public long getIdGiver() {
	    return idGiver; 
    }

    // @Column(name = "to_subscriber")
    public long getIdReceiver() {
	    return idReceiver;
    }
	
    public void setIdGiver(long idGiver) {
	    this.idGiver = idGiver;
    }

    public void setIdReceiver(long idReceiver) {
	    this.idReceiver = idReceiver;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        SubscriptionTransferPK subscriptionTransferPK = (SubscriptionTransferPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idGiver, subscriptionTransferPK.getIdGiver())
            .append(idReceiver, subscriptionTransferPK.getIdReceiver())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idGiver)
            .append(idReceiver)
            .toHashCode();
    }
}