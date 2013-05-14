package library.service;

import java.util.List;
import java.util.Map;

import library.model.entity.Category;

public interface CategoryService {
    // public List<Category> findById();
    public Category save(Category category) throws Exception;
    public Category getById(long id);
    public List<Category> getAll();
    public Map<String, List<String>> getAndGroupCategoriesByLang();
    // public void delete(Category category);
}