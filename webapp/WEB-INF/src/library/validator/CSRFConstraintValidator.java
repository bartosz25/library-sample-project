package library.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import library.security.CSRFProtector;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CSRFConstraintValidator implements ConstraintValidator<CSRFConstraint, Object> {
    final Logger logger = LoggerFactory.getLogger(CSRFConstraintValidator.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CSRFProtector csrfProtector;

    public void initialize(CSRFConstraint constraintAnnotation) {}
    
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setCSRFProtector(CSRFProtector csrfProtector) {
        this.csrfProtector = csrfProtector;
    }

    public boolean isValid(Object entity, ConstraintValidatorContext context) {
        if (request == null || csrfProtector == null || entity == null) return false;
        HttpSession session = request.getSession();
        if (session == null || !csrfProtector.checkSessionTokenValidity(session)) return false;
	
        String requestToken = "";
        String csrfIntention = "";
        try {
            requestToken = BeanUtils.getProperty(entity, "token");
            csrfIntention = BeanUtils.getProperty(entity, "action");
        } catch (Exception e) {
            logger.error("An exception occured on getting entity properties", e);
            return false;
        }

        String userToken = "";
        try {
            csrfProtector.setIntention(csrfIntention);
            userToken = csrfProtector.constructToken(session);
        } catch (Exception e) {
            logger.info("An exception occured on generating CSRF token", e);
            return false;
        }
        logger.info("=> Comparing requestToken ("+requestToken+") with userToken("+userToken+")");
        return requestToken.equals(userToken);
    }
}