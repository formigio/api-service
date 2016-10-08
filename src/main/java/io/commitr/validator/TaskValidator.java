package io.commitr.validator;

import io.commitr.annotation.ValidTask;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
public class TaskValidator implements ConstraintValidator<ValidTask, String>{
    @Override
    public void initialize(ValidTask constraintAnnotation) {
        //EMpty
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }
}
