package library.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy=IsEqualValidator.class)
@Documented
public @interface IsEqual {
    String message() default "Check if is equal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value();
}