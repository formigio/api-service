package io.commitr.task;

import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by peter on 9/14/16.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    Task dtoNotCompleted = new Task();
    Task dtoCompleted = new Task();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService service;

    @Before
    public void setUp() throws Exception {


        dtoNotCompleted.setUuid(DTOUtils.VALID_UUID);
        dtoNotCompleted.setTitle("Test Task");
        dtoNotCompleted.setGoal(DTOUtils.VALID_UUID);
        dtoNotCompleted.setCompleted(false);

        dtoCompleted.setUuid(DTOUtils.VALID_UUID);
        dtoCompleted.setTitle("Test Task");
        dtoCompleted.setGoal(DTOUtils.VALID_UUID);
        dtoCompleted.setCompleted(false);

        given(service.saveTask(dtoNotCompleted))
                .willReturn(DTOUtils.createTask(DTOUtils.VALID_UUID,
                        "Test Task",
                        DTOUtils.VALID_UUID,
                        false));

        given(service.saveTask(dtoCompleted))
                .willReturn(DTOUtils.createTask(DTOUtils.VALID_UUID,
                        "Test Task",
                        DTOUtils.VALID_UUID,
                        true));


        given(this.service.getTask(DTOUtils.VALID_UUID))
                .willReturn(dtoNotCompleted);
    }

    @Test
    public void createTask() throws Exception {

        Task taskDTO = dtoNotCompleted;

        this.mvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Test Task")));

    }

    @Test
    public void getTask() throws Exception {


        this.mvc.perform(get(format("/task/%s", DTOUtils.VALID_UUID_STRING)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)));

    }

    @Test
    public void updateTask() throws Exception {

        Task taskDTO = dtoNotCompleted;

        this.mvc.perform(put("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void completeTask() throws Exception {

        Task taskDTO = dtoNotCompleted;

        this.mvc.perform(put("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(taskDTO)))
                .andExpect(status().isOk());

    }

    @Test
    public void removeTask() throws Exception {
        this.mvc.perform(delete("/task/{uuid}", DTOUtils.VALID_UUID))
                .andExpect(status().isOk());

    }
}
