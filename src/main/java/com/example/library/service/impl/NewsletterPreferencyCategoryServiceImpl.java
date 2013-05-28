package com.example.library.service.impl;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.library.model.entity.NewsletterPreferencyCategory;
import com.example.library.model.repository.NewsletterPreferencyCategoryRepository;
import com.example.library.service.NewsletterPreferencyCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newsletterPreferencyCategoryService")
public class NewsletterPreferencyCategoryServiceImpl implements NewsletterPreferencyCategoryService {
    final Logger logger = LoggerFactory.getLogger(NewsletterPreferencyCategoryServiceImpl.class);
    @Autowired
    private NewsletterPreferencyCategoryRepository newsletterPreferencyCategoryRepository;
    
    public Map<String, NewsletterPreferencyCategory> constructFromCodes(List<String> codes) {
        Map<String, NewsletterPreferencyCategory> result = new HashMap<String, NewsletterPreferencyCategory>();
        if (codes != null && codes.size() > 0) {
            List<NewsletterPreferencyCategory> categories = newsletterPreferencyCategoryRepository.getFromCodes(codes);
            logger.info("============> got catgories " + categories);
            for (NewsletterPreferencyCategory category : categories) {
                result.put(category.getCode(), category);
            }
        }
        return result;
    }
}