package io.commitr.validator;

import io.commitr.annotation.ValidIdentity;
import io.commitr.configuration.IdentityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

/**
 * Created by peter on 11/12/16.
 */
public class IdentityValidator implements ConstraintValidator<ValidIdentity, String> {

    @Autowired
    private IdentityConfiguration configuration;

    @Override
    public void initialize(ValidIdentity constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.matches(configuration.getIdentity(), value);
    }
}