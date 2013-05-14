package library.service;

import java.util.Date;

import library.form.NewsletterWriteForm;
import library.model.entity.Newsletter;
import library.security.AuthenticationUserDetails;

import org.springframework.data.domain.Page;


public interface NewsletterService {
    public Newsletter addNewsletter(NewsletterWriteForm newsletterWriteForm, AuthenticationUserDetails user, Newsletter newsletter) throws Exception;
    public Page<Object[]> getNewslettersToSend(Date date, int state, int limit);
}