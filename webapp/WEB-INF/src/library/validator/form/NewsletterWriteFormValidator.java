package library.validator.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import library.form.NewsletterWriteForm;
import library.model.entity.NewsletterSubscriber;
import library.security.CSRFProtector;
import library.service.NewsletterSubscriberService;
import library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class NewsletterWriteFormValidator implements Validator {
    final Logger logger = LoggerFactory.getLogger(NewsletterWriteFormValidator.class);
    private NewsletterSubscriberService newsletterSubscriberService;
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;  

    public NewsletterWriteFormValidator(NewsletterSubscriberService newsletterSubscriberService, 
    CSRFProtector csrfProtector, HttpServletRequest request) {
        this.newsletterSubscriberService = newsletterSubscriberService;
        this.csrfProtector = csrfProtector;
        this.request = request;
    }

    public boolean supports(Class clazz) {
        return NewsletterWriteForm.class.equals(clazz);
    }
    
    /**
     * Validator will check :
     * - if title and text aren't empty
     * - if at least one subscriber was found to precised criteria
     * We don't valid startDate. It can be in the past. In this case, NewsletterSenderAsyn will send this 
     * newsletter immediately.
     */
    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        NewsletterWriteForm newsletterWriteForm = (NewsletterWriteForm) obj;
        if (newsletterWriteForm.getTitle() == null || newsletterWriteForm.getTitle().equals("")) {
            errors.rejectValue("title", "error.newsletterWriteForm.titleEmpty", "Title can't be empty");
        }
        if (newsletterWriteForm.getText() == null || newsletterWriteForm.getText().equals("")) {
            errors.rejectValue("text", "error.newsletterWriteForm.textEmpty", "Text can't be empty");
        }
        List<Object[]> subscribers = newsletterSubscriberService.getByCriteria(newsletterWriteForm.getPreferencies(), null, 1);
        if (subscribers == null || subscribers.size() == 0) {
            errors.rejectValue("preferencies", "error.newsletterWriteForm.preferenciesNobody", "Nobody was found corresponding to this criteria");
        }
        
        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(newsletterWriteForm, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }
}