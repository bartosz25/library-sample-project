package com.example.library.service.impl;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManagerFactory;

import com.example.library.form.NewsPreferenciesCredentialsForm;
import com.example.library.model.entity.CategoryLang;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.NewsletterPreferencyCategoryLang;
import com.example.library.model.entity.NewsletterPreferencyLang;
import com.example.library.model.entity.NewsletterSubscriber;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Writer;
import com.example.library.model.repository.CategoryLangRepository;
import com.example.library.model.repository.NewsletterPreferencyCategoryLangRepository;
import com.example.library.model.repository.NewsletterPreferencyLangRepository;
import com.example.library.model.repository.WriterRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.service.NewsletterPreferencyService;
import com.example.library.service.NewsletterSubscriberPreferencyService;
import com.example.library.service.SubscriberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.webflow.core.collection.LocalAttributeMap;

@Service("newsletterPreferencyService")
public class NewsletterPreferencyServiceImpl implements NewsletterPreferencyService {
    final Logger logger = LoggerFactory.getLogger(NewsletterPreferencyServiceImpl.class);
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private NewsletterPreferencyCategoryLangRepository newsletterPreferencyCategoryLangRepository;
    @Autowired
    private NewsletterPreferencyLangRepository newsletterPreferencyLangRepository;
    @Autowired
    private CategoryLangRepository categoryLangRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private NewsletterSubscriberPreferencyService newsletterSubscriberPreferencyService;
    @Autowired
    private EntityManagerFactory emf;

// public long testCoupon()
// {
// logger.info("Testing coupon on end : 994");
    // return 994;
// }

    @Override
    public NewsPreferenciesCredentialsForm createCredentialsForm(UsernamePasswordAuthenticationToken user, LocalAttributeMap conversationScope) {
    logger.info("Found conversation scope " + conversationScope);
        NewsPreferenciesCredentialsForm form = new NewsPreferenciesCredentialsForm();
        form.setEmf(emf);
        if (conversationScope.contains("email") && conversationScope.get("email") != null) {
            form.setEmail((String)conversationScope.get("email"));
        }
        if (user == null) return form;
        AuthenticationFrontendUserDetails principal = (AuthenticationFrontendUserDetails)user.getPrincipal();
        Subscriber subscriber = subscriberService.loadByUsername(principal.getUsername());
        if (form.getEmail() == null) {
            form.setEmail(subscriber.getEmail());
        }
        form.setIsSubscriber(true);
        return form;
    }

    @Override
    public NewsPreferenciesCredentialsForm createPreferenciesForm(UsernamePasswordAuthenticationToken user, 
    LocalAttributeMap conversationScope) {
        logger.info("Found conversation scope " + conversationScope);
        List<String> savedPreferencies = new ArrayList<String>();
        if (conversationScope != null && conversationScope.contains("preferencies") && conversationScope.get("preferencies") != null) {
            Collections.addAll(savedPreferencies, (String[]) conversationScope.get("preferencies"));
            logger.info("====> savedPreferencies " + savedPreferencies);
            logger.info("====> conversationScope.get('preferencies') " + conversationScope.get("preferencies"));
        }
        NewsPreferenciesCredentialsForm form = createPreferenciesForm(savedPreferencies);
        if (user == null) return form;
        AuthenticationFrontendUserDetails principal = (AuthenticationFrontendUserDetails) user.getPrincipal();
        Subscriber subscriber = subscriberService.loadByUsername(principal.getUsername());
        form.setIsSubscriber(true);
        // TODO : pré-remplir les préférences cochées dans le cas d'une modification des préférences
        return form;
    }

    @Override
    public NewsPreferenciesCredentialsForm createPreferenciesModifyForm(NewsletterSubscriber 
    newsletterSubscriber) {
        List<String> savedPreferencies = newsletterSubscriberPreferencyService.getSavedPreferencies(newsletterSubscriber);
        NewsPreferenciesCredentialsForm form = createPreferenciesForm(savedPreferencies);
        if (newsletterSubscriber.getSubscriber() == null) return form;
        form.setIsSubscriber(true);
        return form;
    }

    @Override
    public NewsPreferenciesCredentialsForm createPasswordForm() {
        return new NewsPreferenciesCredentialsForm();
    }
    
