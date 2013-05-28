package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.NewsletterPreferencyLang;
import com.example.library.model.entity.NewsletterPreferencyLangPK;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterPreferencyLangRepository extends CrudRepository<NewsletterPreferencyLang, NewsletterPreferencyLangPK> {
    @Query("SELECT npl FROM NewsletterPreferencyCategory npc JOIN npc.preferencies np JOIN np.newsletterPreferencies npl WHERE npc.id = :idCategory AND npl.newsletterPreferencyLangPK.idLang = :idLang")
    public List<NewsletterPreferencyLang> getByLangAndCategory(@Param("idCategory") long idCategory, @Param("idLang") long idLang);
   
    @Query("SELECT npl FROM NewsletterPreferencyLang npl WHERE npl.newsletterPreferencyLangPK.idNewsletterPreferency = :idPreferency AND npl.newsletterPreferencyLangPK.idLang = :idLang")
    public NewsletterPreferencyLang getByPrefAndLang(@Param("idPreferency") long idPreferency, @Param("idLang") long idLang);
}