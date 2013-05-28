package com.example.library.controller.backend;

/**
 * Functionalities : 
 * - newsletter creation : criterion choices (through subscriber newsletter preferencies) and 
 *   sending time precision (for exemple tomorrow at 10 o'clock)
 * - newsletter edition/removing : only for the newsletter which is not actually sending (no 
 *   entries dans newsletter_send table, sending time lower than the actual one and state_ne 
 *   on '0'). The newsletter can be edited only by his creator.
 * - after saving, an scheduled task sends the newsletter to users. This sending is notified at
 *   the database (table newsletter, field state_ne on '1')
 */
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.controller.MainController;
import com.example.library.form.NewsletterWriteForm;
import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Newsletter;
import com.example.library.model.entity.NewsletterPreferencyCategoryLang;
import com.example.library.model.repository.NewsletterPreferencyCategoryLangRepository;
import com.example.library.model.repository.NewsletterRepository;
import com.example.library.security.AuthenticationUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.service.NewsletterPreferencyCategoryService;
import com.example.library.service.NewsletterPreferencyService;
import com.example.library.service.NewsletterService;
import com.example.library.service.NewsletterSubscriberService;
import com.example.library.validator.form.NewsletterWriteFormValidator;

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
 * TODOS : 
 * - suppression en cascade
 * - protection CSRF
 * - protection XSS
 * - implémenter JodaTime
 */
@RequestMapping("/backend/newsletter")
@Controller
public class BackendNewsletterController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BackendNewsletterController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.newsletterWriteForm";
    @Autowired
    private NewsletterSubscriberService newsletterSubscriberService;
    @Autowired
    private NewsletterPreferencyService newsletterPreferencyService;
    @Autowired
    private NewsletterPreferencyCategoryService newsletterPreferencyCategoryService;
    @Autowired
    private NewsletterPreferencyCategoryLangRepository newsletterPreferencyCategoryLangRepository;
    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private NewsletterService newsletterService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'NEWSLETTER_WRITE')")
    @RequestMapping(value = "/write/{idNewsletter}", method = RequestMethod.GET)
    public String write(@ModelAttribute("newsletterWriteForm") NewsletterWriteForm newsletterWriteForm, 
    Model layout, @PathVariable long idNewsletter, @LoggedUser AuthenticationUserDetails user, 
    @LocaleLang Lang lang, HttpServletRequest request) {
        Map<String, Object> layoutMap = layout.asMap();
        logger.info("=============> " + newsletterWriteForm);
        if (idNewsletter != 0) {
            Newsletter newsletter = newsletterRepository.getByIdAndAdmin(idNewsletter, conversionService.convert(user, Admin.class));
            if (newsletter == null) {
                // TODO : certainement il vaut mieux implémanter une méthode du BackendController ou quelque chose de ce type qui va gérer les erreurs "non autorisation d'accès"
                return "redirect:/backend/dashboard";
            }
            logger.info("=========> Found newsletter " + newsletter);
            if (!layoutMap.containsKey("errors")) {
                newsletterWriteForm.setTitle(newsletter.getTitle());
                newsletterWriteForm.setText(newsletter.getText());
                newsletterWriteForm.setStartTime(newsletter.getSendTime());
                newsletterWriteForm.setPreferencies(newsletter.getPreferenciesList());
            }
        }
        List<NewsletterPreferencyCategoryLang> prefCategories = newsletterPreferencyCategoryLangRepository.getByLang(lang.getId());
        newsletterWriteForm.setCategories(newsletterPreferencyService.getTranslatedCategories(prefCategories, lang, newsletterWriteForm.getPreferencies()));
        newsletterWriteForm.setTranslations(newsletterPreferencyService.getTranslations(prefCategories));
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("b-newsletter-write");
            newsletterWriteForm.setToken(csrfProtector.constructToken(request.getSession()));
            newsletterWriteForm.setAction(csrfProtector.getIntention());
            logger.info("Generated token " + newsletterWriteForm.getToken());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
        }
        return "newsletterWrite";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'NEWSLETTER_WRITE')")
    @RequestMapping(value = "/write/{idNewsletter}" ,method = RequestMethod.POST)
    public String writeHandle(@ModelAttribute("newsletterWriteForm") NewsletterWriteForm newsletterWriteForm, 
    Model layout, RedirectAttributes redAtt, @LoggedUser AuthenticationUserDetails user, 
    @PathVariable long idNewsletter, HttpServletRequest request) {
        logger.info("Received POST request " + newsletterWriteForm);
        DataBinder binder = new DataBinder(newsletterWriteForm);
        binder.setValidator(new NewsletterWriteFormValidator(newsletterSubscriberService, csrfProtector, request));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After BookForm validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("newsletterWriteForm", newsletterWriteForm);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + newsletterWriteForm);
        } else {
            try {
                Newsletter newsletter = null;
                if (idNewsletter != 0) {
                    newsletter = newsletterRepository.getByIdAndAdmin(idNewsletter, conversionService.convert(user, Admin.class));
                    if (newsletter == null) {
                    // TODO : certainement il vaut mieux implémanter une méthode du BackendController ou quelque chose de ce type qui va gérer les erreurs "non autorisation d'accès"
                        return "redirect:/backend/dashboard";
                    }
                }
                logger.info("No error found !");        
                newsletterService.addNewsletter(newsletterWriteForm, user, newsletter);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("newsletterWriteForm"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("newsletterWriteForm", newsletterWriteForm);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/backend/newsletter/write/"+idNewsletter;
    }
}