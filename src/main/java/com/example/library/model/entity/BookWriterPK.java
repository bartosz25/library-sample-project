package com.example.library.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BookWriterPK implements Serializable {
    private long idBook;
    private long idWriter;

    public BookWriterPK()
    {
    }

    // @Column(name = "book_id_bo")
    public long getIdBook()
    {
	    return idBook;
    }

    // @Column(name = "writer_id_wr")
    public long getIdWriter()
    {
	    return idWriter;
    }

    public void setIdBook(long idBook)
    {
	    this.idBook = idBook;
    }

    public void setIdWriter(long idWriter)
    {
	    this.idWriter = idWriter;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        
        BookWriterPK bookWriterPK = (BookWriterPK) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(idBook, bookWriterPK.getIdBook())
            .append(idWriter, bookWriterPK.getIdWriter())
            .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(idBook)
            .append(idWriter)
            .toHashCode();
    }
}