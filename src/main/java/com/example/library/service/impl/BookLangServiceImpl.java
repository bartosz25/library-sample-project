package com.example.library.service.impl;

import com.example.library.model.entity.BookLang;
import com.example.library.model.entity.Lang;
import com.example.library.model.repository.BookLangRepository;
import com.example.library.service.BookLangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("bookLangService")
public class BookLangServiceImpl implements BookLangService {
    final Logger logger = LoggerFactory.getLogger(BookLangServiceImpl.class);
    @Autowired
    private BookLangRepository bookLangRepository;

    @Override
    public Page<BookLang> findAllByTitleForLang(int page, int perPage, Lang lang) {
        Pageable pageable = new PageRequest(page, perPage);
        return bookLangRepository.findAllForLangAndType(lang, "titl", pageable);
    }
}