package library.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.model.entity.Category;
import library.model.entity.CategoryLang;
import library.model.entity.Lang;
import library.model.repository.CategoryLangRepository;
import library.model.repository.LangRepository;
import library.service.CategoryLangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("categoryLangService")
@Repository
// @Transactional
public class CategoryLangServiceImpl implements CategoryLangService {
    final Logger logger = LoggerFactory.getLogger(CategoryLangServiceImpl.class);
    @Autowired
    private CategoryLangRepository categoryLangRepository;
    @Autowired
    private LangRepository langRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'CATEGORYLANG_ADD')")
    public List<CategoryLang> addNew(List<CategoryLang> categoryLangs) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            for (CategoryLang categoryLang : categoryLangs) {
                logger.info("Saving categoryLang " + categoryLang);
                categoryLangRepository.save(categoryLang);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving categoryLang", e);
            transactionManager.rollback(status);
            categoryLangs = null;
            throw new Exception(e);
        }
        return categoryLangs;
    }
    
    @Override
    public Map<Long, CategoryLang> getTranslatedByCategory(Category category) {
        Map<Long, CategoryLang> result = new HashMap<Long, CategoryLang>();
        List<CategoryLang> entries = categoryLangRepository.getTranslatedByCategory(category);
        for (CategoryLang entry : entries) {
            result.put(entry.getLang().getId(), entry);
        }
        return result;
    }
    
    @Override
    public CategoryLang getByCatLang(Category category, Lang lang) {
        return categoryLangRepository.getByCatLang(category, lang);
    }
    
    @Override
    public List<CategoryLang> getAllCategoriesByLang(Lang lang) {
        return categoryLangRepository.getAllCategoriesByLang(lang);
    }
}