package com.example.library.model.repository;

import com.example.library.model.entity.BookCopy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookCopyRepository  extends CrudRepository<BookCopy, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT bl FROM BookCopy bl WHERE bl.id = :id AND bl.state = :state")
    public BookCopy getOneNotBorrowed(@Param("id") long id, @Param("state") int state);
}