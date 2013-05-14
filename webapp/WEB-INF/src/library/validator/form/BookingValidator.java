package library.validator.form;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import library.model.entity.Booking;
import library.model.entity.Borrowing;
import library.security.CSRFProtector;
import library.service.BookingService;
import library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookingValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(BookingValidator.class);
    private BookingService bookingService;
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;

    public BookingValidator(BookingService bookingService, HttpServletRequest request,CSRFProtector csrfProtector) {
        this.bookingService = bookingService;
        this.request = request;
        this.csrfProtector = csrfProtector;
    }

    public boolean supports(Class clazz) {
        return Booking.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        Booking booking = (Booking) obj;
        if (booking.getBookingDate() == null) {
            errors.rejectValue(
                "bookingDate",
                "error.booking.dateEmpty",
                "Booking date can't be empty"
            );
        } else {
            logger.info("===========> BOOKINGSERVICE IS " + bookingService);
            // booking can't be done at the same day
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);
            Calendar bookingDate = Calendar.getInstance();
            bookingDate.setTime(booking.getBookingDate());
            bookingDate.set(Calendar.AM_PM, tomorrow.get(Calendar.AM_PM));
            bookingDate.set(Calendar.HOUR, tomorrow.get(Calendar.HOUR));
            bookingDate.set(Calendar.HOUR_OF_DAY, tomorrow.get(Calendar.HOUR_OF_DAY));
            bookingDate.set(Calendar.MINUTE, tomorrow.get(Calendar.MINUTE));
            bookingDate.set(Calendar.SECOND, tomorrow.get(Calendar.SECOND));
            bookingDate.set(Calendar.MILLISECOND , tomorrow.get(Calendar.MILLISECOND));
            Borrowing borrowing = new Borrowing();
            borrowing.setDateFrom(booking.getBookingDate());
            borrowing.setDateTo(borrowing.getDateFrom(), true);
            logger.info("===> comparing " + bookingDate + " and " + tomorrow);
            if (bookingDate.before(tomorrow)) {
                errors.rejectValue(
                    "bookingDate",
                    "error.booking.dateTomorrow",
                    "The farest date is tomorrow"
                );
            } else if (bookingService.hasBooking(booking.getBookCopy(), borrowing.getDateFrom(), 
                    borrowing.getDateTo())) {
                        errors.rejectValue(
                            "bookingDate",
                            "error.booking.dateTaken",
                            "Somebody else has already booked the book for this date"
                        );
            }
        }
        if (bookingService.alreadyBooked(booking.getBookCopy(), booking.getSubscriber())) {
            errors.rejectValue(
                "bookCopy",
                "error.booking.alreadyBooked",
                "You can't booked the same book copy twice"
            );
        }
        if (booking.getSubscriber() == null) {
            errors.rejectValue("subscriber", "entity.empty", "Subscriber was not found");
        } else if (booking.getSubscriber() != null && booking.getSubscriber().getBookingNb() == 0) {
            errors.rejectValue("subscriber", "error.booking.maxLimit", "Subscriber can't book anymore");
        }
        if (booking.getBookCopy() == null) {
            errors.rejectValue("bookCopy", "error.booking.bookCopyEmpty", "Book copy was not found");
        }

        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(booking, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }
}