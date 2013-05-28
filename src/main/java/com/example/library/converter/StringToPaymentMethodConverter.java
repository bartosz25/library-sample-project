package com.example.library.converter;

import com.example.library.model.entity.PaymentMethod;
import com.example.library.model.repository.PaymentMethodRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * TODOS : 
 * - pour toutes les conversions des entités, il faut utiliser l'approche avec Repository et non pas juste avec setId()
 */
public class StringToPaymentMethodConverter implements Converter<String, PaymentMethod> {
    final Logger logger = LoggerFactory.getLogger(StringToPaymentMethodConverter.class);
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    public PaymentMethod convert(String method) {
        logger.info("============> StringToPaymentMethodConverter = " + method);
        try {
            long id = Long.parseLong(method.trim());
            PaymentMethod paymentMethod = paymentMethodRepository.findOne(id); //new PaymentMethod();
            // paymentMethod.setId(id);
            return paymentMethod;
        } catch(NumberFormatException e) {
            logger.error("Exception when trying to convert String ("+method+")to long ", e);
        }
        return null;
    }
}