package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Lang;

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
public interface LangRepository  extends CrudRepository<Lang, Long> {
    @Transactional(readOnly = true)
    @Query("select l from Lang l")
    public List<Lang> getAllLangs();

    @Transactional(readOnly = true)
    @Query("select l from Lang l where id = :id")
    public Lang findById(@Param("id") Long id);

    @Query("select l from Lang l where iso = :iso")
    public Lang getByIso(@Param("iso") String iso);

    @Query("select l from Lang l where iso = 'FR'")
    public Lang getDefaultLang();
}