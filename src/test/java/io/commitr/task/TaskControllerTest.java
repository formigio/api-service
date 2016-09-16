package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by peter on 9/14/16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService service;

    @Test
    public void createTask() throws Exception {
        Task task = DTOUtils.createTask(null, "Test Task", new Goal(), false);

        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid(null);
        taskDTO.setTitle("Task Test");
        taskDTO.setGoal(DTOUtils.VALID_UUID_STRING);
        taskDTO.setCompleted(false);

        given(this.service.saveTask(task))
                .willReturn(DTOUtils.createTask(DTOUtils.VALID_UUID, "Task Test",
                        DTOUtils.createGoal(DTOUtils.VALID_UUID, "Task Test Goal"), false));

        this.mvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Task Test")));

    }

    @Test
    public void getTask() throws Exception {

        TaskDTOImpl dto = new TaskDTOImpl();

        dto.setUuid(DTOUtils.VALID_UUID);
        dto.setTitle("Task Test");
        dto.setGuid(DTOUtils.VALID_UUID);
        dto.setCompleted(false);

        given(this.service.getTask(DTOUtils.VALID_UUID))
                .willReturn(dto);

        this.mvc.perform(get(format("/task/%s", DTOUtils.VALID_UUID_STRING)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)));

    }

    @Test
    public void updateTask() throws Exception {
        Task task = DTOUtils.createTask(DTOUtils.VALID_UUID, "Test Task Update", new Goal(), false);

        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid(DTOUtils.VALID_UUID_STRING);
        taskDTO.setTitle("Test Task Update");
        taskDTO.setGoal(DTOUtils.VALID_UUID_STRING);
        taskDTO.setCompleted(false);

        given(this.service.saveTask(task))
                .willReturn(task);

        this.mvc.perform(put("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Test Task Update")))
                .andExpect(jsonPath("$.goal", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.completed", containsString("false")));
    }

    @Test
    public void completeTask() throws Exception {
        Task task = DTOUtils.createTask(DTOUtils.VALID_UUID, "Test Task Complete", new Goal(), true);

        DTOUtils.TaskDTO taskDTO = new DTOUtils().new TaskDTO();
        taskDTO.setUuid(DTOUtils.VALID_UUID_STRING);
        taskDTO.setTitle("Test Task Complete");
        taskDTO.setGoal(DTOUtils.VALID_UUID_STRING);
        taskDTO.setCompleted(true);

        given(this.service.saveTask(task))
                .willReturn(task);

        this.mvc.perform(put("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Test Task Complete")))
                .andExpect(jsonPath("$.goal", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.completed", containsString("true")));

    }

    @Test
    public void removeTask() throws Exception {
        this.mvc.perform(delete("/task/{uuid}", DTOUtils.VALID_UUID))
                .andExpect(status().is2xxSuccessful());

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
