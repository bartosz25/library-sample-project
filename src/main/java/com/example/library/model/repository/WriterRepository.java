package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Writer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODOS : 
 * - getAllLangs() : rajouter LIMIT X, Y pour gérer la pagination
 * 
 * 
 * 
 */
public interface WriterRepository  extends CrudRepository<Writer, Long> {

    @Transactional(readOnly = true)
    @Query("select w from Writer w")
    public List<Writer> getAll();

    // @Transactional(readOnly = true)
    // @Query("select l from Lang l where id = :id")
    // public Lang findById(@Param("id") Long id);
}