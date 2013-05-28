package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.BookCategory;
import com.example.library.model.entity.Lang;

public interface BookCategoryService {
    public List<BookCategory> findByCategoryTitle(String name, Lang lang);
    // public BookCopy save(BookCopy bookCopy);
    // public void delete(BookCopy bookCopy);
}