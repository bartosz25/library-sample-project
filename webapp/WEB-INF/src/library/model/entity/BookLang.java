package library.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * TODO :
 * - remplacer la liste des entrées par quelque chose plus dynamique
 * - rajouter les validateurs dynamiques pour les entrées
 */

@Entity
@Table(name = "book_lang")
// http://ehcache.org/documentation/2.5/integrations/hibernate
// TODO : aborder la question du 2nd cache level
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BookLang extends ParentEntity implements Serializable {
    private BookLangPK bookLangPK;
    private String value;
    private Book book;
    private List<BookCategory> bookCategories;
    private Lang lang;
    private Map<String, String> labelCodes = new HashMap<String, String>() {{
        put("titl", "book.title");
        put("lead", "book.lead");
        put("desc", "book.description");
        put("revi", "book.review");
    }};
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idBook", column = @Column(name = "book_id_bo", nullable = false)),
        @AttributeOverride(name = "idLang", column = @Column(name = "lang_id_la", nullable = false)),
        @AttributeOverride(name = "type", column = @Column(name = "type_bl", nullable = false)) })
    public BookLangPK getBookLangPK() {
        return bookLangPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id_la", nullable = false, insertable = false, updatable = false)
    public Lang getLang() {
        return lang;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id_bo", nullable = false, insertable = false, updatable = false)
    public Book getBook() {
        return book;
    }
    // // @OneToMany(mappedBy = "bookCategoryPK.idBook") 
    // @ManyToOne(mappedBy = "bookCategoryPK.idBook") 
    // @JoinColumn(name = "book_id_bo", insertable=false, updatable=false)  
    // public List<BookCategory> getBookCategories()
    // {
        // return bookCategories;
    // }
    // @ManyToMany
    // @JoinTable(name = "book_category",
        // joinColumns = 
            // @JoinColumn(name = "book_id_bo", referencedColumnName = "book_id_bo", insertable=false, updatable=false),
        // inverseJoinColumns = 
            // @JoinColumn(name = "book_id_bo", referencedColumnName = "book_id_bo", insertable=false, updatable=false)
    // )
    // public List<BookCategory> getBookCategories()
    // {
        // return bookCategories;
    // }
    @Column(name = "value_bl")
    public String getValue() {
        return value;
    }

    @Transient
    public Map<String, String> getLabelCodes() {
        return labelCodes;
    }

    public void setBookLangPK(Lang lang, Book book, String type) {
        this.bookLangPK = new BookLangPK();
        this.bookLangPK.setIdLang(lang.getId());
        this.bookLangPK.setIdBook(book.getId());
        this.bookLangPK.setType(type);
    }

    public void setBookLangPK(BookLangPK bookLangPK) {
        this.bookLangPK = bookLangPK;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLabelCodes(Map<String, String> labelCodes) {
        this.labelCodes = labelCodes;
    }
    // public void setBookCategories(List<BookCategory> bookCategories)
    // {
        // this.bookCategories = bookCategories;
    // }
    public String toString() {
        return "BookLang : lang ["+lang+"], book ["+book+"], type ["+bookLangPK.getType()+"], value ["+value+"]";        
    }
}