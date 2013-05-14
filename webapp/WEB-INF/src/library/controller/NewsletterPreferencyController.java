package library.controller;

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
import java.util.HashMap;
import java.util.Map;

import library.annotation.LocaleLang;
import library.annotation.LoggedUser;
import library.form.NewsPreferenciesCredentialsForm;
import library.model.entity.Lang;
import library.model.entity.NewsletterSubscriber;
import library.model.entity.Subscriber;
import library.model.repository.NewsletterSubscriberRepository;
import library.security.AuthenticationFrontendUserDetails;
import library.security.SaltCellar;
import library.service.NewsletterPreferencyService;
import library.service.NewsletterSubscriberService;
import library.validator.form.NewsletterPreferenciesValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO : voir pour le CSRF
/**
 * TODOS : 
 * - édition du mot de passe (=> reste à implémenter : uniquement non connecté)
 */
@RequestMapping("/newsletter-preferencies")
@Controller
public class NewsletterPreferencyController extends MainController {
    final Logger logger = LoggerFactory.getLogger(NewsletterPreferencyController.class);
    private final String editBinding = "org.springframework.validation.BindingResult.newsPreferenciesCredentialsForm";
    @Autowired
    private NewsletterSubscriberService newsletterSubscriberService;
    @Autowired
    private NewsletterPreferencyService newsletterPreferencyService;
    @Autowired
    private NewsletterSubscriberRepository newsletterSubscriberRepository;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    @Autowired
    private SaltCellar saltCellar;

// @RequestMapping(method = RequestMethod.GET)
// public void index(Model layout, @LocaleLang Lang lang) {
    // logger.info("=========> index() method called");
// }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modify(@LoggedUser AuthenticationFrontendUserDetails user, Model layout,
    @LocaleLang Lang lang) {
        if (user != null) {
            Subscriber subscriber = conversionService.convert(user, Subscriber.class);
            layout.addAttribute("email", subscriber.getEmail());
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "newsPreferenciesModify";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyHandle(@ModelAttribute("newsPreferenciesCredentialsForm") 
                  NewsPreferenciesCredentialsForm newsPreferenciesCredentialsForm, 
                  @LoggedUser AuthenticationFrontendUserDetails user) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Subscriber subscriber = null;
        if (user != null) {
            subscriber = conversionService.convert(user, Subscriber.class);
        }
        logger.info("==========> Received " + newsPreferenciesCredentialsForm);
        // make some validation before
        DataBinder binder = new DataBinder(newsPreferenciesCredentialsForm);
        binder.setValidator(new NewsletterPreferenciesValidator(subscriber, saltCellar, passwordEncoder, newsletterSubscriberRepository));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After BookForm validation " + results);
        boolean edited = false;
        if (results.hasErrors()) {
            StringBuffer msg = new StringBuffer();
            for (ObjectError error : results.getAllErrors()) {
                msg.append(error.getCode());
                msg.append(error.getDefaultMessage()); // this line is temporary
            }
            jsonMap.put("msg", msg.toString());
            logger.info("errors found = " + newsPreferenciesCredentialsForm);
        } else {
            edited = newsletterSubscriberService.modifySubscription(newsPreferenciesCredentialsForm, subscriber);
        }
        jsonMap.put("edited", edited);
        return jsonMap;
    }
    
    @RequestMapping(value = "/get-values-form", method = RequestMethod.POST)
    public String getValues(@ModelAttribute("newsPreferenciesCredentialsForm") 
    NewsPreferenciesCredentialsForm newsPreferenciesCredentialsForm, @RequestParam("email") String email, 
    @RequestParam("password") String password, @LoggedUser AuthenticationFrontendUserDetails user, Model layout) {
        NewsletterSubscriber newsletterSubscriber = null;
        if (user == null) {
            newsletterSubscriber = newsletterSubscriberService.getByMailAndPlainTextPassword(email, password); 
        } else {
            Subscriber subscriber = conversionService.convert(user, Subscriber.class);
            newsletterSubscriber = newsletterSubscriberRepository.getByMailAndEncodedPassword(email, subscriber.getPassword(), NewsletterSubscriber.STATE_CONFIRMED);
        }
        boolean wasFound = false;
        if (newsletterSubscriber != null) {
            wasFound = true;
            newsPreferenciesCredentialsForm = newsletterPreferencyService.createPreferenciesModifyForm(newsletterSubscriber);
            newsPreferenciesCredentialsForm.setCredEmail(email);
            newsPreferenciesCredentialsForm.setCredPassword(password);
        }
        layout.addAttribute("wasFound", wasFound);
        layout.addAttribute("newsPreferenciesCredentialsForm", newsPreferenciesCredentialsForm);
        return "newsGetValues";
    }
}