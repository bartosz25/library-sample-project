package library.model.entity;

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
@Table(name = "newsletter_preferency_lang")
public class NewsletterPreferencyLang extends ParentEntity implements Serializable {
    private NewsletterPreferencyLangPK newsletterPreferencyLangPK;
    private NewsletterPreferency newsletterPreferency;
    private Lang lang;
    private String label;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idNewsletterPreferency", column = @Column(name = "newsletter_preferency_id_np", nullable = false)),
        @AttributeOverride(name = "idLang", column = @Column(name = "lang_id_la", nullable = false))
    })
    public NewsletterPreferencyLangPK getNewsletterPreferencyLangPK() {
        return newsletterPreferencyLangPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_preferency_id_np", nullable = false, insertable = false, updatable = false)
    public NewsletterPreferency getNewsletterPreferency() {
        return newsletterPreferency;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la", nullable = false, insertable = false, updatable = false)
    public Lang getLang() {
        return lang;
    }

    @Column(name="label_npl")
    public String getLabel() {
        return label;
    }

    public void setNewsletterPreferencyLangPK(NewsletterPreferencyLangPK newsletterPreferencyLangPK) {
        this.newsletterPreferencyLangPK = newsletterPreferencyLangPK;
    }

    public void setNewsletterPreferencyLangPK(long idNewsletterPreferency, long idLang) {
        this.newsletterPreferencyLangPK = new NewsletterPreferencyLangPK();
        this.newsletterPreferencyLangPK.setIdNewsletterPreferency(idNewsletterPreferency);
        this.newsletterPreferencyLangPK.setIdLang(idLang);
    }

    public void setNewsletterPreferency(NewsletterPreferency newsletterPreferency) {
        this.newsletterPreferency = newsletterPreferency;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        return "NewsletterPreferencyLang [newsletterPreferencyLangPK : "+newsletterPreferencyLangPK+", newsletterPreferency :"+newsletterPreferency+" , "+
        "lang :"+lang+" , label :"+label+"]";
    }
}