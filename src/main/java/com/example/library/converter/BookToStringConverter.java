package com.example.library.converter;

import com.example.library.model.entity.Book;

import org.springframework.core.convert.converter.Converter;

// TODO : probablement à virer
public class BookToStringConverter implements Converter<Book, String> {
    public String convert(Book book)
    {
        return "Book";
    }
}