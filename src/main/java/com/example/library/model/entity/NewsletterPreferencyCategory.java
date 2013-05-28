package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="newsletter_preferency_category")
public class NewsletterPreferencyCategory extends ParentEntity implements Serializable {
    private long id;
    private String code;
    private String alias;
    private List<NewsletterPreferencyCategoryLang> categories;
    private List<NewsletterPreferency> preferencies;
    private List<NewsletterSubscriberPreferency> subscriberPreferencies;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_npc")
    public long getId() {
        return id;
    }

    @NotEmpty(message = "{error.newsletterPreferencyCategory.codeEmpty}")
    @Column(name="code_npc")
    public String getCode() {
        return code;
    }

    @NotEmpty(message = "{error.newsletterPreferencyCategory.aliasEmpty}")
    @Column(name="alias_npc")
    public String getAlias() {
        return alias;
    }

    @OneToMany(mappedBy = "newsletterPreferencyCategory")
    public List<NewsletterPreferencyCategoryLang> getCategories() {
        return categories;
    }

    @OneToMany(mappedBy = "category")
    public List<NewsletterPreferency> getPreferencies() {
        return preferencies;
    }

    @OneToMany(mappedBy = "newsletterPreferencyCategory")
    public List<NewsletterSubscriberPreferency> getSubscriberPreferencies() {
        return subscriberPreferencies;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCategories(List<NewsletterPreferencyCategoryLang> categories) {
        this.categories = categories;
    }

    public void setPreferencies(List<NewsletterPreferency> preferencies) {
        this.preferencies = preferencies;
    }

    public void setSubscriberPreferencies(List<NewsletterSubscriberPreferency> subscriberPreferencies) {
        this.subscriberPreferencies = subscriberPreferencies;
    }

    public String toString() {
        return "NewsletterPreferencyCategory [id = "+id+", code = "+code+", alias = "+alias+"]";
    }
}