package com.example.library.form;
 
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.example.library.model.entity.NewsletterSubscriber;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class NewsPreferenciesCredentialsForm implements Serializable {
    final Logger logger = LoggerFactory.getLogger(NewsPreferenciesCredentialsForm.class);
    private EntityManagerFactory emf;
    private String email;
    private String password;
    private String credEmail;
    private String credPassword;
    private boolean isSubscriber;
    private Map<String, Map<String, Map<String, Object>>> categories;
    private Map<String, String> translations;
    private List<String> preferencies;
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCredEmail() {
        return credEmail;
    }

    public String getCredPassword() {
        return credPassword;
    }

    public boolean getIsSubscriber() {
        return isSubscriber;
    }

    public Map<String, Map<String, Map<String, Object>>> getCategories() {
        return categories;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public List<String> getPreferencies() {
        return preferencies;
    }

    public String[] getPreferenciesArray() {
        String[] strArray = new String[preferencies.size()];
        return preferencies.toArray(strArray);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredEmail(String credEmail) {
        this.credEmail = credEmail;
    }

    public void setCredPassword(String credPassword) {
        this.credPassword = credPassword;
    }

    public void setIsSubscriber(boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    public void setCategories(Map<String, Map<String, Map<String, Object>>> categories) {
        this.categories = categories;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void setPreferencies(List<String> preferencies) {
        this.preferencies = preferencies;
    }

    public void validateCredentials(ValidationContext context) {
        logger.info("============> VALIDATING NewsPreferenciesCredentialsForm" + toString());
        logger.info("============> VALIDATING emf" + emf);
        MessageContext messages = context.getMessageContext();
        if (email == null || email.trim().equals("")) {
            messages.addMessage(new MessageBuilder().error().source("email").
            defaultText("E-mail is obligatory").build());
        } else if (email != null && !email.trim().equals("")) {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.isValid(email, null)) {
                messages.addMessage(new MessageBuilder().error().source("email").
                defaultText("E-mail format is invalid").build());
            }
        }
        if (emf == null) {
            messages.addMessage(new MessageBuilder().error().source("email").
            defaultText("Impossible to check e-mail").build());
        } else {
            EntityManager entityManager = emf.createEntityManager();
            Query queryInst = entityManager.createQuery("SELECT ns FROM NewsletterSubscriber ns WHERE ns.email = :email AND ns.state = :state");
            queryInst.setParameter("email", email); 
            queryInst.setParameter("state", NewsletterSubscriber.STATE_CONFIRMED); 
            List list = queryInst.getResultList();
            if (list != null && list.size() > 0) {
                messages.addMessage(new MessageBuilder().error().source("email").
                defaultText("This e-mail is already used").build());
            }
        }
    }

    public void validateSummary(ValidationContext context) {
        logger.info("============> VALIDATING SUMMARY NewsPreferenciesCredentialsForm" + toString());
        MessageContext messages = context.getMessageContext();
        if (!getIsSubscriber() && (password == null || password.trim().equals(""))) {
            messages.addMessage(new MessageBuilder().error().source("password").
            defaultText("Password is obligatory").build());
        }
    }

    public String toString() {
        return "NewsPreferenciesCredentials [email : "+email+", credEmail :"+credEmail+", preferencies " + preferencies + " ]";
    }
}