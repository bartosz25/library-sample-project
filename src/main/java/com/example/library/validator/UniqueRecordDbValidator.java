package com.example.library.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.library.validator.hibernate.EntityManagerAwareValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueRecordDbValidator implements ConstraintValidator<UniqueRecordDb, String>, EntityManagerAwareValidator {
    @Autowired
    private EntityManagerFactory emf;
    private String column;
    private String query;
    private String parameter;
    final Logger logger = LoggerFactory.getLogger(UniqueRecordDbValidator.class);

    public void initialize(UniqueRecordDb constraintAnnotation) {
        column = constraintAnnotation.column();
        query = constraintAnnotation.query();
        parameter = constraintAnnotation.parameter();
    }
    
    @Override
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (emf == null) return false;
        EntityManager entityManager = emf.createEntityManager();
        logger.info("===============> Validating using query " + query);
        Query queryInst = entityManager.createQuery(query);
        queryInst.setParameter(parameter, value);
        List list = queryInst.getResultList();
        logger.info("===============> LIst sie is " + list.size());
        return (list != null && list.size() == 0);
    }
}