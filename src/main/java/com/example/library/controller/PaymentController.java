package com.example.library.controller;

// import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Subscription;
import com.example.library.model.repository.SubscriptionRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.Cryptograph;
import com.example.library.service.PenaltyService;
import com.example.library.tools.UtilitaryTool;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO : 
 * - gérer la propriété webappUrl dynamiquement, au moment du déploiement de l'application et le placer dynamiquement
 * - faire une méthode de notification
 * - transférer le template PayPal dans un autre répertoire
 */
@RequestMapping("/payment")
@Controller
public class PaymentController extends MainController {
    final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private ConversionService conversionService;
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired
    private VelocityEngine velocityEngine;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/confirmation/{reference}", method = RequestMethod.GET)
    public String confirmPayment(@PathVariable String reference, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {    
        long id = 0;
        try {
            id = Long.parseLong(cryptograph.decrypt(reference));
            logger.info("Looking for subscription with " + id + " decrypted from " + reference);
        } catch (NumberFormatException e) {
            logger.error("NumberFormatException catched on converting id", e);
        }
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        Subscription subscription = subscriptionRepository.findByIdAndSubscriber(id, subscriber);
        logger.info("Found subscription informations " + subscription);
        Object paymentTpl = "";
        try {
            HashMap<String, Object> configMap = new HashMap<String, Object>();
            configMap.put("order", reference);
            configMap.put("orderData", subscription);
            configMap.put("amount", subscription.getAmount());
            configMap.put("engine", velocityEngine);
            configMap.put("baseUrl", UtilitaryTool.getBaseUrl(request));
            Class beanClass = Class.forName(subscription.getPaymentMethod().getClassName());
            Object beanInstance = beanClass.newInstance();
            MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
            configMap.put("returnUrl", MethodUtils.invokeMethod(beanInstance, "getUrlOrderOk", null));
            configMap.put("returnCancelUrl", MethodUtils.invokeMethod(beanInstance, "getUrlOrderCancel", null));
            configMap.put("notifyUrl", MethodUtils.invokeMethod(beanInstance, "getUrlOrderNotification", null));
            MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
            paymentTpl = MethodUtils.invokeMethod(beanInstance, "renderPaymentTemplate", null);
            logger.info("Generated payment template " + paymentTpl);
        } catch (Exception e) {
            logger.error("An exception occured on generating payment template", e);
        }
        layout.addAttribute("subscription", subscription);
        layout.addAttribute("paymentTpl", (String)paymentTpl);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "confirmPayment";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/penalty/confirmation/{reference}", method = RequestMethod.GET)
    public String confirmPaymentPenalty(@PathVariable String reference, 
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
        Object paymentTpl = "";
        try {
            HashMap<String, Object> configMap = new HashMap<String, Object>();
            configMap.put("order", reference);
            configMap.put("orderData", penalties);
            configMap.put("amount", total);
            configMap.put("engine", velocityEngine);
            configMap.put("baseUrl", UtilitaryTool.getBaseUrl(request));
            Class beanClass = Class.forName(penalties.get(0).getPaymentMethod().getClassName());
            Object beanInstance = beanClass.newInstance();
            MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
            configMap.put("returnUrl", MethodUtils.invokeMethod(beanInstance, "getUrlPenaltyOk", null));
            configMap.put("returnCancelUrl", MethodUtils.invokeMethod(beanInstance, "getUrlPenaltyCancel", null));
            configMap.put("notifyUrl", MethodUtils.invokeMethod(beanInstance, "getUrlPenaltyNotification", null));
            MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
            paymentTpl = MethodUtils.invokeMethod(beanInstance, "renderPaymentTemplate", null);
            logger.info("Generated payment template " + paymentTpl);
        } catch (Exception e) {
            logger.error("An exception occured on generating payment template", e);
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        layout.addAttribute("paymentTpl", (String)paymentTpl);
        return "confirmPayment";
    }
}