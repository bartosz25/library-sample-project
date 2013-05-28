package com.example.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Subscriber;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.security.Cryptograph;
import com.example.library.security.SaltCellar;
import com.example.library.service.SubscriberService;
import com.example.library.service.check.SubscriberAccountCheck;
import com.example.library.service.check.SubscriberAvatarCheck;
import com.example.library.service.check.SubscriberPasswordCheck;
import com.example.library.service.check.SubscriberRegisterCheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODOS : 
 * - redimensionner l'avatar
 * - convertir entre types (jpg => png, png => jpg etc.)
 * - renommer le fichier uploadé en subscriber.getLogin().png (=> fait partiellement, sans .png)
 * - gérer transparence dans PNG transférés
 * - tout extraire dans un Tool externe qui contiendra une map pour savoir la configuration de chaque image transférée (dans tools.xml)
 */
@Controller
public class SubscriberController extends MainController {
    final Logger logger = LoggerFactory.getLogger(SubscriberController.class);
    private final String registerBinding = "org.springframework.validation.BindingResult.subscriber";
    @Autowired
    private SubscriberService subscriberService;
    @Autowired 
    private Cryptograph cryptograph;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    @Autowired
    private SaltCellar saltCellar;
    @Autowired
    private CSRFProtector csrfProtector;
    @Autowired
    private ConversionService conversionService;

    @InitBinder
    public void bindForm(WebDataBinder binder) {
        binder.setDisallowedFields(new String[] {"id", "created", "confirmed", "blacklisted", "bookingNb", "revival"});
    }
    
