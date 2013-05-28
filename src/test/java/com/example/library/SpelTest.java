package com.example.library.test;

import java.util.ArrayList;
import java.util.List;

import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Subscriber;
import com.example.library.tools.ImageTool;
import com.example.library.tools.MailerTool;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelTest extends AbstractControllerTest {
    private static Subscriber subscriber, subscriberExtra;
    private static StandardEvaluationContext context;
    private static List<Subscriber> subscribersList;
    public boolean globalIsConfirmed = false;
    @Autowired
    private BeanFactory beanFactory;
    
    @BeforeClass
    public static void initParticipants() {
        subscriber = new Subscriber();
        subscriber.setId(1l);
        subscriber.setLogin("testLogin");
        subscriber.setBookingNb(4); // 4 books already borrowed
        subscriber.setConfirmed(0); // not confirmed user

        subscriberExtra = new Subscriber();
        subscriberExtra.setId(99l);
        subscriberExtra.setLogin("extraSubscriber");
        subscriberExtra.setBookingNb(14); // 14 books already borrowed
        subscriberExtra.setConfirmed(1); // confirmed user
        
        subscribersList = new ArrayList<Subscriber>();
        subscribersList.add(subscriber);
        subscribersList.add(subscriberExtra);
        
        context = new StandardEvaluationContext();
        context.setVariable("subscriber", subscriber);
        context.setVariable("subscriberExtra", subscriberExtra);
        context.setVariable("subscribersList", subscribersList);
        
        try {
            context.registerFunction("returnTesterString", Tester.class.getDeclaredMethod("returnTesterString", new Class[] {}));
        } catch (Exception e) {
            System.out.println("An error occured on registering function returnTesterString : " + e.getMessage());
        }
    }
    
    @Test
    public void expressionsTests() {
        ExpressionParser parser = new SpelExpressionParser(); 
        
        String test = parser.parseExpression("'Test'").getValue(String.class);
        System.out.println("Found test String : " + test);

        boolean isTest = parser.parseExpression("true").getValue(Boolean.class);
        System.out.println("Found isTest boolean : " + isTest); 

        Class subscriberClass = parser.parseExpression("T(com.example.library.model.entity.Subscriber)").getValue(Class.class);
        System.out.println("Found subscriberClass class : " + subscriberClass); 
        System.out.println("subscriberClass is equals to Subscriber.class ? " + subscriberClass.equals(Subscriber.class)); 
        System.out.println("subscriberClass is equals to Admin.class ? " + subscriberClass.equals(Admin.class)); 
        
        String newLogin = parser.parseExpression("'-'.concat(#subscriber.login)").getValue(context, String.class);
        System.out.println("Found newLogin : " + newLogin);
        
        boolean isConfirmed = parser.parseExpression("#subscriber.confirmed == T(com.example.library.model.entity.Subscriber).IS_CONFIRMED").getValue(context, Boolean.class);
        System.out.println("If subscriber is confirmed ? " + isConfirmed);
        boolean isNotConfirmed = parser.parseExpression("#subscriber.confirmed == T(com.example.library.model.entity.Subscriber).IS_NOT_CONFIRMED").getValue(context, Boolean.class);
        System.out.println("If subscriber is not confirmed ? " + isNotConfirmed);
        
        int result = parser.parseExpression("10 - #subscriber.getBookingNb()").getValue(context, Integer.class);
        System.out.println("Subscriber's can borrow " + result + " books");

		SpelTest spelTest = new SpelTest();
        StandardEvaluationContext contextSpel = new StandardEvaluationContext(spelTest);
        parser.parseExpression("globalIsConfirmed").setValue(contextSpel, true);
        System.out.println("spelTest.globalIsConfirmed is : " + spelTest.globalIsConfirmed);

        String newName = parser.parseExpression("getNewName('Mock')").getValue(contextSpel, String.class);
        System.out.println("Found newName " + newName);
        
        Subscriber subscriberInventor = parser.parseExpression("new com.example.library.model.entity.Subscriber()").getValue(Subscriber.class);
        System.out.println("subscriberInvetor is : " + subscriberInventor);

        BeanResolver beanResolver = new BeanFactoryResolver(beanFactory);
        context.setBeanResolver(beanResolver);
        Object imageToolBean = parser.parseExpression("@imageTool").getValue(context);
        System.out.println("imageToolBean is instance of ImageTool ? " + (imageToolBean instanceof ImageTool));
        System.out.println("imageToolBean is instance of MailerTool ? " + (imageToolBean instanceof MailerTool));
        
        List<String> letters = (List<String>) parser.parseExpression("{'a', 'b', 'c'}").getValue(context, List.class);
        System.out.println("Found letters : " + letters);
        
        boolean isConfirmedIfElse = parser.parseExpression("#subscriber.confirmed == 1 ? true : false").getValue(context, Boolean.class);
        System.out.println("isConfirmedIfElse is " + isConfirmedIfElse);

        Subscriber subscriberContext = new Subscriber();
        subscriberContext.setLogin("test09");
        System.out.println("subscriberContext.login before modifierContext call is : " + subscriberContext.getLogin());
        StandardEvaluationContext modifierContext = new StandardEvaluationContext(subscriberContext);
        modifierContext.setVariable("login", "test3");
        parser.parseExpression("login = #login").getValue(modifierContext);
        System.out.println("subscriberContext.login after modifierContext call is : " + subscriberContext.getLogin());

        System.out.println("subscribersList contains : " + subscribersList);
        List<Subscriber> subscribersTest = (List<Subscriber>) parser.parseExpression("#subscribersList.?[login == 'testLogin']").getValue(context);
        System.out.println("Found subscribers with testLogin : " + subscribersTest);
        
        List<String> logins = (List<String>) parser.parseExpression("#subscribersList.![login]").getValue(context, List.class);
        System.out.println("Found subscriber's logins : " + logins);

        String subscriberSentence = parser.parseExpression("TestEntity sentence is : #{T(com.example.library.test.model.TestEntity).getTestString()}", new TemplateParserContext()).getValue(String.class);
        System.out.println("subscriberSentence is : \"" + subscriberSentence+ "\"");
        
        String testerString = parser.parseExpression("#returnTesterString()").getValue(context, String.class);
        System.out.println("testerString is " + testerString);
    }

    public String getNewName(String name) {
        return "-old"+name;
    }
}


abstract class Tester {
    public static String returnTesterString() {
        return "testerString";
    }
}