package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Book;
import com.example.library.model.entity.Lang;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository  extends CrudRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {
    @Query("SELECT b, bc, bl, c, cl FROM Book b JOIN b.bookCategories bc JOIN b.bookLangs bl JOIN bc.category c JOIN c.categoryLangs cl WHERE bl.lang = :lang AND cl.lang = :lang AND bl.bookLangPK.type = :type")
    public Page<Book> getAllBooks(@Param("lang") Lang lang, @Param("type") String type, Pageable pageable);

    @Query("SELECT b, bl FROM Book b JOIN b.bookLangs bl WHERE b.id = :id AND bl.lang = :lang")
    public List<Book> getByIdAndLang(@Param("id") long id, @Param("lang") Lang lang);
}