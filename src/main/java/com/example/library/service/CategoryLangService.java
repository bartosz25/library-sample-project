package com.example.library.service;

import java.util.List;
import java.util.Map;

import com.example.library.model.entity.Category;
import com.example.library.model.entity.CategoryLang;
import com.example.library.model.entity.Lang;

public interface CategoryLangService {
    // public List<Category> findById();
    public CategoryLang getByCatLang(Category category, Lang lang);
    public List<CategoryLang> addNew(List<CategoryLang> categoryLangs) throws Exception;
    // public Category getById(long id);
    // public void delete(Category category);
    public Map<Long, CategoryLang> getTranslatedByCategory(Category category);
    public List<CategoryLang> getAllCategoriesByLang(Lang lang);
}