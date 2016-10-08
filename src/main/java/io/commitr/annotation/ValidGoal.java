package io.commitr.annotation;

import io.commitr.validator.GoalValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GoalValidator.class)
public @interface ValidGoal {
    String message() default "{valid.goal.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
