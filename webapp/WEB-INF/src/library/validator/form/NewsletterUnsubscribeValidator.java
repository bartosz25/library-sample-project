package library.validator.form;

import library.model.entity.NewsletterSubscriber;
import library.model.entity.Subscriber;
import library.model.repository.NewsletterSubscriberRepository;
import library.security.SaltCellar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.validation.Errors;

public class NewsletterUnsubscribeValidator extends NewsletterSubscriberValidator {
    final Logger logger = LoggerFactory.getLogger(NewsletterUnsubscribeValidator.class);

    public NewsletterUnsubscribeValidator(Subscriber subscriber,
           SaltCellar saltCellar, ShaPasswordEncoder passwordEncoder, 
           NewsletterSubscriberRepository newsletterSubscriberRepository
    ) {
        this.subscriber = subscriber;
        this.saltCellar = saltCellar;
        this.passwordEncoder = passwordEncoder;
        this.newsletterSubscriberRepository = newsletterSubscriberRepository;
    }

    public boolean supports(Class clazz) {
        return NewsletterSubscriber.class.equals(clazz);
    }

/**
 * Validation est la suivante : 
 * - e-mail obligatoire pour les deux (connecté / hors connecté)
 * - le mot de passe obligatoire pour hors connecté
 * - cryptage du mot de passe par rapport à l'email
 * - vérification si la paire e-mail - mot de passe existe dans la base
 */
    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        NewsletterSubscriber newsletterSubscriber = (NewsletterSubscriber)obj;
        String passwordEncoded = "";
        if (subscriber != null) {
            passwordEncoded = subscriber.getPassword();
        }
        else if (newsletterSubscriber.getPassword() != null && newsletterSubscriber.getEmail() != null) {
                passwordEncoded = passwordEncoder.encodePassword(newsletterSubscriber.getPassword(), saltCellar.getSaltFromString(newsletterSubscriber.getEmail()));
        }
        validateCredentials(newsletterSubscriber.getPassword(), passwordEncoded, newsletterSubscriber.getEmail(), errors);
    }
}