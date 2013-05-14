package library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BookLangPK implements Serializable {
    private long idBook;
    private long idLang;
    private String type;

    public BookLangPK() {
    }

    // @Column(name = "book_id_bo")
    public long getIdBook() {
	    return idBook;
    }

    // @Column(name = "lang_id_la")
    public long getIdLang() {
	    return idLang;
    }

    // @Column(name = "type_bl")
    public String getType() {
        return type;
    }

    public void setIdBook(long idBook) {
	    this.idBook = idBook;
    }

    public void setIdLang(long idLang) {
	    this.idLang = idLang;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        BookLangPK bookLangPK = (BookLangPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idBook, bookLangPK.getIdBook())
            .append(idLang, bookLangPK.getIdLang())
            .append(type, bookLangPK.getType())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idBook)
            .append(idLang)
            .append(type)
            .toHashCode();
    }
}