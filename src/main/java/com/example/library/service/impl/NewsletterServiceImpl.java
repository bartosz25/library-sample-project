package com.example.library.service.impl;

import java.util.Date;

import com.example.library.form.NewsletterWriteForm;
import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Newsletter;
import com.example.library.model.repository.NewsletterRepository;
import com.example.library.security.AuthenticationUserDetails;
import com.example.library.service.NewsletterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

@Service("newsletterService")
public class NewsletterServiceImpl implements NewsletterService {
    final Logger logger = LoggerFactory.getLogger(NewsletterServiceImpl.class);
    @Autowired
    private NewsletterRepository newsletterRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private ConversionService conversionService;

    @Override
    public Newsletter addNewsletter(NewsletterWriteForm newsletterWriteForm, AuthenticationUserDetails user, Newsletter newsletter) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        if (newsletter == null) newsletter = new Newsletter();
        try {
           newsletter.setAdmin(conversionService.convert(user, Admin.class));
           newsletter.setSendTime(newsletterWriteForm.getStartTime());
           newsletter.setState(Newsletter.STATE_NOT_SEND);
           newsletter.setTitle(newsletterWriteForm.getTitle());
           newsletter.setText(newsletterWriteForm.getText());
           newsletter.setPreferenciesFromList(newsletterWriteForm.getPreferencies());
           newsletter = newsletterRepository.save(newsletter);
           transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving newsletter subscription", e);
            transactionManager.rollback(status);
            newsletter = null;
            throw new Exception(e);
        }
        return newsletter;
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<Object[]> getNewslettersToSend(Date date, int state, int limit) {
        if (date == null) date = new Date();
        Pageable pageable = new PageRequest(0, limit);
        return newsletterRepository.getToSend(date, state, pageable);
    }
}