package com.example.library.service;

import com.example.library.model.entity.BookLang;
import com.example.library.model.entity.Lang;

import org.springframework.data.domain.Page;

public interface BookLangService {
    public Page<BookLang> findAllByTitleForLang(int page, int perPage, Lang lang);
    // public BookCopy save(BookCopy bookCopy);
    // public void delete(BookCopy bookCopy);
}