package com.example.library.controller;


/**
 * Pour effectuer un paiement :
 * - le client doit être authentifié
 * - il ne peut pas avoir une pénalité non réglée
 * - après le client choisit le mode d'abonnement (7j/14j/1m/6m/12m)
 * - on détermine le prix hebdomadaire; plus la durée d'abonnement monte, plus de réduction est grande (5% par durée, à partir du 14j)
 * - le client doit choisir le mode de paiement (virement, paypal, cb [SystemPay par exemple])
 * Si le paiement par virement, on affiche le message avec les données de compte à créditer.
 * Si le paiement par PayPal, on peut déterminer si le paiement est accepté en analysant le lien de retour.
 * Si le paiement par CB et notification en différé, on affiche le message que le paiement a bien été enregistré. S'il est accepté par la banque, l'abonnement sera automatiquement activé.
 * Dans tous les cas, si un problème de paiement apparaît, avertir le système par mail.
 * L'abonnement ne peut commencer que le lendemain de l'enregistrement de la demande.
 */
/**
 * Pour transférer un abonnement : 
 * - l'utilisateur choisit le client à qui il veut transférer ses droits
 * - la condition sine qua non est que l'utilisateur n'ait aucun livre en emprunt
 * - la condition suivante est que le réceveur n'ait pas d'abonnement actif sur la période donnée; s'il a déjà un abonnement pour cette période et que l'abonnement donné y est supérieur (plus long), on ne rajoute que la durée supplémentaire d'abonnement.
 * - le transfert s'effectue avec accord du réceveur. Il est notifié par mail qu'une demande de transfert a été faite. Il peut alors soit l'accepter, soit la refuser.Jusqu'à sa réaction, l'abonnement reste chez le donnateur. Le donnateur reçoit le résultat de cette réaction par mail. 
 * Si un utilisateur se connecte, on vérifie automatiquement s'il a des transfers en attente de validation.
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.form.SubscribeForm;
import com.example.library.form.TransferForm;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Payment;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Subscription;
import com.example.library.model.entity.SubscriptionTransfer;
import com.example.library.model.repository.PaymentMethodRepository;
import com.example.library.model.repository.SubscriptionRepository;
import com.example.library.model.repository.SubscriptionTransferRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.security.Cryptograph;
import com.example.library.service.PaymentMethodService;
import com.example.library.service.SubscriptionService;
import com.example.library.service.SubscriptionTransferService;
import com.example.library.service.payment.Paypal;
import com.example.library.validator.form.SubscribeFormValidator;
import com.example.library.validator.form.TransferFormValidator;
import net.sf.ehcache.Ehcache;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
 * TODO : 
 * - vérifier si l'utilisateur n'a aucun autre abonnement pour la période choisie. Si c'est le cas, ne pas enregistrer son nouvel abonnement et l'avertir de ce fait. La seule solution dans ce cas, est le prolongement de l'abonnement déjà en sa posession, ou bien la diminution de l'abonnement qu'il souhaite commander.
 */
