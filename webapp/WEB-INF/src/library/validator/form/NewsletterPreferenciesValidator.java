package library.validator.form;

import library.form.NewsPreferenciesCredentialsForm;
import library.model.entity.NewsletterSubscriber;
import library.model.entity.Subscriber;
import library.model.repository.NewsletterSubscriberRepository;
import library.security.SaltCellar;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.validation.Errors;

public class NewsletterPreferenciesValidator extends NewsletterSubscriberValidator {
    final Logger logger = LoggerFactory.getLogger(NewsletterPreferenciesValidator.class);

    public NewsletterPreferenciesValidator(Subscriber subscriber,
           SaltCellar saltCellar, ShaPasswordEncoder passwordEncoder, 
           NewsletterSubscriberRepository newsletterSubscriberRepository
    ) {
        this.subscriber = subscriber;
        this.saltCellar = saltCellar;
        this.passwordEncoder = passwordEncoder;
        this.newsletterSubscriberRepository = newsletterSubscriberRepository;
    }

    public boolean supports(Class clazz) {
        return NewsPreferenciesCredentialsForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        NewsPreferenciesCredentialsForm newsPreferenciesCredentialsForm = (NewsPreferenciesCredentialsForm)obj;
        String passwordEncoded = "";
        if (subscriber != null) {
            passwordEncoded = subscriber.getPassword();
        }
        else if (newsPreferenciesCredentialsForm.getCredPassword() != null && newsPreferenciesCredentialsForm.getCredEmail() != null) {
                passwordEncoded = passwordEncoder.encodePassword(newsPreferenciesCredentialsForm.getCredPassword(), saltCellar.getSaltFromString(newsPreferenciesCredentialsForm.getCredEmail()));
        }
        validateCredentials(newsPreferenciesCredentialsForm.getCredPassword(), passwordEncoded, newsPreferenciesCredentialsForm.getCredEmail(), errors);
        // validate more things too
        if (newsPreferenciesCredentialsForm.getEmail() != null) {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.isValid(newsPreferenciesCredentialsForm.getEmail(), null)) {
                errors.rejectValue("email", "error.newsletterPreferencies.emailFormat", "Invalid e-mail format");
            }
            NewsletterSubscriber newsletterSubscriber = newsletterSubscriberRepository.getByMailAndState(newsPreferenciesCredentialsForm.getEmail(), NewsletterSubscriber.STATE_CONFIRMED);
            if (newsletterSubscriber != null) {
                errors.rejectValue("email", "error.newsletterPreferencies.emailUsed", "This e-mail is already used");
            }
        }
    }
}