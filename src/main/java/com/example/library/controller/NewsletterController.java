package com.example.library.controller;

/**
 * Newsletter has some fonctionnalities : 
 * - user can subscribe to newsletter with an 3-step form, based on Spring web-flow plugin. At 
 *   the 1st step, the user puts his e-mail address. At the second time, he specifies his 
 *   reading
 *   preferencies (as favorite book categories, favorite book writers etc.). At the end, he 
 *   consults the summary of his choices. At this moment, he can validate or correct it.
 * - every newsletter subscriber can modify his preferencies with the password definied at the
 *   first step of registration process
 * - at every moment, an user can unsubscribe from the newsletter (state = 2)
 * - if a logged user subscribes to a newsletter, we don't demand him to specify his e-mail 
 *   address and password. 
 *   They will be taken directly from subscriber table at every request.
 * - NewsletterController contains a sample URL to check if a newsletter was opened (1x1 px image loading)
 */
import java.util.Map;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.NewsletterSubscriberRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.Cryptograph;
import com.example.library.security.SaltCellar;
import com.example.library.validator.form.NewsletterUnsubscribeValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// TODO : voir pour le token CSRF
@RequestMapping("/newsletter")
@Controller
public class NewsletterController extends MainController {
    final Logger logger = LoggerFactory.getLogger(NewsletterController.class);
    private final String removeBinding = "org.springframework.validation.BindingResult.newsletterSubscriber";
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired 
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    @Autowired
    private SaltCellar saltCellar;
    @Autowired
    private ConversionService conversionService;

    @RequestMapping(value = "/confirm/{code}", method = RequestMethod.GET)
    public String newsletterConfirm(@PathVariable String code, Model layout, @LocaleLang Lang lang) {
        logger.info("============> Entered to NewsletterController.credentials");
        NewsletterSubscriber newsletterSubscriber = null;
        long id;
        try {
            id = Long.parseLong(cryptograph.decrypt(code));
            newsletterSubscriber = newsletterSubscriberRepository.findOne(id);
        } catch (Exception e) {
            logger.error("An exception occured on parsing String ("+code+")", e);
        }
        boolean result = false;
        if (newsletterSubscriber != null && newsletterSubscriber.getState() == NewsletterSubscriber.STATE_NOT_CONFIRMED) {
            newsletterSubscriber.setState(NewsletterSubscriber.STATE_CONFIRMED);
            newsletterSubscriberRepository.save(newsletterSubscriber);
            result = true;
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        layout.addAttribute("result", result);
        return "newsletterConfirm";
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
    public String newsletterUnsubscribe(@ModelAttribute("newsletterSubscriber") NewsletterSubscriber newsletterSubscriber, Model layout, @LoggedUser AuthenticationFrontendUserDetails user, 
    @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(removeBinding, layoutMap.get("errors"));
        }
        // si user != null, préremplir le champ e-mail du formulaire
        if (user != null && newsletterSubscriber.getEmail() == null && !layoutMap.containsKey("errors")) {
            Subscriber subscriber = conversionService.convert(user, Subscriber.class);
            newsletterSubscriber.setEmail(subscriber.getEmail());
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "newsletterUnsubscribe";
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public String newsletterUnsubscribeHandle(@ModelAttribute("subscriber") 
    NewsletterSubscriber newsletterSubscriber, BindingResult binRes,  RedirectAttributes redAtt, Model layout, 
    @LoggedUser AuthenticationFrontendUserDetails user) {
        Subscriber subscriber = null;
        if (user != null) {
            subscriber = conversionService.convert(user, Subscriber.class);
        }
        // make some validation before
        DataBinder binder = new DataBinder(newsletterSubscriber);
        binder.setValidator(new NewsletterUnsubscribeValidator(subscriber, saltCellar, passwordEncoder, newsletterSubscriberRepository));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After BookForm validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("newsletterSubscriber", newsletterSubscriber);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + newsletterSubscriber);
        } else {
            try {
                // will never be null - validation caughts the error when user is not found
                newsletterSubscriber = newsletterSubscriberRepository.getByMail(newsletterSubscriber.getEmail());
                newsletterSubscriber.setState(NewsletterSubscriber.STATE_DELETED);
                newsletterSubscriberRepository.save(newsletterSubscriber);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("subscriber"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscriber", subscriber);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/newsletter/unsubscribe";
    }

    @RequestMapping(value = "/image/{imageName}", method = RequestMethod.GET)
    public void wasOpened(@PathVariable String imageName) {
        // imageName = newsletter's id obfuscated
    }
}