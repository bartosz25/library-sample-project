package com.example.library.test;

import static org.hamcrest.CoreMatchers.not;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolation;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

import com.example.library.controller.SubscriberController;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.SubscriberRepository;
import com.example.library.resolver.JPATraversableResolver;
import com.example.library.security.AuthenticationFrontendUserDetails;
import com.example.library.service.check.SubscriberRegisterCheck;
import com.example.library.validator.hibernate.TestConstraintValidatorFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

// @ContextConfiguration(locations={"file:///D:/resin-4.0.32/webapps/ROOT/META-INF/spring/test-config.xml"})
// @RunWith(SpringJUnit4ClassRunner.class)
public class SubscriptionControllerTest extends AbstractControllerTest {
    @Autowired
    private SubscriberController subscriberController;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private EntityManagerFactory emf;
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
    public void testRegisterInvalid() {   
        existingSubscriber = subscriberRepository.loadByUsername("kamil");
        System.out.println("Test launches with existingSubscriber :"+existingSubscriber);
        
        long subscriberCount = subscriberRepository.countAllUsers();
        
        // MockHttpServletRequest request = new MockHttpServletRequest("POST", "/register");

        SecurityContextHolder.getContext().setAuthentication(anonymousUser);

        Subscriber subscriber = new Subscriber();
        subscriber.setLogin("test00");
        subscriber.setPassword("testeurPass");
        subscriber.setPasswordRepeated("testeurPass");
        subscriber.setEmail(existingSubscriber.getEmail());
        // subscriber.setEmail("bartkonieczny+35904033@gmail.com");
        System.out.println("Testing subscriber "+subscriber);
        
        TraversableResolver traversableResolver = new JPATraversableResolver();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        ValidatorContext validatorContext = validatorFactory.usingContext();
        validatorContext.traversableResolver(traversableResolver);
        validatorContext.constraintValidatorFactory(new TestConstraintValidatorFactory(emf));
        Validator validatorHibernate = validatorContext.getValidator();
        // Validator validatorHibernate = Validation.buildDefaultValidatorFactory().usingContext().traversableResolver(traversableResolver).getValidator(); 
        Set<ConstraintViolation<Subscriber>> constraintViolations = validatorHibernate.validate(subscriber, SubscriberRegisterCheck.class);
        
        BindingResult bindingResult = conversionService.convert(constraintViolations, BeanPropertyBindingResult.class);
        if(bindingResult == null) bindingResult = new BeanPropertyBindingResult(subscriber, "subscriber");
        System.out.println("BindingResult after conversion " + bindingResult);
        
        Assert.assertEquals("Invalid validation errors quantity", 1, bindingResult.getErrorCount());  
        String viewResult = subscriberController.registerHandle(subscriber, bindingResult, new RedirectAttributesModelMap());

        subscriberCount++;
        Assert.assertThat("Subscriber count is invalid", subscriberCount, not(subscriberRepository.countAllUsers()));
    }
    
