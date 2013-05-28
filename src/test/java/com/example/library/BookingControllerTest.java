package com.example.library.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import com.example.library.controller.BookingController;
import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Booking;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.BookCopyRepository;
import com.example.library.model.repository.BookingRepository;
import com.example.library.model.repository.SubscriberRepository;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.service.BookingService;
import com.example.library.validator.form.BookingValidator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

// TODO : tests pour 

// @ContextConfiguration(locations={"file:///D:/resin-4.0.32/webapps/ROOT/META-INF/spring/test-config.xml"})
// @RunWith(SpringJUnit4ClassRunner.class)
public class BookingControllerTest extends AbstractControllerTest {    
    private final long bookCopyId = 36l;
    @Autowired
    private BookingController bookingController;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ConversionService conversionService;
    private static AnonymousAuthenticationToken anonymousUser;
    private static AuthenticationFrontendUserDetails frontendUser;
    private static Subscriber existingSubscriber;

    @BeforeClass
    public static void initParticipants() {
        anonymousUser = new AnonymousAuthenticationToken("anonymous", "anonymous", new 
                        ArrayList(Arrays.asList(new GrantedAuthorityImpl("ROLE_ANONYMOUS"))));
        frontendUser = new AuthenticationFrontendUserDetails("bartosz", "bartosz", true, 
                       true, true, true, new ArrayList(Arrays.asList(
                       new GrantedAuthorityImpl("ROLE_USER"))));
        System.out.println("Test launches with anonymousUser :"+anonymousUser);
        System.out.println("Test launches with frontendUser :"+frontendUser);
    }

    @Test
    public void testBookingInvalid() {
        SecurityContextHolder.getContext().setAuthentication(conversionService.convert(frontendUser, UsernamePasswordAuthenticationToken.class));
        Subscriber subscriber = conversionService.convert(frontendUser, Subscriber.class);
        
        BookCopy bookCopy = bookCopyRepository.findOne(bookCopyId);
        
        Calendar tomorrow = Calendar.getInstance();

        Booking bookingDone = new Booking();
        bookingDone.setBookingDate(tomorrow.getTime());
        bookingDone.setAddedDate(new Date());
        bookingDone.setBookCopy(bookCopy);
        bookingDone.setSubscriber(subscriberRepository.loadByUsername("kamil"));
        try {
            bookingDone = bookingService.bookBookCopy(bookingDone);
        } catch (Exception e) {
            System.out.println("An exception occured on booking a book copy : " + e.getMessage());
            bookingDone = null;
        }
        
        // erreur de validation attendue pour la situation où une réserveation est faite pour aujourd'hui (20/02/2013) et le client tente d'effectuer une réservation pour le lendemain (21/02/2013)
        tomorrow.add(Calendar.DATE, 1);
        Booking booking = new Booking();
        booking.setSubscriber(subscriber);
        booking.setBookCopy(bookCopy);
        booking.setBookingDate(tomorrow.getTime());
        booking.setAddedDate(new Date());
        System.out.println("====> Booking " + booking);
        
        DataBinder binder = new DataBinder(booking);
        // TODO : remplacer par les bons objets; pour l'instant comme ça juste pour compiler
        binder.setValidator(new BookingValidator(bookingService, null, null));
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        System.out.println("BindingResult " + bindingResult);
        
        Assert.assertEquals("Invalid validation errors quantity", 1, bindingResult.getErrorCount());  
        // TODO : remplacer le dernier null par REquest
        String viewResult = bookingController.doBookingHandle(booking, bookCopy.getId(), frontendUser, new RedirectAttributesModelMap(), null);
    }

    @Test
    public void testValidBooking() {
        SecurityContextHolder.getContext().setAuthentication(conversionService.convert(frontendUser, UsernamePasswordAuthenticationToken.class));
        Subscriber subscriber = conversionService.convert(frontendUser, Subscriber.class);
        
        BookCopy bookCopy = bookCopyRepository.findOne(bookCopyId);
        List<Booking> initBookings = bookingRepository.alreadyBooked(bookCopy, subscriber);
        int initBookingsNb = 0; 
        if(initBookings != null) initBookingsNb = initBookings.size(); 
        
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Booking booking = new Booking();
        booking.setSubscriber(subscriber);
        booking.setBookCopy(bookCopy);
        booking.setBookingDate(tomorrow.getTime());
        booking.setAddedDate(new Date());
        System.out.println("====> Booking " + booking);
        
        DataBinder binder = new DataBinder(booking);
        // TODO : virer , null, null
        binder.setValidator(new BookingValidator(bookingService, null, null));
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        System.out.println("BindingResult " + bindingResult);
        
        Assert.assertEquals("Invalid validation errors quantity", 0, bindingResult.getErrorCount());  
        // TODO : remplacer le dernier null par REquest
        String viewResult = bookingController.doBookingHandle(booking, bookCopy.getId(), frontendUser, new RedirectAttributesModelMap(), null);
        
        List<Booking> bookings = bookingRepository.alreadyBooked(bookCopy, subscriber);
        Assert.assertNotNull("Booking wasn't inserted correctly", bookings);
        initBookingsNb++;
        Assert.assertEquals("Booking wasn't inserted correctly (size different)", bookings.size(), initBookingsNb);
    }
}