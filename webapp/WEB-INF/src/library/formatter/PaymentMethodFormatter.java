package library.formatter;

import java.text.ParseException;
import java.util.Locale;

import library.model.entity.PaymentMethod;
import library.model.repository.PaymentMethodRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

public class PaymentMethodFormatter implements Formatter<PaymentMethod> {
    final Logger logger = LoggerFactory.getLogger(PaymentMethodFormatter.class);
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    public String print(PaymentMethod paymentMethod, Locale locale) {
        logger.info("formatting paymentMethod " + paymentMethod);
        return (paymentMethod != null ? ""+paymentMethod.getId() : "0") ;
    }

    public PaymentMethod parse(String text, Locale locale) throws ParseException {
        logger.info("formatting to paymentMethod " + text);
        long id = 0l;
        PaymentMethod paymentMethod = null;
        try {
            id = Long.parseLong(text.trim());
            paymentMethod = paymentMethodRepository.findOne(id);
            logger.info("Formatted to paymentMethod " + paymentMethod);
        } catch(NumberFormatException e) {
            logger.info("An error occured on formatting " + text + " to long", e);
        }
        return paymentMethod;
    }
}