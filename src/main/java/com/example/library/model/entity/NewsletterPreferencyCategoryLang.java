package com.example.library.model.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "newsletter_preferency_category_lang")
public class NewsletterPreferencyCategoryLang extends ParentEntity implements Serializable {
    private NewsletterPreferencyCategoryLangPK newsletterPreferencyCategoryLangPK;
    private NewsletterPreferencyCategory newsletterPreferencyCategory;
    private Lang lang;
    private String label;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idNewsletterPreferencyCategory", column = @Column(name = "newsletter_preferency_category_id_npc", nullable = false)),
        @AttributeOverride(name = "idLang", column = @Column(name = "lang_id_la", nullable = false))
    })
    public NewsletterPreferencyCategoryLangPK getNewsletterPreferencyCategoryLangPK() {
        return newsletterPreferencyCategoryLangPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_preferency_category_id_npc", nullable = false, insertable = false, updatable = false)
    public NewsletterPreferencyCategory getNewsletterPreferencyCategory() {
        return newsletterPreferencyCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la", nullable = false, insertable = false, updatable = false)
    public Lang getLang() {
        return lang;
    }

    @Column(name="value_npcl")
    public String getLabel() {
        return label;
    }

    public void setNewsletterPreferencyCategoryLangPK(NewsletterPreferencyCategoryLangPK newsletterPreferencyCategoryLangPK) {
        this.newsletterPreferencyCategoryLangPK = newsletterPreferencyCategoryLangPK;
    }

    public void setNewsletterPreferencyCategoryLangPK(long idNewsletterPreferencyCategory, long idLang) {
        this.newsletterPreferencyCategoryLangPK = new NewsletterPreferencyCategoryLangPK();
        this.newsletterPreferencyCategoryLangPK.setIdNewsletterPreferencyCategory(idNewsletterPreferencyCategory);
        this.newsletterPreferencyCategoryLangPK.setIdLang(idLang);
    }

    public void setNewsletterPreferencyCategory(NewsletterPreferencyCategory newsletterPreferencyCategory) {
        this.newsletterPreferencyCategory = newsletterPreferencyCategory;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        return "NewsletterPreferencyCategoryLang [newsletterPreferencyCategoryLangPK : "+newsletterPreferencyCategoryLangPK+", newsletterPreferencyCategory :"+newsletterPreferencyCategory+" , "+
        "lang :"+lang+" , label :"+label+"]";
    }
}