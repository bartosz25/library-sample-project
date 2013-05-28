package com.example.library.service;

import java.util.List;

import com.example.library.form.BookForm;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Lang;

import org.springframework.data.domain.Page;

public interface BookService {
    public Page<Book> findAll(int page, int size);
    // public List<Book> findById();
    public Page<Book> getAllBooks(int page, int perPage, Lang lang);
    public List<Book> getByIdAndLang(long id, Lang lang);
    public Book save(BookForm bookForm) throws Exception;
    // public void delete(Book book);
}