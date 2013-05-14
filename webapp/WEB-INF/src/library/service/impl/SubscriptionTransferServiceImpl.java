package library.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import library.form.TransferForm;
import library.model.entity.SubscriptionTransfer;
import library.model.repository.SubscriptionTransferRepository;
import library.service.SubscriptionTransferService;
import library.tools.MailerTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("subscriptionTransferService")
public class SubscriptionTransferServiceImpl implements SubscriptionTransferService {
    final Logger logger = LoggerFactory.getLogger(SubscriptionTransferServiceImpl.class);
    private static final String TPL_NOTIFY = "notifyTransfer";
    @Autowired
    private SubscriptionTransferRepository subscriptionTransferRepository;
    @Autowired
    private MailerTool mailerTool;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public SubscriptionTransfer transferToUser(TransferForm transferForm) throws Exception {
        SubscriptionTransfer subscriptionTransfer = new SubscriptionTransfer();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            subscriptionTransfer.setSubscriptionTransferPK(transferForm.getSubscriber(), transferForm.getReceiver());
            subscriptionTransfer.setDate(new Date());
            subscriptionTransfer.setSubscription(transferForm.getSubscriptionChecked());
            subscriptionTransfer.setState(SubscriptionTransfer.STATE_PENDING);
            subscriptionTransfer = subscriptionTransferRepository.save(subscriptionTransfer);
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_NOTIFY);
            mailData.put("to", transferForm.getReceiver().getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("subscription", subscriptionTransfer.getSubscription());
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            mailerTool.send();
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving SubscriptionTransferRepository", e);
            subscriptionTransfer = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return subscriptionTransfer;
    }
}