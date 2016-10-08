package io.commitr.annotation;

import io.commitr.validator.TeamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TeamValidator.class)
public @interface ValidTeam {

    String message() default "{valid.team.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
