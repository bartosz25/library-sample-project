package library.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import library.controller.SuggestionController;
import library.model.entity.Subscriber;
import library.model.entity.Suggestion;
import library.security.AuthenticationFrontendUserDetails;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

// TODO : a virer
public class SuggestionControllerTest extends AbstractControllerTest {
    @Autowired
    private SuggestionController suggestionController;
    private static AuthenticationFrontendUserDetails frontendUser;
    @Autowired
    private ConversionService conversionService;
    
    @BeforeClass
    public static void initParticipants() {
        frontendUser = new AuthenticationFrontendUserDetails("bartosz", "bartosz", true, 
                       true, true, true, new ArrayList(Arrays.asList(
                       new GrantedAuthorityImpl("ROLE_USER"))));
        System.out.println("Test launches with frontendUser :"+frontendUser);
    }

    @Test
    public void testExceptionCapted() {
	    Subscriber subscriber = new Subscriber();
	    subscriber.setId(10l);
	    subscriber.setLogin("a");
	    
        Suggestion suggestion = new Suggestion();
        suggestion.setSubscriber(subscriber);
        suggestion.setTitle("testeurPass");
        suggestion.setState(Suggestion.STATE_NEW);
        suggestion.setDeliveryDate(new Date());
        System.out.println("Testing suggestion "+suggestion);
        
        BindingResult bindingResult = new BeanPropertyBindingResult(suggestion, "suggestion");
        SecurityContextHolder.getContext().setAuthentication(conversionService.convert(frontendUser, UsernamePasswordAuthenticationToken.class));
        
        String viewResult = suggestionController.newSuggestionHandle(suggestion, bindingResult, frontendUser, new RedirectAttributesModelMap());
        System.out.println("viewResult is " + viewResult);
        
        
    }
}