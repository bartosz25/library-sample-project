package library.service;

import java.util.List;
import java.util.Map;

import library.model.entity.NewsletterPreferencyCategory;

public interface NewsletterPreferencyCategoryService {
    public Map<String, NewsletterPreferencyCategory> constructFromCodes(List<String> codes);
}