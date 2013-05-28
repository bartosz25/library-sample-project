package com.example.library.validator.form;

import javax.servlet.http.HttpServletRequest;

import com.example.library.form.BackendPenaltyForm;
import com.example.library.security.CSRFProtector;
import com.example.library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BackendPenaltyFormValidator extends PenaltyFormValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(BackendPenaltyFormValidator.class);
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;    

    public BackendPenaltyFormValidator(CSRFProtector csrfProtector, HttpServletRequest request) {
        this.csrfProtector = csrfProtector;
        this.request = request;
    }
    
    public boolean supports(Class clazz) {
        return BackendPenaltyForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        BackendPenaltyForm backendPenaltyForm = (BackendPenaltyForm) obj;
        logger.info("================================> backendPenaltyForm " + backendPenaltyForm);
        if (backendPenaltyForm.getReference() == null || backendPenaltyForm.getReference().equals("")) {
            errors.rejectValue(
                "reference",
                "error.backendPenaltyForm.referenceEmpty",
                "Fill up payment's reference"
            );
        }
        
        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(backendPenaltyForm, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }
}