    /**
     * Register form actions. GET serves to show the form.
     * POST is used to handle register request.
     */
    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(@ModelAttribute("subscriber") Subscriber subscriber, Model layout, 
    RedirectAttributes redAtt, @LocaleLang Lang lang, HttpServletRequest request) {
        logger.info("Found lang for register : " + lang);
        String template = "register";
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(registerBinding, layoutMap.get("errors"));
        } else if (layoutMap.containsKey("success")) {
            template = "registerSuccess";
        } else {
            try {
                csrfProtector.setIntention("register");
                subscriber.setToken(csrfProtector.constructToken(request.getSession()));
                subscriber.setAction(csrfProtector.getIntention());
                logger.info("Generated token " + subscriber.getToken());
            } catch (Exception e) {
                logger.error("An exception occured on creating CSRF token", e);
                return "redirect:/generalError";
            }
        }
        logger.info("-------------------------- Got Map : " + layout.asMap());
        layout.addAttribute("communs", getViewCommunMap(lang));
        return template;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public String registerHandle(@ModelAttribute("subscriber") @Validated({SubscriberRegisterCheck.class})
    Subscriber subscriber, BindingResult binRes, RedirectAttributes redAtt) {
        logger.info("Received POST request " + subscriber);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("subscriber", subscriber);
            redAtt.addFlashAttribute("errors", binRes);
/**
TODO : à tester une manière plus intelligente d'appliquer les erreurs  
@RequestMapping(value = "/submit", method = RequestMethod.POST)
public final String submit(@ModelAttribute("register") @Valid final Register register, final BindingResult binding, RedirectAttributes attr, HttpSession session) {

if (binding.hasErrors()) {
    attr.addFlashAttribute("org.springframework.validation.BindingResult.register", binding);
    attr.addFlashAttribute("register", register);
    return "redirect:/register/create";
}

return "redirect:/register/success";*/
        } else {
            try {
                subscriberService.save(subscriber);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                binRes.addError(getExceptionError("subscriber"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscriber", subscriber);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/register";
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register/confirm/{code}", method = RequestMethod.GET)
    public String confirmRegister(@PathVariable String code, Model layout, @LocaleLang Lang lang) {
        Subscriber subscriber =  null;
        boolean confirmedResult = false;
        // decrypt code and find the non-confirmed user
        long id;
        try {
            id = Long.parseLong(cryptograph.decrypt(code));
            logger.info("Looking for user with " + id + " decrypted from " + code);
            subscriber = subscriberService.findNonConfirmedById(id);
        } catch (NumberFormatException e) {
            logger.error("NumberFormatException catched on converting id", e);
        }

        if (subscriber != null) {
            if (!subscriber.ifConfirmed()) {
                subscriber.setConfirmed(Subscriber.IS_CONFIRMED);
                subscriberService.confirm(subscriber);
                confirmedResult = true;
            } else {
                layout.addAttribute("confirmedErrorMsg", messageSource.getMessage("register.confirmed.already", null, LocaleContextHolder.getLocale()));
            }
        } else {
            layout.addAttribute("confirmedErrorMsg", messageSource.getMessage("register.confirmed.notFound", null, LocaleContextHolder.getLocale())); 
        }
        layout.addAttribute("confirmed", confirmedResult);
// TODO : si une exception se produit, afficher une erreur et non pas un message de confirmation
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "registerConfirm";
    }

    /**
     * Account form actions.
     */
    // @PreAuthorize("(#subscriber.login == principal.username) or (#subscriber.login == #user.username) or (#subscriber.login == 'bartosz') or (#subscriber.getLogin() == principal.username)")
    // @PostAuthorize("(#subscriber.login == principal.username) or (#subscriber.login == #user.username)")
    // OK : @PreAuthorize("'bartosz' == principal.username")
    @PreAuthorize("hasRole('ROLE_USER')")
	// OK too : @PreAuthorize("hasAuthority('SUBSCRIBER_ADD')")
    @RequestMapping(value = "/account/modify", method = RequestMethod.GET)
    public String modifyAccount(@ModelAttribute("subscriber") Subscriber subscriber, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        // TODO : faire avec conversion
        subscriber = subscriberService.loadByUsername("bartosz");
        layout.addAttribute("subscriber", subscriber);
        logger.info("Got subscriber from database " + subscriber);
        logger.info("Found user from @LoggedUser annotation " + user);
        logger.info("Found user from @LoggedUser annotation : id " + user.getId());
        logger.info("After converting user to subscriber " + conversionService.convert(user, Subscriber.class));
        Map<String, Object> layoutMap = layout.asMap();
        if (!layoutMap.containsKey("errors")) {
            subscriber = conversionService.convert(user, Subscriber.class);
            try {
                csrfProtector.setIntention("modify-account");
                subscriber.setToken(csrfProtector.constructToken(request.getSession()));
                logger.info("Generated token " + subscriber.getToken());
                subscriber.setAction(csrfProtector.getIntention());
            } catch(Exception e) {
                logger.error("An exception occured on creating CSRF token", e);
                return "redirect:/generalError";
            }
            logger.info("================> put new subscriber");
            layout.addAttribute("subscriber", subscriber);
        } else if (layoutMap.containsKey("errors")) {
            layout.addAttribute(registerBinding, layoutMap.get("errors"));
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "modify";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/account/modify", method = RequestMethod.POST)
    public String modifyAccountHandle(@ModelAttribute("subscriber") @Validated({SubscriberAccountCheck.class}) 
    Subscriber subscriber, BindingResult binRes, @LoggedUser AuthenticationFrontendUserDetails user, 
    Model layout, RedirectAttributes redAtt) {
        logger.info("Received POST request " + subscriber);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("subscriber", subscriber);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                // no errors found, but we have check if email from subscriber is equal to email from User (subscriber's email is send by POST, it can be faked)
                Subscriber subFromUser = conversionService.convert(user, Subscriber.class);
logger.info("============> " + subFromUser);
logger.info("============> " + subFromUser.getEmail().equals(subscriber.getEmail()));
                if (subFromUser == null || !subFromUser.getEmail().equals(subscriber.getEmail())) {
                    redAtt.addFlashAttribute("error", true);
                    redAtt.addFlashAttribute("subscriber", subscriber);
                    logger.error("Exception caught on subFromUser comparaison");
                    throw new Exception("Subscriber got from User is null or User e-mail isn't the same as subscriber's e-mail : got subFromUser ["+subFromUser+"]");
                } else {
                    subscriber.setId(subFromUser.getId()); 
                    subscriber.setEmail(subscriber.getOldEmail()); 
                    subscriberService.updateEmail(subscriber);
                    redAtt.addFlashAttribute("success", true);
                }
            } catch (Exception e) {
                binRes.addError(getExceptionError("subscriber"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscriber", subscriber);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/account/modify";
    }

    /**
     * Password form actions.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/account/modify-password", method = RequestMethod.GET)
    public String modifyPassword(@ModelAttribute("subscriber") Subscriber subscriber, @LoggedUser 
    AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (!layoutMap.containsKey("errors")) {
            subscriber = conversionService.convert(user, Subscriber.class);
            try {
                csrfProtector.setIntention("modify-password");
                subscriber.setToken(csrfProtector.constructToken(request.getSession()));
                logger.info("Generated token " + subscriber.getToken());
                subscriber.setAction(csrfProtector.getIntention());
            } catch (Exception e) {
                logger.error("An exception occured on creating CSRF token", e);
                return "redirect:/generalError";
            }
            logger.info("================> put new subscriber : " + subscriber);
        } else if (layoutMap.containsKey("errors")) {
            layout.addAttribute(registerBinding, layoutMap.get("errors"));
        }
        // always, clean all passwords before displaying
        subscriber.setPassword("");
        subscriber.setOldPassword("");
        subscriber.setPasswordRepeated("");
        //subscriber.setLogin("");
        layout.addAttribute("subscriber", subscriber);
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "modifyPassword";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/account/modify-password", method = RequestMethod.POST)
    public String modifyPasswordHandle(@ModelAttribute("subscriber") @Validated({SubscriberPasswordCheck.class}) 
    Subscriber subscriber, BindingResult binRes, @LoggedUser AuthenticationFrontendUserDetails user, 
    Model layout, RedirectAttributes redAtt) {
        String redirectUrl = "redirect:/account/modify-password";
        logger.info("Received POST request " + subscriber);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("subscriber", subscriber);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                Subscriber subFromUser = conversionService.convert(user, Subscriber.class);
                subscriber.setId(subFromUser.getId());
                subscriber.setLogin(subFromUser.getLogin());
                subscriberService.updatePassword(subscriber);
                redAtt.addFlashAttribute("success", true);
                redirectUrl = "redirect:/logout";
            } catch (Exception e) {
                binRes.addError(getExceptionError("subscriber"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscriber", subscriber);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return redirectUrl;
    }

    /**
     * Authentication form actions.
     */
    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model layout, @LocaleLang Lang lang) {
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "login";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/account/avatar", method = RequestMethod.GET)
    public String modifyAvatar(@ModelAttribute("subscriber") Subscriber subscriber, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(registerBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("avatar");
            subscriber.setToken(csrfProtector.constructToken(request.getSession()));
            logger.info("Generated token " + subscriber.getToken());
            subscriber.setAction(csrfProtector.getIntention());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            return "redirect:/generalError";
        }
        logger.info("====================> " + layoutMap.get("errors"));
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "avatarForm";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/account/avatar", method = RequestMethod.POST)
    @ExceptionHandler({MultipartException.class, MaxUploadSizeExceededException.class})
    public String modifyAvatarHandle(@ModelAttribute("subscriber") @Validated({SubscriberAvatarCheck.class}) 
    Subscriber subscriber, BindingResult binRes, @LoggedUser AuthenticationFrontendUserDetails user, 
    Model layout, RedirectAttributes redAtt) {
        logger.info("Received POST request " + subscriber);
        if (binRes.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("subscriber", subscriber);
            redAtt.addFlashAttribute("errors", binRes);
        } else {
            try {
                Subscriber subFromUser = conversionService.convert(user, Subscriber.class);
                subFromUser.setAvatarFile(subscriber.getAvatarFile());
                subscriberService.addAvatar(subFromUser);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                binRes.addError(getExceptionError("subscriber"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("subscriber", subscriber);
                redAtt.addFlashAttribute("errors", binRes);
            }
        }
        return "redirect:/account/avatar";
    }
    
    @RequestMapping(value = "/session-expired", method = RequestMethod.GET)
    public String sessionExpired(Model layout, @LocaleLang Lang lang) {
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "sessionExpired";
    }
}