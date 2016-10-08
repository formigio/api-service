package io.commitr.validator;

import io.commitr.annotation.ValidTeam;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
public class TeamValidator implements ConstraintValidator<ValidTeam, String>{
    @Override
    public void initialize(ValidTeam constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }
}
