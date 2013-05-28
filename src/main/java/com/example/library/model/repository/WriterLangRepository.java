package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Writer;
import com.example.library.model.entity.WriterLang;
import com.example.library.model.entity.WriterLangPK;

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
public interface WriterLangRepository  extends CrudRepository<WriterLang, WriterLangPK> {
    // @Transactional(readOnly = true)
    // @Query("select l from Lang l")
    // public List<Lang> getAllLangs();

    @Transactional(readOnly = true)
    @Query("select wl from WriterLang wl where writer = :writer")
    public List<WriterLang> getForWriter(@Param("writer") Writer writer);

    @Transactional(readOnly = true)
    @Query("select wl from WriterLang wl where lang = :lang")
    public List<WriterLang> getAllWritersByLang(@Param("lang") Lang lang);
}