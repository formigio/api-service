package io.commitr.annotation;

import io.commitr.validator.TaskValidator;

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
@Constraint(validatedBy = TaskValidator.class)
public @interface ValidTask {
    String message() default "{valid.task.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
