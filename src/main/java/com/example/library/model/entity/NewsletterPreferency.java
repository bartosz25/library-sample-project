package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="newsletter_preferency")
public class NewsletterPreferency extends ParentEntity implements Serializable {
    private long id;
    private NewsletterPreferencyCategory category;
    private String code;
    private String defaultLabel;
    private List<NewsletterPreferencyLang> newsletterPreferencies;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_np")
    public long getId() {
        return id;
    }

    @NotEmpty(message = "{error.newsletterPreferency.codeEmpty}")
    @Column(name="code_np")
    public String getCode() {
        return code;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_preferency_category_id_npc", nullable = false, insertable = false, updatable = false)
    public NewsletterPreferencyCategory getCategory() {
        return category;
    }

    @NotEmpty(message = "{error.newsletterPreferency.defaultLabelEmpty}")
    @Column(name="default_np")
    public String getDefaultLabel() {
        return defaultLabel;
    }

    @OneToMany(mappedBy = "newsletterPreferency")
    public List<NewsletterPreferencyLang> getNewsletterPreferencies() {
        return newsletterPreferencies;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(NewsletterPreferencyCategory category) {
        this.category = category;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public void setNewsletterPreferencies(List<NewsletterPreferencyLang> newsletterPreferencies) {
        this.newsletterPreferencies = newsletterPreferencies;
    }

    public String toString() {
        return "NewsletterPreferency [id = "+id+", category = "+category+", code = "+code+", defaultLabel = "+defaultLabel+"]";
    }
}