@RequestMapping("/subscription")
@Controller
public class SubscriptionController extends MainController {
    final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.subscribeForm";
    private final String transferBinding = "org.springframework.validation.BindingResult.transferForm";
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionTransferService subscriptionTransferService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionTransferRepository subscriptionTransferRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private ConversionService conversionService;
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired 
    private CSRFProtector csrfProtector;
    @Autowired
    private CacheManager cacheManager;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public String subscribe(@ModelAttribute("subscribeForm") SubscribeForm subscribeForm, @LoggedUser 
    AuthenticationFrontendUserDetails user, Model layout, @LocaleLang Lang lang) {
        if (cacheManager.getCache("books") != null) {
            Ehcache map = (Ehcache)cacheManager.getCache("books").getNativeCache();
            logger.info("=> Map " + map.get(1));
            logger.info("=> Map size " + map.getSize());
            logger.info("=> Map name " + map.getName());
            logger.info("=> Map keys " + map.getKeys());
        }
        // for test purposes only, check if cache was correctly written
        logger.info("====> Caches " + cacheManager.getCacheNames());
        logger.info("====> Cache " + cacheManager.getCache("books"));
        logger.info("====> subscribeForm " + subscribeForm);
        if (subscribeForm.getModes() == null || subscribeForm.getModes().size() == 0) {
            subscribeForm.setModes(paymentMethodService.getAllMethodsBySubscriber(conversionService.convert(user, Subscriber.class)));
            subscribeForm.setTypes(Subscription.getTypes());
        }

        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "subscribe";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public String subscribeHandle(@ModelAttribute("subscribeForm") SubscribeForm subscribeForm, @LoggedUser 
    AuthenticationFrontendUserDetails user, RedirectAttributes redAtt) {
        subscribeForm.setTypes(Subscription.getTypes());
        subscribeForm.setModes(paymentMethodService.getAllMethodsBySubscriber(conversionService.convert(user, Subscriber.class)));
        subscribeForm.setSubscriber(conversionService.convert(user, Subscriber.class));
        DataBinder binder = new DataBinder(subscribeForm);
        binder.setValidator(new SubscribeFormValidator(paymentMethodRepository));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After subscription validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("subscribeForm", subscribeForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + subscribeForm);
        } else {
            Object redirectUrl = null;
            try {
                Subscription subscription = subscriptionService.saveSubscribeNotValid(subscribeForm);
                String cryptedId = cryptograph.encrypt(Long.toString(subscription.getId()));
                redirectUrl = "/subscription/confirmation/"+cryptedId;
                // we want to show how Apache Commons Beans works
                // it's the reason why we break here KISS principle
                HashMap<String, Object> configMap = new HashMap<String, Object>();
                configMap.put("order", cryptedId);
                Class beanClass = Class.forName(subscription.getPaymentMethod().getClassName());
                Object beanInstance = beanClass.newInstance();
                MethodUtils.invokeMethod(beanInstance, "setUrlConfig", configMap);
                redirectUrl = MethodUtils.invokeMethod(beanInstance, "getUrlOrderAfter", null);
                logger.info("Redirecting with " + redirectUrl);
            } catch (Exception e) {
                logger.error("An exception occured on getting redirect URL", e);
                results.addError(getExceptionError("subscribeForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscribeForm", subscribeForm);
                redAtt.addFlashAttribute("errors", results);
                redirectUrl = "/subscription/subscribe";
            }
            return "redirect:"+(String) redirectUrl;
        }
        return "redirect:/subscription/subscribe";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/confirmation/{reference}", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmSubscribe(@PathVariable String reference, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        long id = 0;
        try {
            id = Long.parseLong(cryptograph.decrypt(reference));
            logger.info("Looking for subscription with " + id + " decrypted from " + reference);
        } catch (NumberFormatException e) {
            logger.error("NumberFormatException catched on converting id", e);
        }
        // Payment payment = new Payment();
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        Subscription subscription = subscriptionRepository.findByIdAndSubscriber(id, subscriber);
        logger.info("Found subscription informations " + subscription);
        layout.addAttribute("subscription", subscription);
        // check if POST data received (PayPal informations)
        if (request.getParameter("mc_gross") != null && subscription.getPaymentMethod().getCode().equals("paypal") && subscription.getPayment() == null && request.getParameter("payment_status") != null && request.getParameter("payment_status").equals("Completed")) {
            try {
                Paypal paypal = new Paypal();
                Payment payment = paypal.getPaymentInst(subscription, request);
                if (payment != null) {
                    subscription = subscriptionService.addPayment(subscription, payment);
                    return "redirect:/subscription/confirmation/"+reference;
                }
            } catch (Exception e) {
                logger.info("An exception occured on transforming payment data", e);
            }
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        // layout.addAttribute("paymentTpl", subscription.getPaymentMethod().);
        layout.addAttribute("subscription", subscription);
        return "confirmSubscribe";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public String transfer(@ModelAttribute("transferForm") TransferForm transferForm, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, @LocaleLang Lang lang) {
        transferForm.setSubscriber(conversionService.convert(user, Subscriber.class));
        transferForm.setSubscriptions(subscriptionRepository.getActifBySubscriber(transferForm.getSubscriber(), new Date()));
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(transferBinding, layoutMap.get("errors"));
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "transferSubscribe";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transferHandle(@ModelAttribute("transferForm") TransferForm transferForm, 
    @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt) {
        transferForm.setSubscriber(conversionService.convert(user, Subscriber.class));
        DataBinder binder = new DataBinder(transferForm);
        binder.setValidator(new TransferFormValidator(subscriptionRepository, subscriptionService, subscriptionTransferRepository));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After subscription validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("transferForm", transferForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + transferForm);
        } else {
            try {
                SubscriptionTransfer transfer = subscriptionTransferService.transferToUser(transferForm);
            } catch (Exception e) {
                results.addError(getExceptionError("transferForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("transferForm", transferForm);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/subscription/transfer";
    }
    // TODO : Si accepte de recevoir l'abonemment et que la période chevauche, on ne rajoute que les jours supplémentaires
	// TODO : si l'on rajoute des jours, on désactive automatiquement l'abonemment du donneur
	// TODO : si entre-temps, donneur emprunte des livres et ne les retourne pas, le receveur ne peut pas accepter l'abonnement. Un mail d'alerte est alors envoyé au donneur.
    // TODO : donneur peut offrir son abonnement à plusieurs personnes. La première personne qui accepte le don, l'acquiert. Il faut alors vérifier si l'abonnement offert appartient toujours au donneur d'origine.
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/transfer/{decision}/{fromId}/{subscriptionId}", method = RequestMethod.GET)
    public String transferDecision(@PathVariable String decision, @PathVariable long fromId, 
    @PathVariable long subscriptionId, @LoggedUser AuthenticationFrontendUserDetails user, Model layout,
    @LocaleLang Lang lang) {
        // vérifier si appartient
        // temporairement on utilise l'objet Subscription
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
        Subscription subscription = subscriptionRepository.findOne(subscriptionId);
        SubscriptionTransfer subscriptionTransfer = subscriptionTransferRepository.getByReceiverAndSubscription(fromId, subscriber.getId(), subscription, SubscriptionTransfer.STATE_PENDING);
        logger.info("Found subscriptionTransfer =========> " + subscriptionTransfer);
        logger.info("Found subscriber =========> " + subscriber);
        logger.info("Found subscription =========> " + subscription);
        if (subscriptionTransfer == null) {
            layout.addAttribute("notAllowed", true);
        } else {
            if (decision.equals("deny")) {
                subscriptionTransfer.setState(SubscriptionTransfer.STATE_DENIED);
                subscriptionTransferRepository.save(subscriptionTransfer);
            } else if (decision.equals("accept")) {
                subscription = subscriptionService.transferSubscription(subscriptionTransfer);
            }
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "transferDecision";
    }
}