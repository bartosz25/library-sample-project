package library.service;

import library.form.TransferForm;
import library.model.entity.SubscriptionTransfer;

public interface SubscriptionTransferService {
    // public List<SubscriptionTransfer> findById();
    // public SubscriptionTransfer save(SubscriptionTransfer subscriptionTransfer);
    public SubscriptionTransfer transferToUser(TransferForm transferForm) throws Exception;
    // public void delete(SubscriptionTransfer subscriptionTransfer);
}