    @Override
    public Map<String, Map<String, Map<String, Object>>>  getTranslatedCategories(List<
    NewsletterPreferencyCategoryLang> prefCategories, Lang lang, List<String> savedPreferencies) {
        // TODO : gérer l'attribution des valeurs dynamiquement (par exemple avec Apache BeanUtils)
        Map<String, Map<String, Map<String, Object>>> categories = new TreeMap<String, Map<String, Map<String, Object>>>();
        for (NewsletterPreferencyCategoryLang category : prefCategories) {
            Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
            String attributes = "";
            if (category.getNewsletterPreferencyCategory().getCode().equals("BOOKCATE")) {
                List<CategoryLang> bookCategories = categoryLangRepository.getAllCategoriesByLang(lang);
                for (CategoryLang categoryLang : bookCategories) {
                    String key = category.getNewsletterPreferencyCategory().getCode()+"-"+categoryLang.getCategory().getId();
                    attributes = "";
                    if (savedPreferencies != null && savedPreferencies.indexOf(key) > -1) {
                        attributes = "selected=\"selected\"";
                    }
                    Map<String, Object> resultValues = new HashMap<String, Object>();
                    resultValues.put("label", categoryLang.getName());
                    resultValues.put("attributes", attributes);
                    resultMap.put(""+categoryLang.getCategory().getId(), resultValues);
                }
            } else if (category.getNewsletterPreferencyCategory().getCode().equals("WRITERFA")) {
                List<Writer> writers = writerRepository.getAll();
                for (Writer writer : writers) {
                    String key = category.getNewsletterPreferencyCategory().getCode()+"-"+writer.getId();
                    attributes = "";
                    if (savedPreferencies != null && savedPreferencies.indexOf(key) > -1) {
                        attributes = "selected=\"selected\"";
                    }
                    Map<String, Object> resultValues = new HashMap<String, Object>();
                    resultValues.put("label", writer.getFullname());
                    resultValues.put("attributes", attributes);
                    resultMap.put(""+writer.getId(), resultValues);
                }
            } else {
                List<NewsletterPreferencyLang> preferencies = newsletterPreferencyLangRepository.getByLangAndCategory(category.getNewsletterPreferencyCategory().getId(), lang.getId());
                for (NewsletterPreferencyLang preferency : preferencies) {
                    String key = category.getNewsletterPreferencyCategory().getCode()+"-"+preferency.getNewsletterPreferencyLangPK().toString();
                    attributes = "";
                    if (savedPreferencies != null && savedPreferencies.indexOf(key) > -1) {
                        attributes = "selected=\"selected\"";
                    }
                    Map<String, Object> resultValues = new HashMap<String, Object>();
                    resultValues.put("label", preferency.getLabel());
                    resultValues.put("attributes", attributes);
                    resultMap.put(preferency.getNewsletterPreferencyLangPK().toString(), resultValues);
                }
            }
            categories.put(category.getNewsletterPreferencyCategory().getCode(), resultMap);
        }
        return categories;
    }
    
    @Override
    public Map<String, String> getTranslations(List<NewsletterPreferencyCategoryLang> prefCategories) {
        Map<String, String> translations = new HashMap<String, String>();
        for (NewsletterPreferencyCategoryLang category : prefCategories) {
            translations.put(category.getNewsletterPreferencyCategory().getCode(), category.getLabel());
        }
        return translations;
    }
    
    private NewsPreferenciesCredentialsForm createPreferenciesForm(List<String> savedPreferencies) {
        // TODO : gérer Lang dynamiquement
        Lang lang = new Lang();
        lang.setId(2l);
        NewsPreferenciesCredentialsForm form = new NewsPreferenciesCredentialsForm();
        // Construct preferency choices
        List<NewsletterPreferencyCategoryLang> prefCategories = newsletterPreferencyCategoryLangRepository.getByLang(lang.getId());
        form.setTranslations(getTranslations(prefCategories));
        form.setCategories(getTranslatedCategories(prefCategories, lang, savedPreferencies));
        logger.info("===========> FOUND CATEGORIES " + form.getCategories());
        return form;
    }
}