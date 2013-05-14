package library.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy=UniqueRecordDbValidator.class)
@Documented
public @interface UniqueRecordDb {
    String message() default "Check if record exists in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String column();
    String query();
    String parameter();
}