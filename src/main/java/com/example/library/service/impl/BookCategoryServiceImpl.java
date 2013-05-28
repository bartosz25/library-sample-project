package com.example.library.service.impl;

import java.util.List;

import com.example.library.model.entity.BookCategory;
import com.example.library.model.entity.Lang;
import com.example.library.model.repository.BookCategoryRepository;
import com.example.library.service.BookCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bookCategoryService")
public class BookCategoryServiceImpl implements BookCategoryService {
    final Logger logger = LoggerFactory.getLogger(BookCategoryServiceImpl.class);
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Override
    public List<BookCategory> findByCategoryTitle(String name, Lang lang) {
        return bookCategoryRepository.findByCategoryTitle(name, lang);
    }
}