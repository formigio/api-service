package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/13/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Test
    public void getTaskWithValidUUID() throws Exception {
        Task task = postTestTask();

        ResponseEntity<Task> response = getTaskResponseEntity(task.getUuid());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getUuid()).isEqualByComparingTo(task.getUuid());
    }

    @Test
    public void createTaskWithValidGoal() {

        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = DTOUtils.createTask(null, "Create a task with a valid Goal - Task", validGoal, false);

        ResponseEntity<Task> response = postTaskWithResponseEntity(task);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getUuid()).isEqualByComparingTo(task.getUuid());
        assertThat(response.getBody().getGoal()).isEqualToComparingFieldByField(validGoal);
    }

    @Test
    public void getGoalWithValidTask() throws Exception{
        Task response = postTestTask();

        Goal goal = this.restTemplate.getForObject(new URI(format("http://localhost:%d/task/%s", port,
                response.getGoal().getUuid().toString())), Goal.class);

        assertThat(goal.getTasks().contains(response)).isTrue();
    }

    @Test
    public void createTaskWithNonValidGoal() {
        Task task = DTOUtils.createTask(null, "Test Task - Invalid Goal",new Goal(), false);

        ResponseEntity<Task> response = postTaskWithResponseEntity(task);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to also check that a valid error message is coming back with the response
    }

    @Test
    public void createTaskWithInvalidUUIDFormat() throws Exception{
        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid("invalid-uuid-format");
        taskDTO.setTitle("Bad UUID Task Update Test");
        taskDTO.setGoal(DTOUtils.VALID_UUID_STRING);
        taskDTO.setCompleted(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.POST, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void updateTaskWithValidUUID() throws Exception {
        Task task = postTestTask();

        task.setTitle("Test Update Task");

        this.restTemplate.put("/task", task);

        Task updatedTask = getTestTask(task.getUuid());

        assertThat(updatedTask.getTitle()).isEqualTo("Test Update Task");
    }

    @Test
    public void updateTaskWithNonValidUUID() throws Exception{
        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid("invalid-uuid-format");
        taskDTO.setTitle("Bad UUID Task Update Test");
        taskDTO.setGoal(DTOUtils.VALID_UUID_STRING);
        taskDTO.setCompleted(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.PUT, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void updateTaskWithNonValidGoal() throws Exception{
        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid(DTOUtils.VALID_UUID_STRING);
        taskDTO.setTitle("Bad UUID Task Update Test");
        taskDTO.setGoal(DTOUtils.NON_VALID_UUID_STRING);
        taskDTO.setCompleted(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.PUT, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void deleteTaskWithValidUUID() throws Exception{
        Task task = postTestTask();

        ResponseEntity<Task> response;

        response = getTaskResponseEntity(task.getUuid());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        this.restTemplate.delete(new URI(format("http:localhost:%d/task/%s",port,task.getUuid().toString())));

        response = getTaskResponseEntity(task.getUuid());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteTaskWithNonValidUUID() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = this.restTemplate.exchange(format("/task/%s", DTOUtils.NON_VALID_UUID_STRING), HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    private Task postTestTask() {
        Task task = DTOUtils.createTask(null,
                "Test Task",
                DTOUtils.createGoal(UUID.randomUUID(), "Test Parent Goal"),
                false);

        return postTestTask(task);
    }

    private Task postTestTask(Task task) {
        return postTaskWithResponseEntity(task).getBody();
    }

    private ResponseEntity<Task> postTaskWithResponseEntity(Task task) {
        return this.restTemplate.postForEntity("/task", task, Task.class);
    }

    private Task getTestTask(UUID uuid) throws Exception {
        return getTaskResponseEntity(uuid).getBody();
    }

    private ResponseEntity<Task> getTaskResponseEntity(UUID uuid) throws Exception {
        return this.restTemplate.getForEntity(new URI(format("http://localhost:%d/task/%s", port, uuid.toString())),
                Task.class);
    }

}
