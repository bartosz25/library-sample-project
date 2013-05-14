package library.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import library.model.entity.BookCopy;
import library.model.entity.Borrowing;
import library.model.entity.Subscriber;

import org.springframework.security.core.userdetails.User;

public interface BorrowingService {
    // public List<Borrowing> findById();
    // public Borrowing save(Borrowing borrowing);
    // public void delete(Borrowing borrowing);
    public Borrowing borrowBookCopy(Borrowing borrowing, BookCopy bookCopy, User user);
    public Borrowing getBorrowingByIdAndUser(long borrowingId, Subscriber subscriber);
    public boolean returnBookCopy(Borrowing borrowing, Subscriber subscriber, User user);
    public List<Object[]> getDelayedBooks();
    public void checkDelayed();
    public Map<Long, Map<String, Object>> getReportByLangId(long langId);
    public long countBorrowedBySubscriber(Subscriber subscriber, Date from, Date to);
}