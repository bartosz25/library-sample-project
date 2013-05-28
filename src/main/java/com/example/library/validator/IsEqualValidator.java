package com.example.library.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsEqualValidator implements
ConstraintValidator<IsEqual, String> {
    private String toCompare;

    public void initialize(IsEqual constraintAnnotation) {
        toCompare = constraintAnnotation.value();
    }

    public boolean isValid(String text, ConstraintValidatorContext context) {
       return text.equals(toCompare);
    }
}