package library.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import library.model.entity.PaymentMethod;
import library.model.entity.Subscriber;
import library.model.repository.PaymentMethodRepository;
import library.service.BorrowingService;
import library.service.PaymentMethodService;
import library.service.SubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service("paymentMethodService")
public class PaymentMethodServiceImpl implements PaymentMethodService {
    final Logger logger = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private BorrowingService borrowingService;
    @Autowired
    private SubscriberService subscriberService;
    
    /**
     * Gets available payment methods for subscriber. Some criterion are added to get request :
     * - check quantity of borrowed books. 
     * - calcul the sum of payed subscriptions and minus from them penalties amount
     * - get subscriber activity (newsletter subscription, suggestions, borrowings, questions,
     *   chat presence)
     * Every payment method has a field (validity_pm). It's used to match informations about 
     * subscriber with payment method access criterion. The matching is accomplished thanks
     * to SpEL.
     */
    @Override
    // @Cacheable(value = "books", key = "#subscriber.id")
    public List<PaymentMethod> getAllMethodsBySubscriber(Subscriber subscriber) {
        List<PaymentMethod> result = new ArrayList<PaymentMethod>();
        // get all PaymentMethod
        List<PaymentMethod> paymentMethods = paymentMethodRepository.getAllMethods();
        logger.info("=> paymentMethods : " + paymentMethods);
        logger.info("=> paymentMethods.size() : " + paymentMethods.size());
        
        // construct subscriber criterion data
        long borrowed = borrowingService.countBorrowedBySubscriber(subscriber, null, null);
        BigDecimal spentMoney = subscriberService.getSpentMoney(subscriber, true);
        // logger.info("spentMoney is " + spentMoney);
        int activityPoints = subscriberService.getActivityPoints(subscriber, spentMoney);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("borrowed", borrowed);
        context.setVariable("spentMoney", spentMoney);
        context.setVariable("activityPoints", activityPoints);
        ExpressionParser parser = new SpelExpressionParser();
        // PaymentMethod paymentMethod;
        // String criterion = "#borrowed > 1 and #spentMoney > 10 and #activityPoints > 3";
        Iterator<PaymentMethod> methodsIterator = paymentMethods.iterator();
        while (methodsIterator.hasNext()) {
        // for(int i = 0; i < initialSize; i++)
        // {
            // logger.info("i is " + i);
            // PaymentMethod paymentMethod = paymentMethods.get(i);
            PaymentMethod paymentMethod = (PaymentMethod) methodsIterator.next();
            // logger.info("Got " + paymentMethod);
            // logger.info("Result after parsing " + parser.parseExpression(paymentMethod.getValidity()).getValue(context));
            // TODO : implement real case
            if (!parser.parseExpression(paymentMethod.getValidity()).getValue(context, Boolean.class) == true) {
                // logger.info("Removing : " + paymentMethod);
                // result.add(paymentMethod);
                // paymentMethods.remove(i);
                methodsIterator.remove();
            }
        }
        // logger.info("Payment methods after validation :"+paymentMethods);
        return paymentMethods;
    }
}