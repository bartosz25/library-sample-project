package library.service;

import java.util.List;
import java.util.Map;

import library.form.NewsPreferenciesCredentialsForm;
import library.model.entity.Lang;
import library.model.entity.NewsletterPreferencyCategoryLang;
import library.model.entity.NewsletterSubscriber;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.webflow.core.collection.LocalAttributeMap;

public interface NewsletterPreferencyService {
    public NewsPreferenciesCredentialsForm createCredentialsForm(UsernamePasswordAuthenticationToken user, LocalAttributeMap conversationScope);
    public NewsPreferenciesCredentialsForm createPreferenciesForm(UsernamePasswordAuthenticationToken user, LocalAttributeMap conversationScope);
    public NewsPreferenciesCredentialsForm createPasswordForm();
    public NewsPreferenciesCredentialsForm createPreferenciesModifyForm(NewsletterSubscriber newsletterSubscriber);
    public Map<String, Map<String, Map<String, Object>>>  getTranslatedCategories(List<NewsletterPreferencyCategoryLang> prefCategories, Lang lang, List<String> savedPreferencies);
    public Map<String, String> getTranslations(List<NewsletterPreferencyCategoryLang> prefCategories);
}