package io.commitr.validator;

import io.commitr.annotation.ValidGoal;
import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
public class GoalValidator implements ConstraintValidator<ValidGoal, UUID>{

    @Autowired
    private GoalService goalService;

    @Override
    public void initialize(ValidGoal constraintAnnotation) {
        //Empty initialization
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        Goal g = null;

        if (null != value) {
            g = goalService.getGoal(value);
        }

        return null != g;
    }
}
