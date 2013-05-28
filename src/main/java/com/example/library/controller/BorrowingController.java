package com.example.library.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Borrowing;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Subscriber;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.service.BookCopyService;
import com.example.library.service.BookingService;
import com.example.library.service.BorrowingService;
import com.example.library.service.SubscriberService;
import com.example.library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO :
 * - vérifier si pas de réservation pour ce livre (réservation entre le jour de réservation et le jour estimé de retour)
 * - seuls les utilisateurs ayant un abonnement correspondant à la période de réservation, peuvent effectuer une réservation
 */
@RequestMapping("/borrowing")
@Controller
public class BorrowingController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BorrowingController.class);
    @Autowired
    private BookCopyService bookCopyService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BorrowingService borrowingService;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/do/{copy}", method = RequestMethod.GET)
    public String doBorrow(@PathVariable long copy, @LoggedUser AuthenticationFrontendUserDetails user, 
    Model layout, HttpServletRequest request, @LocaleLang Lang lang) {
        // Lang lang = new Lang();
        // lang.setId(1l);
        BookCopy bookCopy = bookCopyService.getOneNotBorrowed(copy);
        boolean canBorrow = true;
        logger.info("Found Auth " + user);
        logger.info("Found bookCopy " + bookCopy);
        // User user = SecurityContextHolder.getContext().getAuthentication()
        if (Subscriber.canBorrow(user.getBookingNb()) && bookCopy != null) {
            Borrowing borrowing = new Borrowing();
            borrowing.setDateFrom(new Date());
            borrowing.setDateTo(borrowing.getDateFrom(), true);
            borrowing.setAction("borrowing-do");
            borrowing.setToken((String) request.getAttribute("_token"));
            // check if no booking was done for this book
            
            CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
            csrfConstraintValidator.setRequest(request);
            csrfConstraintValidator.setCSRFProtector(csrfProtector);
            boolean csrfIsValid = csrfConstraintValidator.isValid(borrowing, null);
            
            if (!bookingService.hasBooking(bookCopy, borrowing.getDateFrom(), borrowing.getDateTo()) && csrfIsValid) {
                borrowing = borrowingService.borrowBookCopy(borrowing, bookCopy, user);
                layout.addAttribute("bookBorrowed", true);
                logger.info("Borrowing object " + borrowing);
                if (borrowing.getSubscriber().getBookingNb() == 0) {
                    logger.info("===============> Can't book anymore " + borrowing.getSubscriber());
                    layout.addAttribute("cantBookAnymore", true);
                }
            } else if(!csrfIsValid) {
                logger.info("CSRF token is invalid");
                return "redirect:/tokenInvalid";
            }
        } else {
            canBorrow = false;
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        layout.addAttribute("canBorrow", canBorrow);
        return "doBorrow";
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/return/{id}", method = RequestMethod.GET)
    public String returnBorrowing(@PathVariable long id, @LoggedUser AuthenticationFrontendUserDetails 
    user, Model layout, HttpServletRequest request, @LocaleLang Lang lang) {   
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
// subscriberService.getById(user.getId());
        Borrowing borrowing = borrowingService.getBorrowingByIdAndUser(id, subscriber);
        if (borrowing != null) {
            borrowing.setAction("borrowing-return");
            borrowing.setToken((String) request.getAttribute("_token"));
            CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
            csrfConstraintValidator.setRequest(request);
            csrfConstraintValidator.setCSRFProtector(csrfProtector);
            if (csrfConstraintValidator.isValid(borrowing, null)) {
                layout.addAttribute("result", borrowingService.returnBookCopy(borrowing, subscriber, user));
            } else {
                logger.info("CSRF Token is invalid");
                return "redirect:/tokenInvalid";
            }
        } else {
            layout.addAttribute("notFound", true);
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "returnBorrowing";
    }
}