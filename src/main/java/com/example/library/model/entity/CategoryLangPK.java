package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CategoryLangPK implements Serializable {
    private long idCategory;
    private long idLang;
        
    public CategoryLangPK() {}

    // @Column(name = "category_id_ca")
    public long getIdCategory() {
        return idCategory;
    }

    // @Column(name = "lang_id_la")
    public long getIdLang() {
        return idLang;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public void setIdLang(long idLang) {
        this.idLang = idLang;
    }
        
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        CategoryLangPK categoryLangPK = (CategoryLangPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idCategory, categoryLangPK.getIdCategory())
            .append(idLang, categoryLangPK.getIdLang())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idCategory)
            .append(idLang)
            .toHashCode();
    }
}