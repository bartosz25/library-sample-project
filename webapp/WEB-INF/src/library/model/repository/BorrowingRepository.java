package library.model.repository;

import java.util.Date;
import java.util.List;

import library.model.entity.Borrowing;
import library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BorrowingRepository  extends CrudRepository<Borrowing, Long> {
    @Query("SELECT bo FROM Borrowing bo WHERE bo.id = :id AND bo.subscriber = :subscriber")
    public Borrowing getBorrowingByIdAndUser(@Param("id") long id, @Param("subscriber") Subscriber subscriber);

    @Query("SELECT bo, bc, s FROM Borrowing bo JOIN bo.bookCopy bc JOIN bo.subscriber s WHERE bo.dateTo <= CURDATE() AND bc.state = :bookedState AND bo.penalized = :noPenalized AND (bo.lastActionDate <= CURDATE() OR bo.lastActionDate = null)")
    public List<Object[]> getDelayedBooks(@Param("bookedState") int bookedState, @Param("noPenalized") int noPenalized);

    @Query("SELECT bo, bc, s, bl FROM Borrowing bo JOIN bo.bookCopy bc JOIN bo.subscriber s JOIN bc.book b JOIN b.bookLangs bl WHERE bl.bookLangPK.idLang = :langId")
    public List<Object[]> getReportByLangId(@Param("langId") long langId);

    @Query("SELECT COUNT(bo.id) FROM Borrowing bo WHERE bo.subscriber = :subscriber")
    public long countBySubscriber(@Param("subscriber") Subscriber subscriber);
    
    @Query("SELECT COUNT(bo.id) FROM Borrowing bo WHERE bo.subscriber = :subscriber AND bo.dateFrom >= :from AND bo.dateTo <= :to")
    public long countBySubscriberAndDates(@Param("subscriber") Subscriber subscriber, @Param("from") Date from, @Param("to") Date to);
}