package library.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO : 
 * - peut-être rajouter l'année de publication
 * - gérer les versions linguistiques (exemplaire en FR, EN etc.)
 * - voir si l'on peut utiliser mappedBy="attribute" + une requête contenant une condition supplémentaire
 */
@Entity
@Table(name="book")
public class Book extends ParentEntity implements Serializable {
    final Logger logger = LoggerFactory.getLogger(Book.class);
    private Long id;
    private Date addedDate;
    private Date updatedDate;
    private String alias;
    private List<BookCopy> bookCopies;
    private List<BookCategory> bookCategories;
    private List<BookLang> bookLangs;
    /**
     * This is attribute with textual informations (title, description, resume) about the book. The Map is 
     * composed with database code as a key and database value as a value
     */
    private Map<String, Object> bookData;
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_bo")
    public Long getId() {
        return id;
    }

    @Temporal(DATE)
    @NotNull(message = "{error.book.addedDateEmpty}")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @Column(name="added_bo")
    public Date getAddedDate() {
        return addedDate;
    }

    @Column(name="updated_bo")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public Date getUpdatedDate() {
        return updatedDate;
    }

    @NotEmpty(message = "{error.book.aliasEmpty}")
    @Size(min = 1, max = 200, message = "{error.book.aliasSize}")
    @Column(name="alias_bo")
    public String getAlias() {
        return alias;
    }

    @OneToMany(mappedBy = "book")
    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    @OneToMany(mappedBy = "book")
    public List<BookCategory> getBookCategories() {
        return bookCategories;
    }

    @OneToMany(mappedBy = "book")
    public List<BookLang> getBookLangs() {
        return bookLangs;
    }

    @Transient
    public Map<String, Object> getTranslationsByLang(Lang lang) {
        if (bookData == null) bookData = new HashMap<String, Object>();
        if (bookLangs == null) bookLangs = getBookLangs();
        for (BookLang bookLang : bookLangs) {
            logger.info("Found BookLang instance " + bookLang);
            if (bookLang.getLang().getId() == lang.getId()) {
                bookData.put(bookLang.getBookLangPK().getType(), bookLang.getValue());
            }
        }
        return bookData;
    }

    @Transient
    public Map<String, Object> getBookData() {
        return bookData;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAddedDate(Date addedDate) {
        if (addedDate == null) addedDate = new Date();
        this.addedDate = addedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setBookCopies(List<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }

    public void setBookCategories(List<BookCategory> bookCategories) {
        this.bookCategories = bookCategories;
    }

    public void setBookLangs(List<BookLang> bookLangs) {
        this.bookLangs = bookLangs;
    }

    public String toString() {
        return "Book [id = "+id+", added date="+addedDate+", updated date = "+updatedDate+"]";
    }
}