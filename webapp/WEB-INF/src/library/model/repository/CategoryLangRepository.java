package library.model.repository;

import java.util.List;

import library.model.entity.Category;
import library.model.entity.CategoryLang;
import library.model.entity.CategoryLangPK;
import library.model.entity.Lang;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODOS : 
 * - getAllLangs() : rajouter LIMIT X, Y pour gérer la pagination
 * 
 * 
 * 
 */
public interface CategoryLangRepository  extends CrudRepository<CategoryLang, CategoryLangPK> {
    // @Transactional(readOnly = true)
    // @Query("select l from Lang l")
    // public List<Lang> getAllLangs();

    @Transactional(readOnly = true)
    @Query("select cl from CategoryLang cl where category = :category AND lang = :lang")
    public CategoryLang getByCatLang(@Param("category") Category category, @Param("lang") Lang lang);

    @Transactional(readOnly = true)
    @Query("select cl from CategoryLang cl where category = :category")
    public List<CategoryLang> getTranslatedByCategory(@Param("category") Category category);

    @Transactional(readOnly = true)
    @Query("select cl from CategoryLang cl where lang = :lang order by cl.name asc")
    public List<CategoryLang> getAllCategoriesByLang(@Param("lang") Lang lang);
}