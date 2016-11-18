package io.commitr.annotation;

import io.commitr.validator.IdentityValidator;

import javax.lang.model.element.Element;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by peter on 11/12/16.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityValidator.class)
public @interface ValidIdentity {
    String message() default "{valid.idenity.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
