package com.example.library.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsEqualLocallyValidator implements ConstraintValidator<IsEqualLocally, Object> {
    private String firstValue;
    private String secondValue;
    final Logger logger = LoggerFactory.getLogger(IsEqualLocallyValidator.class);

    @Override
    public void initialize(IsEqualLocally constraintAnnotation) {
        firstValue = constraintAnnotation.firstValue();
        secondValue = constraintAnnotation.secondValue();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // logger.info("valueM = " + value);
        // logger.info("value = " + value);
        try {
            Object firstObj = BeanUtils.getProperty(value, firstValue);
            Object secondObj = BeanUtils.getProperty(value, secondValue);
            // logger.info("IsEqualLocallyValidator : got firstObj " + firstObj);
            // logger.info("IsEqualLocallyValidator : got secondObj " + secondObj);
            return (firstObj != null && secondObj != null && firstObj.equals(secondObj));
        }
        catch (Exception e) {
            logger.error("An exception occured on IsEqualLocallyValidator when validating "+firstValue+" and " + secondValue, e);
        }
        return false;
    }
}