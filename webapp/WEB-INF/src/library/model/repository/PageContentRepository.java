package library.model.repository;

import java.util.List;

import library.model.entity.Lang;
import library.model.entity.PageContent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODOS : 
 * - getAllPages() : rajouter LIMIT X, Y pour gérer la pagination
 * 
 * 
 * 
 */
public interface PageContentRepository  extends CrudRepository<PageContent, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.placement.id = :placementId ORDER BY p.placement.id ASC, p.title ASC")
    public List<PageContent> findByPlacement(@Param("placementId") Long placementId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.lang.id = :langId ORDER BY p.placement.id ASC, p.title ASC")
    public List<PageContent> findByLang(@Param("langId") Long langId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.lang.id = :langId AND p.placement.id = :placementId ORDER BY p.placement.id ASC, p.title ASC")
    public List<PageContent> findByLangAndPlacement(@Param("langId") Long langId,
                                             @Param("placementId") Long placementId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.url = :url")
    public PageContent findByUrl(@Param("url") String url);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.url = :url AND p.lang = :lang")
    public PageContent findByUrlAndLang(@Param("url") String url, @Param("lang") Lang lang);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM PageContent p WHERE p.id = :id")
    public PageContent findById(@Param("id") Long id);
    
    // @Transactional(readOnly = true)
    // @Query("SELECT p FROM PageContent p")
    // public PageContent getAllPages(Pageable pageable);
}