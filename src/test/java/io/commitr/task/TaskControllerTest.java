package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import io.commitr.goal.GoalServiceImpl;
import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser
public class TaskControllerTest {

    Task dtoNotCompleted = new Task();
    Task dtoCompleted = new Task();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService service;

    @MockBean
    private GoalService goalService;

    @Before
    public void setUp() throws Exception {

        dtoNotCompleted.setUuid(DTOUtils.VALID_UUID);
        dtoNotCompleted.setTitle("Test Task");
        dtoNotCompleted.setGoal(DTOUtils.VALID_UUID);
        dtoNotCompleted.setCompleted(false);

        dtoCompleted.setUuid(DTOUtils.VALID_UUID);
        dtoCompleted.setTitle("Test Task");
        dtoCompleted.setGoal(DTOUtils.VALID_UUID);
        dtoCompleted.setCompleted(true);

        given(service.saveTask(dtoNotCompleted))
                .willReturn(dtoNotCompleted);

        given(service.saveTask(dtoCompleted))
                .willReturn(dtoCompleted);

        given(service.updateTask(dtoNotCompleted))
                .willReturn(dtoNotCompleted);

        given(service.updateTask(dtoCompleted))
                .willReturn(dtoCompleted);

        given(this.service.getTask(DTOUtils.VALID_UUID))
                .willReturn(dtoCompleted);

        given(this.goalService.getGoal(DTOUtils.VALID_UUID))
                .willReturn(Goal.of(DTOUtils.VALID_UUID, "Test Goal", DTOUtils.VALID_UUID));

        given(this.goalService.getGoal(DTOUtils.NON_VALID_UUID))
                .willReturn(null);
    }

    @Test
    public void createTask() throws Exception {

        this.mvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(dtoNotCompleted)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.uuid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Test Task")));

    }

    @Test
    public void getTask() throws Exception {


        this.mvc.perform(get(format("/task/%s", DTOUtils.VALID_UUID_STRING)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.uuid", containsString(DTOUtils.VALID_UUID_STRING)));

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
//
//    @Configuration
//    static class ControllerConfig {
//
//        @Mock
//        GoalServiceImpl service;
//
//        @Primary
//        @Bean
//        public GoalService goalService() { return service; }
//    }
}
