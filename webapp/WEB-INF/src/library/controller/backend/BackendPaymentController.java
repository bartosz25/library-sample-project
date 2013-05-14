package library.controller.backend;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import library.controller.MainController;
import library.form.BackendPenaltyForm;
import library.model.entity.Payment;
import library.model.entity.Penalty;
import library.model.entity.Subscriber;
import library.model.entity.Subscription;
import library.model.repository.PaymentMethodRepository;
import library.model.repository.PenaltyRepository;
import library.model.repository.SubscriberRepository;
import library.model.repository.SubscriptionRepository;
import library.security.CSRFProtector;
import library.service.PenaltyService;
import library.service.SubscriptionService;
import library.validator.form.BackendPenaltyFormValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Même si l'utilisateur a choisi un mode de payement donné, il peut toujours faire parvenir
 * le montant via un autre blais. Alors on met à jour le champ paymentMethod dans subscription,
 * jusque là jouant le rôle indicatif.
 */
@RequestMapping("/backend/payment")
@Controller
public class BackendPaymentController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendPaymentController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.payment";
    private final String addBindingPenalty = "org.springframework.validation.BindingResult.backendPenaltyForm";
    private final static String TYPE_SUB = "subscription";
    private final static String TYPE_PENALTY = "penalty";
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PAYMENT_ACCEPT')")
    @RequestMapping(value = "/accept/subscription/{idSub}", method = RequestMethod.GET)
    public String acceptPayment(@ModelAttribute("payment") Payment payment, @PathVariable long idSub, 
    Model layout) {
        logger.info("Received POST request " + payment);
        logger.info("==========> looking for subscription " + idSub);
        Subscription subscription = subscriptionRepository.findOne(idSub);
        if (subscription.getPayment() != null) {
            layout.addAttribute("cantAccept", true);
        }
        payment.setPaymentMethod(subscription.getPaymentMethod());
        layout.addAttribute("subscription", subscription);
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        payment.setPaymentMethods(paymentMethodRepository.getAllMethods());
        return "acceptPayment";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PAYMENT_ACCEPT')")
    @RequestMapping(value = "/accept/subscription/{idSub}", method = RequestMethod.POST)    
    public String acceptPaymentHandle(@ModelAttribute("payment") @Valid Payment payment, BindingResult binRes, 
    @PathVariable long idSub, Model layout, RedirectAttributes redAtt) {
        logger.info("Received POST request " + payment);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("payment", payment);
            redAtt.addFlashAttribute("errors", binRes);
            logger.info("Errors found  " + binRes);
        } else {
            try {
                payment.setDate(new Date());
                payment.setType(payment.getSubscriptionType());
                Subscription subscription = subscriptionRepository.findOne(idSub);
                subscription.setPaymentMethod(payment.getPaymentMethod());
                subscriptionService.addPayment(subscription, payment);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                binRes.addError(getExceptionError("payment"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("payment", payment);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/backend/payment/accept/subscription/"+idSub;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PAYMENT_ACCEPT')")
    @RequestMapping(value = "/accept/penalty/{idSub}", method = RequestMethod.GET)
    public String acceptPenaltyPayment(@ModelAttribute("backendPenaltyForm") BackendPenaltyForm backendPenaltyForm, @PathVariable long idSub, Model layout) {
        logger.info("Received POST request " + backendPenaltyForm);
        logger.info("==========> looking for subscriber " + idSub);
        Subscriber subscriber = subscriberRepository.findOne(idSub);
        backendPenaltyForm.setPenalties(penaltyRepository.getPenaltiesBySubscriber(subscriber.getId(), Penalty.STATE_PAYED));
        backendPenaltyForm.setModes(paymentMethodRepository.getAllMethods());
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBindingPenalty, layoutMap.get("errors"));
        }
        return "acceptPenaltyPayment";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'PAYMENT_ACCEPT')")
    @RequestMapping(value = "/accept/penalty/{idSub}", method = RequestMethod.POST)    
    public String acceptPenaltyPaymentHandle(@ModelAttribute("backendPenaltyForm") @Valid BackendPenaltyForm backendPenaltyForm, BindingResult binRes, @PathVariable long idSub, Model layout, RedirectAttributes redAtt,
    HttpServletRequest request) {
        backendPenaltyForm.setSubscriber(subscriberRepository.findOne(idSub));
        logger.info("Received POST request " + backendPenaltyForm);
        DataBinder binder = new DataBinder(backendPenaltyForm);
        binder.setValidator(new BackendPenaltyFormValidator(csrfProtector, request));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("backendPenaltyForm", backendPenaltyForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("Errors found  " + results);
        } else {
            try {
                double amount = 0;
                for (Penalty penalty : backendPenaltyForm.getPenaltiesChecked()) {
                    amount = amount + penalty.getAmount();
                }
                Payment payment = new Payment();
                payment.setReference(backendPenaltyForm.getReference());
                payment.setType(payment.getPenaltyType());
                payment.setAmount(amount);
                payment.setPaymentMethod(paymentMethodRepository.findOne(backendPenaltyForm.getModeChecked()));
                payment.setDate(new Date());
                penaltyService.addPayment(backendPenaltyForm.getPenaltiesChecked(), payment);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("backendPenaltyForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("backendPenaltyForm", backendPenaltyForm);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/backend/payment/accept/penalty/"+idSub;
    }
}