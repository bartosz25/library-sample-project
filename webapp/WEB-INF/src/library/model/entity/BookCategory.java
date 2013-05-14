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
@Table(name = "book_category")
public class BookCategory extends ParentEntity implements Serializable {
    private BookCategoryPK bookCategoryPK;
    private Book book;
    // private List<BookLang> bookLang;
    private Category category;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idBook", column = @Column(name = "book_id_bo", nullable = false)),
        @AttributeOverride(name = "idCategory", column = @Column(name = "category_id_ca", nullable = false))
    })
    public BookCategoryPK getBookCategoryPK() {
        return bookCategoryPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_ca", nullable = false, insertable = false, updatable = false)
    public Category getCategory() {
        return category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id_bo", nullable = false, insertable = false, updatable = false)
    public Book getBook() {
        return book;
    }
    // @ManyToOne
    // @JoinColumn(name = "book_id_bo", insertable=false, updatable=false, nullable=true)  
    // @JoinColumns ({
        // @JoinColumn(name="book_id_bo", insertable = false, updatable = false),
        // @JoinColumn(name = "lang_id_la", insertable = false, updatable = false),
        // @JoinColumn(name = "type_bl", insertable = false, updatable = false)
    // })
    // @ManyToMany(mappedBy = "bookCategories")
    // public List<BookLang> getBookLang()
    // {
        // return bookLang;
    // }
    public void setBook(Book book) {
        this.book = book;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setBookCategoryPK(Book book, Category category) {
        bookCategoryPK = new BookCategoryPK();
        bookCategoryPK.setIdBook(book.getId());
        bookCategoryPK.setIdCategory(category.getId());
    }

    public void setBookCategoryPK(BookCategoryPK bookCategoryPK) {
        this.bookCategoryPK = bookCategoryPK;
    }
    // public void setBookLang(List<BookLang> bookLang)
    // {
        // this.bookLang = bookLang;
    // }
    public String toString() {
        return "BookCategory [book = "+bookCategoryPK.getIdBook()+", category = " + bookCategoryPK.getIdCategory()+"]";
    }
}