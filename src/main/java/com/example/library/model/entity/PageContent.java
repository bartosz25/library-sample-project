package com.example.library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "page_content")
public class PageContent extends ParentEntity implements Serializable {
    private Long id;
    private Lang lang;
    private Placement placement;
    private String title;
    private String url;
    private String content;
    private String header1;
    private String header2;
    private String metaTitle;
    private String metaDesc;
    private String metaKeywords;
    private Date added;
    private Date modified;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_pag")
    public Long getId() {
        return id;
    }
    /**
     * Annotation @Column est interdite ici
     */
    // @Column(name="lang_id_la")
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="lang_id_la")
    public Lang getLang() {
        return lang;
    }

    /**
     * Annotation @Column est interdite ici - si présente, une exception est captée :
     * Caused by: org.hibernate.AnnotationException: @Column(s) not allowed on a @OneToOne property: com.example.library.model.entity.PageContent.lang
     * at org.hibernate.cfg.AnnotationBinder.processElementAnnotations(AnnotationBinder.java:1615)
     * 
     */
    // @Column(name="placement_id_pl")
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="placement_id_pl")
    public Placement getPlacement() {
        return placement;
    }

    @Column(name="title_pag")
    public String getTitle() {
        return title;
    }

    @Column(name="url_pag")
    public String getUrl() {
        return url;
    }

    @Column(name="content_pag")
    public String getContent() {
        return content;
    }

    @Column(name="h1_pag")
    public String getHeader1() {
        return header1;
    }

    @Column(name="h2_pag")
    public String getHeader2() {
        return header2;
    }

    @Column(name="meta_title_pag")
    public String getMetaTitle() {
        return metaTitle;
    }

    @Column(name="meta_desc_pag")
    public String getMetaDesc() {
        return metaDesc;
    }

    @Column(name="meta_keywords_pag")
    public String getMetaKeywords() {
        return metaKeywords;
    }

    @Temporal(DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="added_pag")
    public Date getAdded() {
        return added;
    }

    @Temporal(DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="modified_pag")
    public Date getModified() {
        return modified;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeader1(String header1) {
        this.header1 = header1; 
    }

    public void setHeader2(String header2)  { 
        this.header2 = header2;
    } 

    public void setMetaTitle(String metaTitle) { 
        this.metaTitle = metaTitle;
    }

    public void setMetaDesc(String metaDesc) {    
        this.metaDesc = metaDesc;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String toString() {
        return "PageContent [title = "+title+", url = "+url+", content = "+content+", "+
        "header1 = "+header1+", header2 = "+header2+", metaTitle = "+metaTitle+", metaDesc = "+metaDesc+
        ", metaKeywords = "+metaKeywords+"]";
        
    }
    // @NotNull(message = "The title can't be null")
    // @Size(min = 1, max = 100, message = "The length can't exced 100 chars")
    // @IsEqual(value = "test")
    // private String title;
    // @NotNull(message = "The date can't be null")
    // @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    // private Date addedDate;
    
    // @Id
    // @GeneratedValue(strategy = IDENTITY)
    // @Column(name="id_bo")
    // public Long getId()
    // {
        // return id;
    // }
    // @Column(name="title_bo")
    // public String getTitle()
    // {
        // return title;
    // }
    // @Temporal(DATE)
    // @Column(name="added_bo")
    // public Date getAddedDate()
    // {
        // return addedDate;
    // }
    // public void setId(long i)
    // {
        // id = i;
    // }
    // public void setTitle(String t)
    // {
        // title = t;
    // }
    // public void setAddedDate(Date d)
    // {
        // if(d == null) d = new Date();
        // addedDate = d;
    // }
    // public String toString()
    // {
        // return "Book [id = "+id+", title="+title+", added date = "+addedDate+"]";
    // }
}