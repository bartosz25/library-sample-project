package library.validator.hibernate;

import javax.persistence.EntityManagerFactory;

public interface EntityManagerAwareValidator {
    void setEmf(EntityManagerFactory emf);
} 