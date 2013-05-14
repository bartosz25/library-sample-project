package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BookCategoryPK implements Serializable {
    private long idBook;
    private long idCategory;

    public BookCategoryPK() {
    }

    // @Column(name = "book_id_bo")
    public long getIdBook() {
	    return idBook;
    }

    // @Column(name = "category_id_ca")
    public long getIdCategory() {
	    return idCategory;
    }

    public void setIdBook(long idBook) {
	    this.idBook = idBook;
    }

    public void setIdCategory(long idCategory) {
	    this.idCategory = idCategory;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        BookCategoryPK bookCategoryPK = (BookCategoryPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idBook, bookCategoryPK.getIdBook())
            .append(idCategory, bookCategoryPK.getIdCategory())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idBook)
            .append(idCategory)
            .toHashCode();
    }
}