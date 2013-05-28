package com.example.library.service;

import java.util.List;
import java.util.Map;

import com.example.library.model.entity.NewsletterPreferencyCategory;

public interface NewsletterPreferencyCategoryService {
    public Map<String, NewsletterPreferencyCategory> constructFromCodes(List<String> codes);
}