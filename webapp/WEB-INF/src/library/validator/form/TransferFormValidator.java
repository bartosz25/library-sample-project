package library.validator.form;

import java.util.List;

import library.form.TransferForm;
import library.model.entity.Subscriber;
import library.model.entity.Subscription;
import library.model.repository.SubscriptionRepository;
import library.model.repository.SubscriptionTransferRepository;
import library.service.SubscriptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TransferFormValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(TransferFormValidator.class);
    private SubscriptionRepository subscriptionRepository;
    private SubscriptionTransferRepository subscriptionTransferRepository;
    private SubscriptionService subscriptionService;
    
    public TransferFormValidator(SubscriptionRepository subscriptionRepository, SubscriptionService subscriptionService, SubscriptionTransferRepository subscriptionTransferRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionTransferRepository = subscriptionTransferRepository;
        this.subscriptionService = subscriptionService;
    }

    public boolean supports(Class clazz) {
        return TransferForm.class.equals(clazz);
    }
    
    /**
     * A subscription transfer can be done only when subscriber doesn't have any book to return.
     * In additional, transfered subscription must belong to subscriber. It can't be ended.
     * A period of active receiver's subscription can't overlap with given subscription.
     * 
     */
    public void validate(Object obj, Errors errors) {
        TransferForm transferForm = (TransferForm) obj;
        logger.info("================================> transferForm " + transferForm);
        if (transferForm.getSubscriber().getBookingNb() != Subscriber.BOOKINGS_NB) {
            errors.rejectValue("subscriber", "error.transferForm.subscriberBooking",
                "You must return books before you give your subscription"
            );
        }
        Subscription subscription = subscriptionRepository.findOne(transferForm.getSubscriptionChecked().getId());
        // get receiver's subscriptions
        List<Subscription> overlapSub = subscriptionService.getOverlapSubscriptionBySubscriber(transferForm.getReceiver(), transferForm.getSubscriptionChecked().getStart(), transferForm.getSubscriptionChecked().getEnd());
        if (subscription.getSubscriber() != transferForm.getSubscriber()) {
            errors.rejectValue(
                "subscriptionChecked",
                "error.transferForm.subscriberNotHimSubscription",
                "You can give only your subscription"
            );        
        } else if (overlapSub != null && overlapSub.size() > 0) {
            errors.rejectValue(
                "subscriptionChecked",
                "error.transferForm.subscriptionOverlap",
                "Given subscription can't overlap with receiver subscription"
            );              
        } else if (subscriptionTransferRepository.alreadyGiven(transferForm.getReceiver().getId(), transferForm.getSubscriptionChecked()) != null) {
            errors.rejectValue(
                "subscriptionChecked",
                "error.transferForm.subscriptionAlready",
                "You have already given this subscription to this user"
            );              
        }
    }
}