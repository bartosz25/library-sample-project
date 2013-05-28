package com.example.library.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.form.PenaltyForm;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Payment;
import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.PaymentMethodRepository;
import com.example.library.model.repository.PenaltyRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.security.Cryptograph;
import com.example.library.service.PenaltyService;
import com.example.library.service.payment.Paypal;
import com.example.library.validator.form.PenaltyFormValidator;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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
 * Détection des pénalités se fait par un module tournant en background. Chaque fois où, malgré
 * X relances, l'utilisateur ne retourne pas le livre, il prend une pénalité. Elle est réglable
 * dans ce contrôleur, via les méthodes de paiement immédiat : PayPal + CB. 
 * Il faut définir des outils communs pour PayPal et CB.
 * Le module background remplit automatiquement le libellé de la pénalité (about_pe). Normalement,
 * le remplissage va consister à concaténer les titres du livre et à rajoute entre parenthèses les dates de retours prévus.
 * Si un utilisateur a plusieurs penalités à payer, il peut les régler d'un seul coup.
 */
/**
 * TODOS : 
 * - accepter /penalty/pay et penalty/pay/3 comme URL (et si 3 est spécifié, précocher par défaut la penalité 3)
 */
@RequestMapping("/penalty")
@Controller
public class PenaltyController extends MainController {
    final Logger logger = LoggerFactory.getLogger(PenaltyController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.penaltyForm";
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired
    private PenaltyRepository penaltyRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String penaltyPay(@ModelAttribute("penaltyForm") PenaltyForm penaltyForm, 
    @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt, Model layout, HttpServletRequest request, @LocaleLang Lang lang) {
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        penaltyForm.setPenalties(penaltyRepository.getPenaltiesBySubscriber(subscriber.getId(), Penalty.STATE_PAYED));
        penaltyForm.setModes(paymentMethodRepository.getAllMethods());
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("penalty-pay");
            penaltyForm.setToken(csrfProtector.constructToken(request.getSession()));
            penaltyForm.setAction(csrfProtector.getIntention());
            logger.info("Generated token " + penaltyForm.getToken());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            return "redirect:/generalError";
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "penaltyPay";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String penaltyPayHandle(@ModelAttribute("penaltyForm") PenaltyForm penaltyForm, 
    @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt, HttpServletRequest request) {
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        penaltyForm.setPenalties(penaltyRepository.getPenaltiesBySubscriber(subscriber.getId(), Penalty.STATE_PAYED));
        penaltyForm.setModes(paymentMethodRepository.getAllMethods());
        penaltyForm.setSubscriber(subscriber);
        DataBinder binder = new DataBinder(penaltyForm);
        binder.setValidator(new PenaltyFormValidator(csrfProtector, request));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After subscription validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("penaltyForm", penaltyForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + penaltyForm);
        } else {
            Object redirectUrl = null;
            try {
                List<Penalty> penalties = penaltyService.saveNotValid(penaltyForm);
                String cryptedId = cryptograph.encrypt(penaltyForm.getCheckedInString());
                redirectUrl = "/penalty/confirmation/"+cryptedId;
                // we want to show how Apache Commons Beans works
                // it's the reason why we break here KISS principle
                HashMap<String, Object> configMap = new HashMap<String, Object>();
                configMap.put("order", cryptedId);
                Class beanClass = Class.forName(penalties.get(0).getPaymentMethod().getClassName());
                Object beanInstance = beanClass.newInstance();
                MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
                redirectUrl = MethodUtils.invokeMethod(beanInstance, "getUrlPenaltyAfter", null);
                logger.info("Redirecting with " + redirectUrl);
            } catch (Exception e) {
                results.addError(getExceptionError("penaltyForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("penaltyForm", penaltyForm);
                redAtt.addFlashAttribute("errors", results);
                redirectUrl = "/penalty/pay";
            }
            return "redirect:"+(String) redirectUrl;
        }
        return "redirect:/penalty/pay";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/confirmation/{reference}", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmSubscribe(@PathVariable String reference, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        List<Penalty> penalties = new ArrayList<Penalty>();
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        double total = 0;
        String references = cryptograph.decrypt(reference);
        String[] ids = references.split(",");
        for (String idString : ids) {
            Penalty penalty = penaltyService.getPenaltyFromCode(idString, subscriber);
            if (penalty != null) {
                penalties.add(penalty);
                total = total + penalty.getAmount();
            }
        }
        layout.addAttribute("penalties", penalties);
        // check if POST data received (PayPal informations)
        if (request.getParameter("mc_gross") != null && penalties.get(0).getPaymentMethod().getCode().equals("paypal") && penalties.get(0).getPayment() == null && request.getParameter("payment_status") != null && request.getParameter("payment_status").equals("Completed")) {
            // check total amount
            try {
                double paypalAmount = Double.parseDouble(request.getParameter("mc_gross").trim());
                if (paypalAmount == total) {
                    Paypal paypal = new Paypal();
                    Payment payment = new Payment();
                    payment.setPaymentMethod(penalties.get(0).getPaymentMethod());
                    payment.setReference(request.getParameter("txn_id"));
                    payment.setDate(new Date());
                    payment.setType(payment.getPenaltyType());
                    payment.setAmount(paypalAmount);
                    penalties = penaltyService.addPayment(penalties, payment);
                    return "redirect:/penalty/confirmation/"+reference;
                }
            } catch (Exception e) {
                logger.error("Exception when trying to convert String to double ", e);
                return "errorPenalty";
            }
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "confirmPenalty";
    }
}