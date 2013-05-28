package com.example.library.model.repository;

import java.util.Date;
import java.util.List;

import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Booking;
import com.example.library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookingRepository  extends CrudRepository<Booking, Long> {
    @Query("SELECT boo FROM Booking boo WHERE boo.bookCopy = :bookCopy AND ((boo.bookingDate BETWEEN :dateFrom AND :dateTo) OR (boo.bookingDate BETWEEN CURDATE() AND :dateTo))")
    public List<Booking> findBookingsBetween(@Param("bookCopy") BookCopy bookCopy, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    @Query("SELECT boo FROM Booking boo WHERE boo.bookCopy = :bookCopy AND boo.subscriber = :subscriber")
    public List<Booking> alreadyBooked(@Param("bookCopy") BookCopy bookCopy, @Param("subscriber") Subscriber subscriber);

}