    @Test
    public void testRegisterValid() {        
        long subscriberCount = subscriberRepository.countAllUsers();
        
        // MockHttpServletRequest request = new MockHttpServletRequest("POST", "/register");

        SecurityContextHolder.getContext().setAuthentication(anonymousUser);

        Subscriber subscriber = new Subscriber();
        subscriber.setLogin("test00");
        subscriber.setPassword("testeurPass");
        subscriber.setPasswordRepeated("testeurPass");
        subscriber.setEmail("bartkonieczny+35904033@gmail.com");
        System.out.println("Testing subscriber "+subscriber);
        
        TraversableResolver traversableResolver = new JPATraversableResolver();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        ValidatorContext validatorContext = validatorFactory.usingContext();
        validatorContext.traversableResolver(traversableResolver);
        validatorContext.constraintValidatorFactory(new TestConstraintValidatorFactory(emf));
        Validator validatorHibernate = validatorContext.getValidator();
        Set<ConstraintViolation<Subscriber>> constraintViolations = validatorHibernate.validate(subscriber, SubscriberRegisterCheck.class);
        
        BindingResult bindingResult = conversionService.convert(constraintViolations, BeanPropertyBindingResult.class);
        if(bindingResult == null) bindingResult = new BeanPropertyBindingResult(subscriber, "subscriber");
        System.out.println("BindingResult after conversion " + bindingResult);
        
        Assert.assertEquals("Invalid validation errors quantity", 0, bindingResult.getErrorCount());

        String viewResult = subscriberController.registerHandle(subscriber, bindingResult, new RedirectAttributesModelMap());

        subscriberCount++;
        Assert.assertEquals("Subscriber count is invalid", subscriberCount, subscriberRepository.countAllUsers());
    }



/*    @Test
    public void testRegister()
    {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/question/write");

        request.setParameter("passwordRepeated", "Some text");
        request.setParameter("email", "Some text");
 
        
        SecurityContextHolder.getContext().setAuthentication(
        new AnonymousAuthenticationToken("anonymous", "anonymous", new ArrayList(Arrays.asList(new GrantedAuthorityImpl("ROLE_ANONYMOUS")))));
        // pour se déconnecter :     SecurityContextHolder.clearContext();
        System.out.println("Running tests for SubscriberController");
        // http://blog.codeleak.pl/2012/03/how-to-method-level-validation-in.html
        Subscriber subscriber = new Subscriber();
        subscriber.setId(1l);
        Lang lang = new Lang();
        lang.setId(2l);
        
        Question question = new Question();
        Example example = new Example();
example.setTitle("");
example.setContent(null);
question.setTitle("");
question.setContent("");
question.setLang(lang);
question.setDate(new Date());
question.setSubscriber(subscriber);
System.out.println("Validation question  " +example); 

// ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
// javax.validation.Validator validatorHibernate = factory.getValidator();

 TraversableResolver traversableResolver = new JPATraversableResolver();
javax.validation.Validator validatorHibernate = Validation.buildDefaultValidatorFactory().usingContext().traversableResolver(traversableResolver).getValidator(); 

System.out.println("Validation with  " +validatorHibernate);

Set<ConstraintViolation<Example>> constraintViolations = validatorHibernate.validate(example);
        System.out.println("Found constraintViolations " + constraintViolations);

Set<ConstraintViolation<Question>> constraintViolations2 = validatorHibernate.validate(question);
        System.out.println("Found question " + constraintViolations2);

// valider un groupe
Set<ConstraintViolation<Subscriber>> constraintViolations3 = validatorHibernate.validate(subscriber, SubscriberRegisterCheck.class);
        System.out.println("Found question " + constraintViolations3);

        DataBinder binder = new DataBinder(question, "question");
        // WebDataBinder binder = new WebDataBinder(subscriber, "subscriber");
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.validate();
        System.out.println("===> "+binder);	
        System.out.println("===> "+binder.getTarget());	
        System.out.println("===> "+binder.getBindingResult().getAllErrors());	
        System.out.println("===> "+binder.getBindingResult());	
        System.out.println("===> "+validator);	
        BindingResult errors = new BeanPropertyBindingResult(question, "question");
        validator.validate(question, errors);	
        System.out.println("===> "+errors);
        System.out.println("===> "+errors.hasErrors());
        BindingResult bindingResult = binder.getBindingResult(); //new BeanPropertyBindingResult(subscriber, "subscriber");
        
        
        // Set<ConstraintViolation<Subscriber>> constraintViolations = validator.validate(subscriber);
        // System.out.println("Found constraintViolations " + constraintViolations);
        // binder.validate();
// RedirectAttributes redirectAttributes =
// new RedirectAttributesModelMap();
        // Assert.assertEquals(0, binder.getBindingResult().getErrorCount());  
        request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,new FlashMap());
        String viewResult = questionController.handleWrite(question, bindingResult, null, new RedirectAttributesModelMap()); 
        System.out.println("Found result " + viewResult);
    /*    
    // create a new instance of your form object
    YourFormObject formObject = new YourFormObject();

    WebDataBinder binder = new WebDataBinder(formObject);
    binder.setValidator(validator); // use the validator from the context
    binder.bind(new MutablePropertyValues(request.getParameterMap()));

    // validation must be triggered manually
   Â binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());

    // if you want to test if the validation works as expected you can check the binding result
    // eg. if no errors are expected:
    assertEquals(0, binder.getBindingResult().getErrorCount());

    // now call your controller method and store the return type if it matters
    // f. ex. a different view might be returned depending on whether there are validation errros
    String view = yourController.edit(binder.getTarget(), binder.getBindingResult());

    assertEquals("error", view);

    // now you can test the target object, f. ex.:
    assertEquals("Some text", binder.getTarget().getText());        
    */
        
        
  /*  }
*/
}

/**
An Authentication object was not found in the SecurityContext
Il faut définir l'Authentication dans SecurityContext comme suit : 
SecurityContextHolder.getContext().setAuthentication(conversionService.convert(frontendUser, UsernamePasswordAuthenticationToken.class));
        Subscriber subscriber = conversionService.convert(frontendUser, Subscriber.class);

*/

/* Pour les contrôleurs on peut faire comme ça aussi : import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.apress.prospring3.ch19.test.config.ControllerTestConfig;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ControllerTestConfig.class})
@ActiveProfiles("test")
public class AbstractControllerTest {
}
In Listing 19-2, we applied several annotations to the abstract base class. First, the @RunWith
annotation belongs to the JUnit com.example.library (in this chapter, we are using JUnit version 4.10), which indicates
the runner classes used to execute the test case. Within the annotation, Spring’s
SpringJUnit4ClassRunner is provided, which is Spring’s JUnit support class for running test cases within
Spring’s ApplicationContext environment.
Second, the @ContextConfiguration indicates to the Spring JUnit runner on the configuration to be
loaded. Within the annotation, we specified the classes attribute, which indicates that configuration
was defined in the provided Java classes. It’s also possible to load the context from XML files by
providing the locations attribute, but you can’t provide both locations and classes attributes together.
Finally, the @ActiveProfiles annotation is applied, passing in the profile name test as the attribute.
This indicates to Spring that beans belonging to the test profile should be loaded.

Et après 
public class ContactControllerTest extends AbstractControllerTest {
private final List<Contact> contacts = new ArrayList<Contact

Sans dupliquer la configuration
*/