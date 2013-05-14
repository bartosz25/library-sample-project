package library.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.model.entity.Category;
import library.model.entity.CategoryLang;
import library.model.entity.Lang;
import library.model.repository.CategoryLangRepository;
import library.model.repository.CategoryRepository;
import library.model.repository.LangRepository;
import library.service.CategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service("categoryService")
// @Repository
// @Transactional
public class CategoryServiceImpl implements CategoryService {
    final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LangRepository langRepository;
    @Autowired
    private CategoryLangRepository categoryLangRepository;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORY_ADD')")
    public Category save(Category category) throws Exception {
        logger.info("Saving category " + category);
        return categoryRepository.save(category);
    }
    
    @Override
    public Category getById(long id) {
        return categoryRepository.findOne(id);
    }
    
    @Override
    public List<Category> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public Map<String, List<String>> getAndGroupCategoriesByLang() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (Lang lang : langRepository.findAll()) {
            List<String> langs = new ArrayList<String>();
            for (CategoryLang categoryLang : categoryLangRepository.getAllCategoriesByLang(lang)) {
                langs.add(categoryLang.getName());
                
            }
            result.put(lang.getIso(), langs);
        }
        logger.info("Found categories : " + result);
        return result;
    }
}