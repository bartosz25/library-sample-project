package library.model.repository;

import java.util.List;

import library.model.entity.BookCategory;
import library.model.entity.BookCategoryPK;
import library.model.entity.Lang;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookCategoryRepository  extends CrudRepository<BookCategory, BookCategoryPK> {
    @Transactional(readOnly = true)
    @Query("SELECT bc, c, cl, b FROM BookCategory bc JOIN bc.category c JOIN c.categoryLangs cl JOIN bc.book b WHERE cl.name = :name AND cl.lang = :lang")
    public List<BookCategory> findByCategoryTitle(@Param("name") String name, @Param("lang") Lang lang);
}