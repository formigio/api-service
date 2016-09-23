package io.commitr.task;

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {

    private UUID goal;

    @Autowired
    TaskRepository repository;

    @Autowired
    GoalRepository goalRepository;

    @Before
    public void setUp() throws Exception {
        goal = goalRepository.save(DTOUtils.createGoal(null, "Test Task Goal")).getUuid();

    }

    @Test
    public void saveTask() throws Exception {

        Task task = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        goal,
                        false)
        );

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid().toString())
                .containsPattern("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getGoal()).isEqualByComparingTo(goal);
    }

    @Test
    public void findTask() throws Exception {

        Task taskSaved = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        goal,
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
                        goal,
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
        Task task = repository.saveAndFlush(
                DTOUtils.createTask(null, "Test Task",
                        goal,
                        false)
        );

        assertThat(task.getUuid()).isNotNull();

        assertThat(repository.deleteByUuid(task.getUuid())).isGreaterThan(0);

    }

    @Test
    public void findTaskByGoal() throws Exception {
        Task taskSaved = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        goal,
                        false));

        Task task = repository.findByGoal(goal);

        assertThat(task.getGoal()).isEqualTo(goal);

    }

}