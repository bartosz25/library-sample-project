package com.example.library.service;

import java.util.Date;

import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Booking;
import com.example.library.model.entity.Subscriber;

public interface BookingService {
    public boolean hasBooking(BookCopy bookCopy, Date dateFrom, Date dateTo);
    public Booking bookBookCopy(Booking booking) throws Exception;
    public boolean alreadyBooked(BookCopy bookCopy, Subscriber subscriber);
    // public List<Booking> findById();
    // public Booking save(Booking booking);
    // public void delete(Booking booking);
}