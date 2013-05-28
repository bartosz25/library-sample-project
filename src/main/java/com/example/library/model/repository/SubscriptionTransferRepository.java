package com.example.library.model.repository;

import com.example.library.model.entity.Subscription;
import com.example.library.model.entity.SubscriptionTransfer;
import com.example.library.model.entity.SubscriptionTransferPK;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubscriptionTransferRepository extends CrudRepository<SubscriptionTransfer, SubscriptionTransferPK> {
    @Query("SELECT sf FROM SubscriptionTransfer sf WHERE sf.subscriptionTransferPK.idReceiver = :receiver AND sf.subscription = :subscription")
    public SubscriptionTransfer alreadyGiven(@Param("receiver") long receiver, @Param("subscription") Subscription subscription);

    @Query("SELECT sf FROM SubscriptionTransfer sf WHERE sf.subscriptionTransferPK.idGiver = :giver AND sf.subscriptionTransferPK.idReceiver = :receiver AND sf.subscription = :subscription AND sf.state = :state")
    public SubscriptionTransfer getByReceiverAndSubscription(@Param("giver") long giver, @Param("receiver") long receiver, @Param("subscription") Subscription subscription, @Param("state") int state);
}