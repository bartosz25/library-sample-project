package library.service;

import library.model.entity.BookLang;
import library.model.entity.Lang;

import org.springframework.data.domain.Page;

public interface BookLangService {
    public Page<BookLang> findAllByTitleForLang(int page, int perPage, Lang lang);
    // public BookCopy save(BookCopy bookCopy);
    // public void delete(BookCopy bookCopy);
}