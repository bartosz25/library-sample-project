package library.validator.hibernate;

// Trouvé ici pour pouvoir tester la validation : http://stackoverflow.com/questions/4613055/hibernate-unique-key-validation
// TODO : voir ce que veut dire         isAssignableFrom() : if(EntityManagerAwareValidator.class.isAssignableFrom(key)) {
// dans le lien ci-dessus

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

public class TestConstraintValidatorFactory implements ConstraintValidatorFactory {
    private EntityManagerFactory entityManagerFactory;

    public TestConstraintValidatorFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        T instance = null;

        try {
            instance = key.newInstance();
        } catch (Exception e) {
            // could not instantiate class
            e.printStackTrace();
        }

        if (EntityManagerAwareValidator.class.isAssignableFrom(key)) {
            EntityManagerAwareValidator validator = (EntityManagerAwareValidator) instance;
            validator.setEmf(entityManagerFactory);
        }

        return instance;
    }
}