package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NewsletterPreferencyLangPK implements Serializable {
    private long idNewsletterPreferency;
    private long idLang;

    public NewsletterPreferencyLangPK() {}

    // @Column(name = "newsletter_preferency_id_np")
    public long getIdNewsletterPreferency() {
	    return idNewsletterPreferency;
    }

    // @Column(name = "lang_id_la")
    public long getIdLang() {
	    return idLang;
    }
	
    public void setIdNewsletterPreferency(long idNewsletterPreferency) {
	    this.idNewsletterPreferency = idNewsletterPreferency;
    }

    public void setIdLang(long idLang) {
	    this.idLang = idLang;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        NewsletterPreferencyLangPK newsletterPreferencyLangPK = (NewsletterPreferencyLangPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idNewsletterPreferency, newsletterPreferencyLangPK.getIdNewsletterPreferency())
            .append(idLang, newsletterPreferencyLangPK.getIdLang())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idNewsletterPreferency)
            .append(idLang)
            .toHashCode();
    }

    public String toString() {
        return idNewsletterPreferency+"-"+idLang;
    }
}