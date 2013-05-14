package library.service.impl;

import library.model.entity.BookCopy;
import library.model.repository.BookCopyRepository;
import library.service.BookCopyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bookCopyService")
public class BookCopyServiceImpl implements BookCopyService {
    final Logger logger = LoggerFactory.getLogger(BookCopyServiceImpl.class);
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public BookCopy getOneNotBorrowed(long id) {
        return bookCopyRepository.getOneNotBorrowed(id, BookCopy.getStateAvailableId());
    }
}