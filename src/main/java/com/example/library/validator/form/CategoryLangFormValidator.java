package com.example.library.validator.form;

import javax.servlet.http.HttpServletRequest;

import com.example.library.form.CategoryLangForm;
import com.example.library.model.entity.CategoryLang;
import com.example.library.security.CSRFProtector;
import com.example.library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CategoryLangFormValidator implements Validator  {
    private static final int MAX_LENGTH = 255;
    final Logger logger = LoggerFactory.getLogger(CategoryLangFormValidator.class);
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;  
    
    public CategoryLangFormValidator(CSRFProtector csrfProtector, HttpServletRequest request) {
        this.csrfProtector = csrfProtector;
        this.request = request;
    }
    
    public boolean supports(Class clazz) {
        return CategoryLangForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        CategoryLangForm categoryLangForm = (CategoryLangForm) obj;
        int i = 0;
        for (CategoryLang categoryLang : categoryLangForm.getCategoryLang()) {
            if (categoryLang.getName() != null && !categoryLang.getName().equals("") && categoryLang.getName().length() > MAX_LENGTH) {
                errors.rejectValue(
                    "categoryLang["+i+"].name",
                    "error.categoryLangForm.nameLength",
                    new Object[] {MAX_LENGTH},
                    "The password max length is " + MAX_LENGTH
                );
            }
            i++;
        }
        
        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(categoryLangForm, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }
}