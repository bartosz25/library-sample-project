package library.service;

import java.util.List;

import library.form.BookForm;
import library.model.entity.Book;
import library.model.entity.Lang;

import org.springframework.data.domain.Page;

public interface BookService {
    public Page<Book> findAll(int page, int size);
    // public List<Book> findById();
    public Page<Book> getAllBooks(int page, int perPage, Lang lang);
    public List<Book> getByIdAndLang(long id, Lang lang);
    public Book save(BookForm bookForm) throws Exception;
    // public void delete(Book book);
}