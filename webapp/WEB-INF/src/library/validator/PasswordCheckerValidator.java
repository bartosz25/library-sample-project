package library.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import library.security.SaltCellar;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class PasswordCheckerValidator implements ConstraintValidator<PasswordChecker, Object> {
    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private SaltCellar saltCellar;
    @Autowired
    private ShaPasswordEncoder passwordEncoder;
    private String toSaltField;
    private String passwordField;
    private String query;
    private String queryUser;
    final Logger logger = LoggerFactory.getLogger(PasswordCheckerValidator.class);

    public void initialize(PasswordChecker constraintAnnotation) {
        toSaltField = constraintAnnotation.toSaltField();
        passwordField = constraintAnnotation.passwordField();
        query = constraintAnnotation.query();
        queryUser = constraintAnnotation.queryUser();
    }
    
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (emf == null) return false;
        try {
            EntityManager entityManager = emf.createEntityManager();
            User user = (User) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            logger.info("=============> Validating password with User " + user);
            Query queryInstUser = entityManager.createQuery(queryUser);
            queryInstUser.setParameter("login", user.getUsername());
		    Object userObject = queryInstUser.getSingleResult();
            logger.info("=============> userObject found " + userObject);
            // first, get both values to check
            String saltString = BeanUtils.getProperty(userObject, toSaltField);
            String passwordString = BeanUtils.getProperty(value, passwordField);
            // secondly, create salted password
            String finalSaltString = saltCellar.getSaltFromString(saltString);
            // thirdly, construct SQL check query
            logger.info("===============> Validating using query " + query);
            logger.info("===============> Validating using password " + passwordString);
            logger.info("===============> Validating using saltString " + saltString);
            Query queryInst = entityManager.createQuery(query);
            logger.info("===============> Setting password " + passwordEncoder.encodePassword(passwordString, finalSaltString));
            queryInst.setParameter("password", passwordEncoder.encodePassword(passwordString, finalSaltString));
            List list = queryInst.getResultList();
            logger.info(".==============> Found results " + list);
            return (list != null && list.size() > 0);
        } catch(Exception e) {
            logger.error("An exception occured on validating unique record", e);
        }
        return false;
    }
}