package library.validator.form;

import library.model.entity.NewsletterSubscriber;
import library.model.entity.Subscriber;
import library.model.repository.NewsletterSubscriberRepository;
import library.security.SaltCellar;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class NewsletterSubscriberValidator implements Validator {
    protected NewsletterSubscriberRepository newsletterSubscriberRepository;
    protected Subscriber subscriber;
    protected ShaPasswordEncoder passwordEncoder;
    protected SaltCellar saltCellar;
    
    protected void validateCredentials(String passwordPlainText, String passwordEncoded, String email, Errors errors) {
        if (email == null) {
            errors.rejectValue("email", "error.newsletterSubscriber.emailEmpty", "Fill up your e-mail address");
        }
        if (subscriber == null) {
            if (passwordPlainText == null) {
                errors.rejectValue("password", "error.newsletterSubscriber.passwordEmpty", "Fill up your password");            
            }
        }
        NewsletterSubscriber subscriberByCriterion = newsletterSubscriberRepository.getByMailAndEncodedPassword(email, passwordEncoded, NewsletterSubscriber.STATE_CONFIRMED);
        if (subscriberByCriterion == null) {
            errors.rejectValue("email", "error.newsletterSubscriber.emailBad", "User was not found. Password, e-mail or both values are invalid.");
        }
    }
}