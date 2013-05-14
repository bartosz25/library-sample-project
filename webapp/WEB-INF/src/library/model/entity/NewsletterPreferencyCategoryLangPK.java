package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NewsletterPreferencyCategoryLangPK implements Serializable {
    private long idNewsletterPreferencyCategory;
    private long idLang;

    public NewsletterPreferencyCategoryLangPK() {}

    // @Column(name = "newsletter_preferency_category_id_npc")
    public long getIdNewsletterPreferencyCategory() {
	    return idNewsletterPreferencyCategory;
    }

    // @Column(name = "lang_id_la")
    public long getIdLang() {
	    return idLang;
    }
	
    public void setIdNewsletterPreferencyCategory(long idNewsletterPreferencyCategory) {
	    this.idNewsletterPreferencyCategory = idNewsletterPreferencyCategory;
    }

    public void setIdLang(long idLang) {
	    this.idLang = idLang;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        NewsletterPreferencyCategoryLangPK newsletterPreferencyCategoryLangPK = (NewsletterPreferencyCategoryLangPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idNewsletterPreferencyCategory, newsletterPreferencyCategoryLangPK.getIdNewsletterPreferencyCategory())
            .append(idLang, newsletterPreferencyCategoryLangPK.getIdLang())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idNewsletterPreferencyCategory)
            .append(idLang)
            .toHashCode();
    }

    public String toString() {
        return idNewsletterPreferencyCategory+"-"+idLang;
    }
}