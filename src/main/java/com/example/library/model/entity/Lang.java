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


@Entity
@Table(name="lang")
public class Lang implements Serializable {
    private Long id;
    private String iso;
    private String name;
    private List<CategoryLang> categoryLangs;
    private List<Question> questions;
    private List<NewsletterPreferencyLang> newsletterPreferencies;
    private List<NewsletterPreferencyCategoryLang> newsletterPreferencyCategories;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_la")
    public Long getId() {
        return id;
    }

    @Column(name="iso_la")
    public String getIso() {
        return iso;
    }

    @Column(name="name_la")
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "category")
    public List<CategoryLang> getCategoryLangs() {
        return categoryLangs;
    }

    @OneToMany(mappedBy = "lang")
    public List<NewsletterPreferencyLang> getNewsletterPreferencies() {
        return newsletterPreferencies;
    }

    @OneToMany(mappedBy = "lang")
    public List<NewsletterPreferencyCategoryLang> getNewsletterPreferencyCategories() {
        return newsletterPreferencyCategories;
    }

    @OneToMany(mappedBy = "lang")
    public List<Question> getQuestions() {
        return questions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryLangs(List<CategoryLang> categoryLangs) {
        this.categoryLangs = categoryLangs;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setNewsletterPreferencies(List<NewsletterPreferencyLang> newsletterPreferencies) {
        this.newsletterPreferencies = newsletterPreferencies;
    }

    public void setNewsletterPreferencyCategories(List<NewsletterPreferencyCategoryLang> newsletterPreferencyCategories) {
        this.newsletterPreferencyCategories = newsletterPreferencyCategories;
    }

    public String toString() {
        return "Lang [id = "+id+", iso = "+iso+", name = "+name+"]";
    }
}