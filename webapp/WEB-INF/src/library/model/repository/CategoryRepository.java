package library.model.repository;

import java.util.List;

import library.model.entity.Category;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODOS : 
 * - getAllLangs() : rajouter LIMIT X, Y pour gérer la pagination
 * 
 * 
 * 
 */
public interface CategoryRepository  extends CrudRepository<Category, Long> {	
    // avec ça on a une exception Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Transaction marked as rollbackOnly@Transactional(readOnly = true)
    @Query("select c from Category c")
    public List<Category> getAll();

    // @Transactional(readOnly = true)
    // @Query("select l from Lang l where id = :id")
    // public Lang findById(@Param("id") Long id);
}