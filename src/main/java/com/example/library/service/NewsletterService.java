package com.example.library.service;

import java.util.Date;

import com.example.library.form.NewsletterWriteForm;
import com.example.library.model.entity.Newsletter;
import com.example.library.security.AuthenticationUserDetails;

import org.springframework.data.domain.Page;


public interface NewsletterService {
    public Newsletter addNewsletter(NewsletterWriteForm newsletterWriteForm, AuthenticationUserDetails user, Newsletter newsletter) throws Exception;
    public Page<Object[]> getNewslettersToSend(Date date, int state, int limit);
}