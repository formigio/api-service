package io.commitr.validator;

import io.commitr.annotation.ValidGoal;
import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by Peter Douglas on 10/5/2016.
 */
@RunWith(SpringRunner.class)
public class ValidGoalTest {

    @MockBean
    private GoalService goalService;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Autowired
    private GoalValidator constraintValidator;

    private ValidGoal validGoal;

    private Exception thrownException;

    private Boolean results;

    @Before
    public void setUp() throws Exception {
        when(goalService.getGoal(DTOUtils.VALID_UUID)).thenReturn(Goal.of(DTOUtils.VALID_UUID, "Test Goal", DTOUtils.VALID_UUID));
        when(goalService.getGoal(DTOUtils.NON_VALID_UUID)).thenReturn(null);
    }

    @Test
    public void testValidatorInitialize() throws Exception {
        try {
            constraintValidator.initialize(validGoal);
        } catch (final Exception e) {
            thrownException = e;
        }

        assertNull("No exception expected", thrownException);
        verifyZeroInteractions(goalService);
    }

    @Test
    public void testValidatorWithNullValue() throws Exception {

        try {
            results = constraintValidator.isValid(null, constraintValidatorContext);
        } catch (final Exception e) {
            thrownException = e;
        }

        assertNull("No exception expected", thrownException);
        assertThat(results).isFalse();
        verifyZeroInteractions(goalService);
    }

    @Test
    public void testValidatorWithValidValue() throws Exception {

        try {
            results = constraintValidator.isValid(DTOUtils.VALID_UUID, constraintValidatorContext);
        } catch (final Exception e) {
            thrownException = e;
        }

        assertNull("No exception expected", thrownException);
        assertThat(results).isTrue();
        verify(goalService, times(1)).getGoal(DTOUtils.VALID_UUID);
    }

    @Test
    public void testValidatorWithInValidValue() throws Exception {

        try {
            results = constraintValidator.isValid(DTOUtils.NON_VALID_UUID, constraintValidatorContext);
        } catch (Exception e) {
            thrownException = e;
        }

        assertNull("No exception expected", thrownException);
        assertThat(results).isFalse();
        verify(goalService, times(1)).getGoal(DTOUtils.NON_VALID_UUID);
    }

    @Configuration
    static class validationConfig {

        @Bean
        GoalValidator goalValidator() { return new GoalValidator(); }
    }
}
