package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.NewsletterPreferencyCategoryLang;
import com.example.library.model.entity.NewsletterPreferencyCategoryLangPK;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterPreferencyCategoryLangRepository extends CrudRepository<NewsletterPreferencyCategoryLang, NewsletterPreferencyCategoryLangPK> {
    @Query("SELECT npcl FROM NewsletterPreferencyCategoryLang npcl WHERE npcl.newsletterPreferencyCategoryLangPK.idLang = :lang")
    public List<NewsletterPreferencyCategoryLang> getByLang(@Param("lang") long lang);
}