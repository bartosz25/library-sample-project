package com.example.library.service;

import com.example.library.model.entity.BookCopy;

public interface BookCopyService {
    // public List<BookCopy> findById();
    public BookCopy getOneNotBorrowed(long id);
    // public BookCopy save(BookCopy bookCopy);
    // public void delete(BookCopy bookCopy);
}