package com.example.library.validator.form;

import java.util.Date;

import com.example.library.form.SubscribeForm;
import com.example.library.model.entity.PaymentMethod;
import com.example.library.model.repository.PaymentMethodRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * TODO : 
 * - si PaymentMethodRepository n'est pas passé dans constructeur, il est null - à voir pourquoi @Autowired n'est pas pris en compte
 */
public class SubscribeFormValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(SubscribeFormValidator.class);
    private PaymentMethodRepository paymentMethodRepository;
    
    public SubscribeFormValidator(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }
    
    public boolean supports(Class clazz) {
        return SubscribeForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        SubscribeForm subscribeForm = (SubscribeForm) obj;
        logger.info("================================> subscribeForm " + subscribeForm);
        logger.info("================================> subscribeFormgetModeChecked " + subscribeForm.getModeChecked());
        PaymentMethod paymentMethod = subscribeForm.getModeChecked();
        if (paymentMethod == null || !paymentMethodRepository.exists(paymentMethod.getId())) {
            errors.rejectValue(
                "modeChecked",
                "error.subscriberForm.modeEmpty",
                "Payment mode can't be empty"
            );
        } else {
            if (subscribeForm.getModes().indexOf(paymentMethod) == -1) {
                errors.rejectValue(
                    "modeChecked", "error.subscriberForm.modeEmpty", "Payment mode can't be empty"
                );
            }
        }
        if (!subscribeForm.getTypes().containsKey(subscribeForm.getTypeChecked())) {
            errors.rejectValue(
                "typeChecked", "error.subscriberForm.typeEmpty", "Subscription type can't be empty"
            );
        }
        if (subscribeForm.getStartDate() == null) {
            errors.rejectValue(
                "startDate", "error.subscriberForm.dateEmpty", "Subscription's date can't be empty"
            );
        } else if (subscribeForm.getStartDate().before(new Date())) {
            errors.rejectValue(
                "startDate", "error.subscriberForm.datePast", "Subscription's date can't be in the past"
            );
        }
    }
}