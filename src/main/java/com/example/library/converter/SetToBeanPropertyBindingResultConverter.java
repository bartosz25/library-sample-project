package com.example.library.converter;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;


public class SetToBeanPropertyBindingResultConverter implements Converter<Set<ConstraintViolation<Object>>, BeanPropertyBindingResult> {
    final Logger logger = LoggerFactory.getLogger(SetToBeanPropertyBindingResultConverter.class);
    
    public BeanPropertyBindingResult convert(Set<ConstraintViolation<Object>> violations) {
        logger.info("Data to convert :"+violations);
        System.out.println("Data to convert :"+violations); // TODO : supprimer une fois Logger implémenté dans test unitaires
        BeanPropertyBindingResult bindingResult = null; 
        if (violations.size() > 0) {
            String className = "";
            for (ConstraintViolation<Object> violation : violations) {
                if (bindingResult == null)  {
                    className = violation.getRootBeanClass().getName();
                    bindingResult = new BeanPropertyBindingResult(violation.getRootBean(), className);
                }
                String propertyPath = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                // Add JSR-303 errors to BindingResult
                bindingResult.addError(new FieldError(className, propertyPath, message));
            }
        }
        return bindingResult;
    }
}