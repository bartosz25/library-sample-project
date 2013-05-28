package com.example.library.validator.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.library.form.BookForm;
import com.example.library.model.entity.BookCopy;
import com.example.library.security.CSRFProtector;
import com.example.library.validator.CSRFConstraintValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookFormValidator implements Validator  {

    // private static final int MAX_LENGTH = 255;
    final Logger logger = LoggerFactory.getLogger(BookFormValidator.class);
    
    private CSRFProtector csrfProtector;
    private HttpServletRequest request;
    private MessageSource messageSource;
    
    public BookFormValidator(CSRFProtector csrfProtector, HttpServletRequest request, MessageSource messageSource) {
        this.csrfProtector = csrfProtector;
        this.request = request;
        this.messageSource = messageSource;
    }
    
    public boolean supports(Class clazz) {
        return BookForm.class.equals(clazz);
    }
    
    /**
     * Validates BookForm object. We have some constraints : 
     * - at least one writer must be selected
     * - at least one category muste be selected
     * - BookCopy's code must have exactly 10 characters
     * - BookCopy's state has to be between 0 and 1
     * - BookCopy's condition has to be between 1 and 2
     * - BookLang's type must have exactly 4 characters
     * - Book's alias can't be empty and has to be between 1 and 200 characters
     */
    public void validate(Object obj, Errors errors) {
        logger.info("================================> obj " + obj);
        BookForm bookForm = (BookForm) obj;
        
        if (bookForm.getCategoriesChecked() == null || bookForm.getCategoriesChecked().size() == 0) {
            errors.rejectValue("categoriesChecked", "error.book.categoriesEmpty", "Check at least one category");
        }
        if (bookForm.getWritersChecked() == null || bookForm.getWritersChecked().size() == 0)        {
            errors.rejectValue("writersChecked", "error.book.writersEmpty", "Check at least one writer"); 
        }
        if (bookForm.getBook().getAlias() == null || bookForm.getBook().getAlias().length() > 200 || bookForm.getBook().getAlias().length() == 0) {
            errors.rejectValue(
                "book.alias", "error.book.alias", new String[] {"200"},
                "The alias' length must be between 1 and 200 chars"
            );
        }
        if (bookForm.getTranslations() != null) {
            for (Long key : bookForm.getTranslations().keySet()) {
                Map<String, Object> infos = bookForm.getTranslations().get(key);
                for (String infosKey : infos.keySet()) {
                    if (infosKey != null && infosKey.length() != 4 && infos.get(infosKey) != null && !((String)
                        infos.get(infosKey)).equals("")) {
                            logger.debug("Rejecting value for " + "translations['"+key+"']['"+infosKey+"']");
                            // add some error here
                            errors.rejectValue(
                                "translations['"+key+"']['"+infosKey+"']",
                                "error.book.keyMaxLength", new Object[] {4},
                                "The info key's max length must be 4 chars"
                            );
                    }
                }
            }
        }
        if (bookForm.getCopies() != null) {
            int c = 0;
            for (BookCopy copy : bookForm.getCopies()) {
                if (copy.getCode() == null || copy.getCode().length() != 10) {
                    errors.rejectValue(
                        "copies["+c+"].code",
                        "error.book.bookCopyMaxLength", new Object[] {"10"},
                        "The code's max length must be 10 chars"
                    );
                }
                if (copy.getCondition() == 0 || copy.getCondition() > 2) {
                    errors.rejectValue(
                        "copies["+c+"].condition",
                        "error.book.bookCopyBadCondition",
                        "The condition's value must be between 1 and 2"
                    );
                }
                if (copy.getState() > 1) {
                    errors.rejectValue(
                        "copies["+c+"].state",
                        "error.book.bookCopyBadState",
                        "The state's value must be between 0 and 1"
                    );
                }
                c++;
            }
        }
        
        CSRFConstraintValidator csrfConstraintValidator = new CSRFConstraintValidator();
        csrfConstraintValidator.setCSRFProtector(csrfProtector);
        csrfConstraintValidator.setRequest(request);
        if (!csrfConstraintValidator.isValid(bookForm, null)) {
            errors.rejectValue(
                "token",
                "error.csrf.invalid",
                "An error occured. Please, try again later"
            );
        }
    }

}