package com.example.library.model.entity;

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
@Table(name = "book_writer")
public class BookWriter extends ParentEntity implements Serializable {
    private BookWriterPK bookWriterPK;
    private Book book;
    private Writer writer;
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "idBook", column = @Column(name = "book_id_bo", nullable = false)),
        @AttributeOverride(name = "idWriter", column = @Column(name = "writer_id_wr", nullable = false)) })
    public BookWriterPK getBookWriterPK() {
        return bookWriterPK;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id_wr", nullable = false, insertable = false, updatable = false)
    public Writer getWriter() {
        return writer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id_bo", nullable = false, insertable = false, updatable = false)
    public Book getBook() {
        return book;
    }

    public void setBookWriterPK(Writer writer, Book book) {
        this.bookWriterPK = new BookWriterPK();
        this.bookWriterPK.setIdWriter(writer.getId());
        this.bookWriterPK.setIdBook(book.getId());
    }

    public void setBookWriterPK(BookWriterPK bookWriterPK) {
        this.bookWriterPK = bookWriterPK;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String toString() {
        return "BookLang : writer ["+writer+"], book ["+book+"]";
    }
}