package com.example.library.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy=CSRFConstraintValidator.class)
@Documented
public @interface CSRFConstraint {
    String message() default "Invalid request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}