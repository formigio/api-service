package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import org.junit.Before;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

    @Mock
    Task taskMock;

    @Mock
    Task badTaskMock;

    @Mock
    Goal goalMock;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private GoalRepository goalRepository;

    @Autowired
    private TaskService taskService;

    @Before
    public void setUp() throws Exception {

        given(this.taskRepository.findByUuid(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        given(this.taskRepository.save(taskMock))
                .willReturn(taskMock);

        given(this.taskRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(taskMock);

        given(this.goalRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(goalMock);

        when(taskMock.getId()).thenReturn(1L);
        when(taskMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
        when(taskMock.getTitle()).thenReturn("Mocked Task");
        when(taskMock.getGoal()).thenReturn(DTOUtils.VALID_UUID);
        when(taskMock.getCompleted()).thenReturn(false);

        when(badTaskMock.getUuid()).thenReturn(DTOUtils.NON_VALID_UUID);
        when(badTaskMock.getTitle()).thenReturn("Mocked Bad Task");
        when(badTaskMock.getGoal()).thenReturn(DTOUtils.NON_VALID_UUID);
        when(badTaskMock.getCompleted()).thenReturn(false);

    }

    @Test
    public void createTask() throws Exception {

        Task task = taskService.saveTask(taskMock);

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Mocked Task");
        assertThat(task.getGoal()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getCompleted()).isFalse();
    }

    @Test
    public void getTask() throws Exception {

        Task task = taskService.getTask(DTOUtils.VALID_UUID);

        assertThat(task.getUuid()).isNotNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Mocked Task");
        assertThat(task.getGoal().toString()).isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(task.getCompleted()).isFalse();

    }

    @Test
    public void getNonExistentTask() throws Exception {
        Task task = taskService.getTask(DTOUtils.NON_VALID_UUID);

        assertThat(task).isNull();

    }

    @Test
    public void updateNonExistentTask() throws Exception {

        Task task = taskService.updateTask(badTaskMock);

        assertThat(task).isNull();
    }

    @Test
    public void deleteTask() throws Exception {
        //Do nothing
    }

    @Configuration
    static class ServiceConfig {
        @Mock
        TaskRepository taskRepository;

        @Mock
        GoalRepository goalRepository;

        @Primary
        @Bean
        public TaskRepository taskRepository() { return taskRepository; }

        @Primary
        @Bean
        public GoalRepository goalRepository() { return goalRepository; }

        @Bean
        public TaskService taskService() { return new TaskServiceImpl(); }
    }
}
