package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.NewsletterPreferencyCategory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterPreferencyCategoryRepository extends CrudRepository<NewsletterPreferencyCategory, Long> {
    @Query("SELECT npc FROM NewsletterPreferencyCategory npc WHERE npc.code = :code")
    public NewsletterPreferencyCategory getByCode(@Param("code") String code);

    @Query("SELECT npc FROM NewsletterPreferencyCategory npc WHERE npc.code IN :codes")
    public List<NewsletterPreferencyCategory> getFromCodes(@Param("codes") List<String> codes);
}