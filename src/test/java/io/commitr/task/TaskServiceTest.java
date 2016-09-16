package io.commitr.task;

import io.commitr.util.DTOUtils;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

    @Mock
    Task taskMock;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Test
    public void createTask() throws Exception {
        given(this.taskRepository.save(taskMock))
                .willReturn(DTOUtils.createTask(DTOUtils.VALID_UUID,
                        "Test Task", DTOUtils.createGoal(DTOUtils.VALID_UUID, "Test Task Goal"), false));

        Task task = taskService.saveTask(taskMock);

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Test Task");
        assertThat(task.getGoal().getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getCompleted()).isFalse();
    }

    @Test
    public void getTask() throws Exception {
        TaskDTOImpl dto = new TaskDTOImpl();

        dto.setUuid(DTOUtils.VALID_UUID);
        dto.setTitle("Task Test");
        dto.setGuid(DTOUtils.VALID_UUID);
        dto.setCompleted(false);

        given(this.taskRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(dto);

        TaskDTO task = taskService.getTask(DTOUtils.VALID_UUID);

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Task Test");
        assertThat(task.getGoal().toString()).isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(task.getCompleted()).isFalse();

    }

    @Test
    public void updateTask() throws Exception {
        given(this.taskRepository.save(taskMock))
                .willReturn(DTOUtils.createTask(DTOUtils.VALID_UUID,
                        "Test Update Task", DTOUtils.createGoal(DTOUtils.VALID_UUID, "Test Task Goal"), false));

        Task task = taskService.saveTask(taskMock);

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Test Update Task");
        assertThat(task.getGoal().getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getCompleted()).isFalse();
    }

    @Test
    public void deleteTask() throws Exception {

    }

    @Configuration
    static class ServiceConfig {
        @Mock
        TaskRepository repository;

        @Primary
        @Bean
        public TaskRepository taskRepository() { return repository; }

        @Bean
        public TaskService taskService() { return new TaskServiceImpl(); }
    }

    @Data
    static class TaskDTOImpl implements TaskDTO {
        private UUID uuid;
        private String title;
        private UUID guid;
        private Boolean completed;

        @Override
        public UUID getUuid() {
            return this.uuid;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public UUID getGoal() {
            return this.guid;
        }

        @Override
        public Boolean getCompleted() {
            return this.completed;
        }
    }

}
