package io.commitr.task;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;

    @Autowired
    EntityManager em;

    @Test
    public void saveTask() throws Exception {

        Task task = repository.save(
                DTOUtils.createTask(null, "Test Task",
                        DTOUtils.VALID_UUID,
                        false)
        );

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid().toString())
                .containsPattern("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
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
                DTOUtils.createTask(null, "Test Task",
                        DTOUtils.VALID_UUID,
                        false)
        );

        assertThat(task.getUuid()).isNotNull();

        assertThat(repository.deleteByUuid(task.getUuid())).isGreaterThan(0);

    }

    @Test
    public void findTaskByGoal() throws Exception {
        repository.save(
                DTOUtils.createTask(null, "Test Task 1",
                        DTOUtils.VALID_UUID,
                        false));
        repository.save(
                DTOUtils.createTask(null, "Test Task 2",
                        DTOUtils.VALID_UUID,
                        false));

        List<Task> tasks = repository.findByGoal(DTOUtils.VALID_UUID);

        assertThat(tasks.size()).isEqualTo(2);

    }

}