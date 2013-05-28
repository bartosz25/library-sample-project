package com.example.library.form;

import java.util.List;
import java.util.Map;

import com.example.library.model.entity.Book;
import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Category;
import com.example.library.model.entity.ParentEntity;
import com.example.library.model.entity.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookForm extends ParentEntity {
    final Logger logger = LoggerFactory.getLogger(BookForm.class);
    private Map<Long, Map<String, Object>> translations;
    private List<Category> categories;
    private List<Writer> writers;
    private List<BookCopy> copies;
    private List<Category> categoriesChecked;
    private List<Writer> writersChecked;
    private Book book;
    
    public void setTranslations(Map<Long, Map<String, Object>> translations) {
        this.translations = translations;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }

    public void setCategoriesChecked(List<Category> categoriesChecked) {
        this.categoriesChecked = categoriesChecked;
    }

    public void setWritersChecked(List<Writer> writersChecked) {
        this.writersChecked = writersChecked;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Map<Long, Map<String, Object>> getTranslations() {
        return translations;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public List<Category> getCategoriesChecked() {
        return categoriesChecked;
    }

    public List<Writer> getWritersChecked() {
        return writersChecked;
    }

    public Book getBook() {
        return book;
    }

    public String toString() {
        return "BookForm [translations = "+translations+", categories = "+categories+", "+
        "writers = "+writers+", copies = "+copies+", categoriesChecked = "+categoriesChecked+
        ", writersChecked = "+writersChecked+"]";
    }
}