package library.resolver;

import java.lang.annotation.ElementType;

import javax.validation.Path;
import javax.validation.TraversableResolver;

import org.hibernate.Hibernate;

public class JPATraversableResolver implements TraversableResolver {

    @Override
    public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, 
        Path pathToTraversableObject, ElementType elementType) {
        return traversableObject == null || Hibernate.isInitialized(traversableObject);
    }

    @Override
    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, 
        Path pathToTraversableObject, ElementType elementType) {
        return true;
    }
}



/* TODO : pour savoir plus sur cette exception
Exception :
	Caused an ERROR
HV000041: Call to TraversableResolver.isReachable() threw an exception.
javax.validation.ValidationException: HV000041: Call to TraversableResolver.isReachable() threw an exception.
	at org.hibernate.validator.internal.engine.ValidatorImpl.isValidationRequired(ValidatorImpl.java:1230)
	at org.hibernate.validator.internal.engine.ValidatorImpl.validateConstraint(ValidatorImpl.java:438)
	at org.hibernate.validator.internal.engine.ValidatorImpl.validateConstraintsForDefaultGroup(ValidatorImpl.java:387)
	at org.hibernate.validator.internal.engine.ValidatorImpl.validateConstraintsForCurrentGroup(ValidatorImpl.java:351)
	at org.hibernate.validator.internal.engine.ValidatorImpl.validateInContext(ValidatorImpl.java:303)
	at org.hibernate.validator.internal.engine.ValidatorImpl.validate(ValidatorImpl.java:133)
	at org.springframework.validation.beanvalidation.SpringValidatorAdapter.validate(SpringValidatorAdapter.java:89)
	at org.springframework.validation.DataBinder.validate(DataBinder.java:709)
	at library.test.SubscriptionControllerTest.testRegister(SubscriptionControllerTest.java:115)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:74)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:83)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:72)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:231)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:71)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:174)
Caused by: java.lang.NullPointerException
	at javax.persistence.Persistence$1.isLoaded(Persistence.java:93)
	at org.hibernate.validator.internal.engine.resolver.JPATraversableResolver.isReachable(JPATraversableResolver.java:57)
	at org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver.isReachable(DefaultTraversableResolver.java:130)
	at org.hibernate.validator.internal.engine.resolver.SingleThreadCachedTraversableResolver.isReachable(SingleThreadCachedTraversableResolver.java:46)
	at org.hibernate.validator.internal.engine.ValidatorImpl.isValidationRequired(ValidatorImpl.java:1221)



Remède : 
http://j2eestandardsexperience.blogspot.fr/2011/01/jpatraversalresolver-and-bug.html?spref=bl
http://musingsofaprogrammingaddict.blogspot.fr/2010/06/whats-new-in-hibernate-validator-41.html
http://antoniogoncalves.org/2010/03/03/bean-validation-with-jpa-1-0/
https://forum.hibernate.org/viewtopic.php?f=9&t=1002745
http://lists.jboss.org/pipermail/hibernate-issues/2011-February/028526.html
https://hibernate.onjira.com/browse/HV-438

*/