package library.validator.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import library.form.PenaltyForm;
import library.model.entity.Penalty;
import library.security.CSRFProtector;
import library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PenaltyFormValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(PenaltyFormValidator.class);
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;
    
    public PenaltyFormValidator() {}
    
    public PenaltyFormValidator(CSRFProtector csrfProtector, HttpServletRequest request) {
        this.csrfProtector = csrfProtector;
        this.request = request;
    }
    
    public boolean supports(Class clazz) {
        return PenaltyForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        PenaltyForm penaltyForm = (PenaltyForm) obj;
        logger.info("================================> penaltyForm " + penaltyForm);
        List<Penalty> penaltiesChecked = new ArrayList<Penalty>();
        if (penaltyForm.getPenaltiesChecked() != null) {
            for (Penalty penalty : penaltyForm.getPenaltiesChecked()) {
                logger.info("Comparing " + penalty.getSubscriber() + " with " + penaltyForm.getSubscriber());
                if (penalty.getSubscriber().equals(penaltyForm.getSubscriber())) {
                    logger.info("Adding penalty : " + penalty);
                    penaltiesChecked.add(penalty);
                }
            }
        }
        if (penaltiesChecked.size() == 0) {
            errors.rejectValue(
                "penaltiesChecked", "error.penaltyForm.penaltyEmpty", "Select at least one penalty to pay"
            );        
        }
        if (penaltyForm.getModeChecked() == 0) {
            errors.rejectValue( "modeChecked",
                "error.penaltyForm.modeEmpty", "Select payment mode"
            );
        }
        
        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(penaltyForm, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }
}