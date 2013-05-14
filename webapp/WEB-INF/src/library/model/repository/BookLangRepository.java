package library.model.repository;

import library.model.entity.BookLang;
import library.model.entity.BookLangPK;
import library.model.entity.Lang;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookLangRepository  extends CrudRepository<BookLang, BookLangPK>, PagingAndSortingRepository<BookLang, BookLangPK> {
    @Transactional(readOnly = true)
    @Query("SELECT bl, bc FROM BookLang bl, BookCategory bc WHERE bl.lang = :lang AND bl.bookLangPK.type = :type")
    // @Query("SELECT bl, bc FROM BookLang bl JOIN bl.bookCategories bc WHERE bl.lang = :lang AND bl.bookLangPK.type = :type")
        // @Query(value = "SELECT bl.*, bc.* FROM book_lang bl JOIN book_category bc ON bc.book_id_bo = bl.book_id_bo", nativeQuery = true)
    public Page<BookLang> findAllForLangAndType(@Param("lang") Lang lang, @Param("type") String type, Pageable pageable);

    @Query("SELECT bl FROM BookLang bl WHERE bl.bookLangPK.idBook = :idBook AND bl.bookLangPK.idLang = :idLang AND bl.bookLangPK.type = :type")
    public BookLang getByFieldLangAndBook(@Param("type") String type, @Param("idLang") long idLang, @Param("idBook") long idBook);
    // @Query("SELECT bl FROM BookLang bl WHERE bl.lang = :lang AND bl.type = :type")
    // public Page<BookLang> findAllByTitleForLang(Pageable pageable, @Param("lang") Lang lang, @Param("type") String type);
}