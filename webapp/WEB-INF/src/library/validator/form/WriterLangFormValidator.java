package library.validator.form;

import library.form.WriterLangForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WriterLangFormValidator implements Validator  {
    final Logger logger = LoggerFactory.getLogger(WriterLangFormValidator.class);

    public boolean supports(Class clazz) {
        return WriterLangForm.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        // logger.info("================================> obj " + obj);
        // CategoryLangForm categoryLangForm = (CategoryLangForm) obj;
        // int i = 0;
        // for(CategoryLang categoryLang : categoryLangForm.getCategoryLang())
        // {
            // if(categoryLang.getName() != null && !categoryLang.getName().equals("") && categoryLang.getName().length() > MAX_LENGTH)
            // {
                // errors.rejectValue(
                    // "categoryLang["+i+"].name",
                    // "max.length",
                    // new Object[] {MAX_LENGTH},
                    // "The password max length is " + MAX_LENGTH
                // );
            // }
            // i++;
        // }
    }
}