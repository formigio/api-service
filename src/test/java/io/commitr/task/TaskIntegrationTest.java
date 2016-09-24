package io.commitr.task;

import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;
import io.commitr.goal.Goal;
import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Set;
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
    public void getTask() throws Exception {
        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = DTOUtils.createTask(null, "Create a task with a valid Goal - Task", validGoal.getUuid(), false);

        Task dto = this.restTemplate.postForObject("/task", task, Task.class);

        assertThat(dto).isNotNull();

    }

    @Test
    public void createTaskWithValidGoal() throws Exception{

        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = new Task();

        task.setUuid(null);
        task.setTitle("Create a task with a valid Goal - Task");
        task.setGoal(validGoal.getUuid());
        task.setCompleted(false);

        ResponseEntity<Task> response = this.restTemplate.postForEntity(new URI(format("http://localhost:%d/task", port)), task, Task.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getUuid()).isNotNull();
        assertThat(response.getBody().getGoal()).isEqualTo(validGoal.getUuid());
    }

    @Test
    public void createTaskWithNonValidGoal() {
        Task task = DTOUtils.createTask(null, "Test Task",DTOUtils.NON_VALID_UUID, false);

        ResponseEntity<Task> response = postTaskWithResponseEntity(task);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        //TODO: Need to also check that a valid error message is coming back with the response
    }

    @Test
    public void createTaskWithInvalidUUIDFormat() throws Exception{

        DTOUtils.TaskDTO taskDTO = DTOUtils.TaskDTOBuilder.of(
                "invalid-uuid-format",
                "Bad UUID Task Update Test",
                DTOUtils.VALID_UUID_STRING,
                false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.POST, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void updateTaskWithValidUUID() throws Exception {
        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = new Task();

        task.setUuid(null);
        task.setTitle("Create a task with a valid Goal - Task");
        task.setGoal(validGoal.getUuid());
        task.setCompleted(false);

        ResponseEntity<Task> response = this.restTemplate.postForEntity(new URI(format("http://localhost:%d/task", port)), task, Task.class);

        task.setUuid(response.getBody().getUuid());
        task.setTitle("Test Update Task");

        this.restTemplate.put("/task", task);

        ResponseEntity<Task> responseWithUpdate = this.restTemplate.getForEntity(new URI(format("http://localhost:%d/task/%s", port, response.getBody().getUuid().toString())), Task.class);

        assertThat(responseWithUpdate.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void updateTaskWithNonValidUUID() throws Exception{


        DTOUtils.TaskDTO taskDTO = DTOUtils.TaskDTOBuilder.of(
                "invalid-uuid-format",
                "Bad UUID Task Update Test",
                DTOUtils.VALID_UUID_STRING,
                false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.PUT, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void updateTaskWithNonValidGoal() throws Exception{
        DTOUtils.TaskDTO taskDTO = DTOUtils.TaskDTOBuilder.of(
                "invalid-uuid-format",
                "Bad UUID Task Update Test",
                DTOUtils.VALID_UUID_STRING,
                false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(JsonUtils.convertObject(taskDTO).toString(), headers);

        ResponseEntity<Task> response = this.restTemplate.exchange(new URI("http://localhost:"+ port +"/task"), HttpMethod.PUT, request, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //TODO: Need to check that message is coming back and it is the one we are expecting
    }

    @Test
    public void deleteTaskWithValidUUID() throws Exception{
        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = new Task();

        task.setUuid(null);
        task.setTitle("Create a task with a valid Goal - Task");
        task.setGoal(validGoal.getUuid());
        task.setCompleted(false);

        ResponseEntity<Task> response = this.restTemplate.postForEntity(new URI(format("http://localhost:%d/task", port)), task, Task.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);

        this.restTemplate.delete(new URI(format("http://localhost:%d/task/%s",port,response.getBody().getUuid().toString())));

        ResponseEntity<Task> responseAfterDelete = this.restTemplate.getForEntity(new URI(format("http://localhost:%d/task/%s", port,response.getBody().getUuid().toString())), Task.class);

        assertThat(responseAfterDelete.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteTaskWithNonValidUUID() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = this.restTemplate.exchange(format("/task/%s", DTOUtils.NON_VALID_UUID_STRING), HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void getTasksByValidGoal() throws Exception{
        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = new Task();

        task.setUuid(null);
        task.setTitle("Create a task with a valid Goal - Task 1");
        task.setGoal(validGoal.getUuid());
        task.setCompleted(true);

        this.restTemplate.postForEntity(new URI(format("http://localhost:%d/task", port)), task, Task.class);

        task.setUuid(null);
        task.setTitle("Create a task with a valid Goal - Task 2");

        this.restTemplate.postForEntity(new URI(format("http://localhost:%d/task", port)), task, Task.class);

        ResponseEntity<Task[]> response = this.restTemplate.getForEntity(format("/task?goal=%s", validGoal.getUuid().toString()), Task[].class);

        Task[] tasks = response.getBody();

        assertThat(tasks.length).isEqualTo(2);
    }

    private Task postTestTask() {
        Goal validGoal = this.restTemplate.postForObject("/goal", DTOUtils.createGoal(null, "Create Task with Valid Goal"), Goal.class);

        Task task = DTOUtils.createTask(null,
                "Test Task",
                validGoal.getUuid(),
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
