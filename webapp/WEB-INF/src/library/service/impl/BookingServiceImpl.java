package library.service.impl;

import java.util.Date;
import java.util.List;

import library.model.entity.BookCopy;
import library.model.entity.Booking;
import library.model.entity.Subscriber;
import library.model.repository.BookingRepository;
import library.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {
    final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    @Autowired
    private BookingRepository bookingRepository;
    
    @Override
    public boolean hasBooking(BookCopy bookCopy, Date dateFrom, Date dateTo) {
        List<Booking> bookings = bookingRepository.findBookingsBetween(bookCopy, dateFrom, dateTo);
        logger.info("Found bookings " + bookings + " for between date " + dateFrom + " and " + dateTo);
        logger.info("=======>"+bookings.size());
        return (bookings.size() > 0);
    }
    
    @Override
    public boolean alreadyBooked(BookCopy bookCopy, Subscriber subscriber) {
        List<Booking> bookings = bookingRepository.alreadyBooked(bookCopy, subscriber);
        logger.info("Found bookings " + bookings + " for Subscriber " + subscriber);
        logger.info("=======>"+bookings.size());
        return (bookings.size() > 0);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Booking bookBookCopy(Booking booking) throws Exception {
        try {
            booking.setBookingPK(booking.getBookCopy(), booking.getSubscriber());
            booking.setState(Booking.STATE_ACTIVE);
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            booking = null;
            logger.error("An exception occured on booking a book copy", e);
            throw new Exception(e);
        }
        return booking;
    }
}