package com.example.library.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.annotation.LocaleLang;
import com.example.library.annotation.LoggedUser;
import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Booking;
import com.example.library.model.entity.Lang;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.BookCopyRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.security.CSRFProtector;
import com.example.library.service.BookingService;
import com.example.library.service.SubscriberService;
import com.example.library.validator.form.BookingValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO : 
 * - vérifier si l'utilisateur possède un abonnement valable
 */
@RequestMapping("/booking")
@Controller
public class BookingController extends MainController {
    final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final String addBinding = "org.springframework.validation.BindingResult.booking";
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CSRFProtector csrfProtector;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/do/{copy}", method = RequestMethod.GET)
    public String doBooking(@ModelAttribute("booking") Booking booking, @PathVariable long copy, 
    @LoggedUser AuthenticationFrontendUserDetails user, Model layout, HttpServletRequest request,
    @LocaleLang Lang lang) {
        Map<String, Object> layoutMap = layout.asMap();
        if (layoutMap.containsKey("errors")) {
            layout.addAttribute(addBinding, layoutMap.get("errors"));
        }
        try {
            csrfProtector.setIntention("booking-do");
            booking.setToken(csrfProtector.constructToken(request.getSession()));
            booking.setAction(csrfProtector.getIntention());
            logger.info("Generated token " + booking.getToken());
        } catch (Exception e) {
            logger.error("An exception occured on creating CSRF token", e);
            return "redirect:/generalError";
        }
        layout.addAttribute("communs", getViewCommunMap(lang));
        return "doBooking";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/do/{copy}", method = RequestMethod.POST)
    public String doBookingHandle(@ModelAttribute("booking") Booking booking, @PathVariable long copy, 
    @LoggedUser AuthenticationFrontendUserDetails user, RedirectAttributes redAtt, HttpServletRequest request) {    
        logger.info("============> Received booking POST data " + booking);
        Subscriber subscriber = conversionService.convert(user, Subscriber.class);
// subscriberService.getById(user.getId());
        booking.setSubscriber(subscriber);
        BookCopy bookCopy = bookCopyRepository.findOne(copy);
        booking.setBookCopy(bookCopy);
        logger.info("==========> found book Copy " + bookCopy);
        // make some validation before and add property editor for date field
        DataBinder binder = new DataBinder(booking);
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true);
        binder.registerCustomEditor(Date.class, customDateEditor);
        binder.setValidator(new BookingValidator(bookingService, request, csrfProtector));
        binder.validate();
        BindingResult results = binder.getBindingResult();
        logger.info("==================> After BookForm validation " + results);
        if (results.hasErrors()) {
            redAtt.addFlashAttribute("error", true);
            redAtt.addFlashAttribute("booking", booking);
            redAtt.addFlashAttribute("errors", results);
            logger.info("errors found = " + booking);
        } else {
            try {
                bookingService.bookBookCopy(booking);
                redAtt.addFlashAttribute("success", true);
            } catch (Exception e) {
                results.addError(getExceptionError("booking"));
                redAtt.addFlashAttribute("error", true);
                redAtt.addFlashAttribute("booking", booking);
                redAtt.addFlashAttribute("errors", results);
            }
        }
        return "redirect:/booking/do/"+copy;
    }
}