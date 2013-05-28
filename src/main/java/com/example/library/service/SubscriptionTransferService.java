package com.example.library.service;

import com.example.library.form.TransferForm;
import com.example.library.model.entity.SubscriptionTransfer;

public interface SubscriptionTransferService {
    // public List<SubscriptionTransfer> findById();
    // public SubscriptionTransfer save(SubscriptionTransfer subscriptionTransfer);
    public SubscriptionTransfer transferToUser(TransferForm transferForm) throws Exception;
    // public void delete(SubscriptionTransfer subscriptionTransfer);
}