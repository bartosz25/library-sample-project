package com.example.library.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.library.validator.hibernate.EntityManagerAwareValidator;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueRecordDbCrossedValidator implements ConstraintValidator<UniqueRecordDbCrossed, Object>, EntityManagerAwareValidator {
    @Autowired
    private EntityManagerFactory emf;
    private String uniqueColumn;
    private String oldColumn;
    private String query;
    final Logger logger = LoggerFactory.getLogger(UniqueRecordDbCrossedValidator.class);

    public void initialize(UniqueRecordDbCrossed constraintAnnotation) {
        uniqueColumn = constraintAnnotation.uniqueColumn();
        oldColumn = constraintAnnotation.oldColumn();
        query = constraintAnnotation.query();
    }
    
    @Override
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (emf == null) return false;
        try {
            // first, get both values to check
            Object uniqueColVal = BeanUtils.getProperty(value, uniqueColumn);
            Object oldColVal = BeanUtils.getProperty(value, oldColumn);
            // secondly, construct SQL check query
            EntityManager entityManager = emf.createEntityManager();
            logger.info("===============> Validating using query " + query);
            Query queryInst = entityManager.createQuery(query);
            logger.info("===============> Setting " + uniqueColumn + " = " + uniqueColVal);
            logger.info("===============> Setting " + oldColumn + " = " + oldColVal);
            queryInst.setParameter(uniqueColumn, uniqueColVal);
            queryInst.setParameter(oldColumn, oldColVal);
            List list = queryInst.getResultList();
            logger.info("==============> Generated query " + queryInst);
            logger.info("==============> Found results " + list);
            return (list != null && list.size() == 0);
        } catch(Exception e) {
            logger.error("An exception occured on validating unique record", e);
        }
        return false;
    }
}