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
@Constraint(validatedBy=UniqueRecordDbCrossedValidator.class)
@Documented
public @interface UniqueRecordDbCrossed {
    String message() default "Check if record exists in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String uniqueColumn();
    String oldColumn();
    String query();
}