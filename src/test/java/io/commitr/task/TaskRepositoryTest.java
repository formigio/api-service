package io.commitr.task;

import io.commitr.configuration.DocumentationConfiguration;
import io.commitr.configuration.ValidationConfiguration;
import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import io.commitr.util.DTOUtils;
import io.commitr.validator.GoalValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest(excludeFilters = {
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE,
                value = {
                        DocumentationConfiguration.class
                })},
        includeFilters = {
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE,
                value = {
                        ValidationConfiguration.class
                })
        })
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;

    @Test
    public void saveTask() throws Exception {

        Task task = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        DTOUtils.VALID_UUID,
                        false)
        );

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getGoal()).isEqualByComparingTo(DTOUtils.VALID_UUID);
    }

    @Test
    public void findTask() throws Exception {

        Task taskSaved = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        DTOUtils.VALID_UUID,
                        false));

        Task task = repository.findByUuid(taskSaved.getUuid());

        assertThat(task.getUuid())
                .isEqualTo(taskSaved.getUuid());
        assertThat(task.getTitle()).isEqualTo("Test Task");

    }

    @Test
    public void updateTask() throws Exception {
        Task taskSaved = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        DTOUtils.VALID_UUID,
                        false));

        assertThat(taskSaved.getTitle()).isEqualTo("Test Task");

        taskSaved.setTitle("Test Update Task");
        taskSaved.setCompleted(true);

        Task task = repository.saveAndFlush(taskSaved);

        assertThat(task.getTitle()).isEqualTo("Test Update Task");
        assertThat(task.getCompleted()).isTrue();

    }

    @Test
    public void deleteTask() throws Exception {
        Task task = repository.save(
                Task.of(DTOUtils.VALID_UUID, "Test Task",
                        DTOUtils.VALID_UUID,
                        false)
        );

        repository.delete(task);

        assertThat(repository.findByUuid(DTOUtils.VALID_UUID)).isNull();

    }

    @Test
    public void findTaskByGoal() throws Exception {
        repository.save(
                Task.of(null, "Test Task 1",
                        DTOUtils.VALID_UUID,
                        false));
        repository.save(
                Task.of(null, "Test Task 2",
                        DTOUtils.VALID_UUID,
                        false));

        List<Task> tasks = repository.findByGoal(DTOUtils.VALID_UUID);

        assertThat(tasks.size()).isEqualTo(2);

    }
}