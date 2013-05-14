package library.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import library.form.BookForm;
import library.model.entity.Book;
import library.model.entity.BookCategory;
import library.model.entity.BookCategoryPK;
import library.model.entity.BookCopy;
import library.model.entity.BookLang;
import library.model.entity.BookLangPK;
import library.model.entity.BookWriter;
import library.model.entity.BookWriterPK;
import library.model.entity.Category;
import library.model.entity.Lang;
import library.model.entity.Writer;
import library.model.repository.BookCategoryRepository;
import library.model.repository.BookCopyRepository;
import library.model.repository.BookLangRepository;
import library.model.repository.BookRepository;
import library.model.repository.BookWriterRepository;
import library.service.BookService;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("bookService")
public class BookServiceImpl implements BookService {
    final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private BookLangRepository bookLangRepository;
    @Autowired
    private BookWriterRepository bookWriterRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    // @Transactional(readOnly = false,  noRollbackFor={PersistenceException.class}, propagation = Propagation.REQUIRES_NEW)
    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'BOOK_ADD')")
    public Book save(BookForm bookForm) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        // first, create a new Book instance
        Book book = new Book();
        try {
            book.setAlias(bookForm.getBook().getAlias());
            book.setAddedDate(new Date());
            book.setUpdatedDate(null);
            book = bookRepository.save(book);

            // second, add copies
            if (bookForm.getCopies() != null) {
                for (BookCopy copy : bookForm.getCopies()) {
                    copy.setBook(book);
                    bookCopyRepository.save(copy);
                }
            }
            // third, add translations
            if (bookForm.getTranslations() != null) {
                for (Long key : bookForm.getTranslations().keySet()) {
                    Lang lang = new Lang();
                    lang.setId(key);
                    Map<String, Object> infos = bookForm.getTranslations().get(key);
                    for (String infosKey : infos.keySet()) {
                        if (infos.get(infosKey) != null && !((String)infos.get(infosKey)).equals("")) {
                            BookLangPK bookLangPK = new BookLangPK();
                            bookLangPK.setIdBook(book.getId());
                            bookLangPK.setIdLang(lang.getId());
                            bookLangPK.setType(infosKey);
                            BookLang bookLang = new BookLang();
                            bookLang.setValue((String)infos.get(infosKey));
                            bookLang.setBookLangPK(bookLangPK);
                            bookLangRepository.save(bookLang);
                        }
                    }
                }
            }
            // fourth, insert writers
            for (Writer writer : bookForm.getWritersChecked()) {
                BookWriterPK bookWriterPK = new BookWriterPK();
                bookWriterPK.setIdWriter(writer.getId());
                bookWriterPK.setIdBook(book.getId());
                BookWriter bookWriter = new BookWriter();
                bookWriter.setBookWriterPK(bookWriterPK);
                bookWriterRepository.save(bookWriter);
            }
            // finally, add categories
            for (Category category : bookForm.getCategoriesChecked()) {
                BookCategoryPK bookCategoryPK = new BookCategoryPK();
                bookCategoryPK.setIdCategory(category.getId());
                bookCategoryPK.setIdBook(book.getId());
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBookCategoryPK(bookCategoryPK);
                bookCategoryRepository.save(bookCategory);
            }
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving Book", e);
            book = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return book;
    }

    @Override
@Cacheable("allBooks")
    public Page<Book> findAll(int page, int size) {
        Pageable pageable = new PageRequest(page, size);
        return bookRepository.findAll(pageable);
    }

    @Override
@Cache(region = "allBooks", usage = CacheConcurrencyStrategy.READ_WRITE)
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Page<Book> getAllBooks(int page, int perPage, Lang lang) {
        Pageable pageable = new PageRequest(page, perPage);
        return bookRepository.getAllBooks(lang, "titl", pageable);
    }
    
    @Override
    public List<Book> getByIdAndLang(long id, Lang lang) {
        return bookRepository.getByIdAndLang(id, lang);
    }
}