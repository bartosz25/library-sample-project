package library.converter;

import library.model.entity.Book;

import org.springframework.core.convert.converter.Converter;

// TODO : probablement à virer
public class BookToStringConverter implements Converter<Book, String> {
    public String convert(Book book)
    {
        return "Book";
    }
}