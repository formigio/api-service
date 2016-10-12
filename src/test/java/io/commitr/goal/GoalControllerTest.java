package io.commitr.goal;

import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Peter Douglas on 9/5/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GoalController.class)
public class GoalControllerTest {


    private final Goal GOAL_RESPONSE = Goal.of(DTOUtils.VALID_UUID, "Goal Test", DTOUtils.VALID_UUID);
    private final Goal GOAL_REQUEST = Goal.of(null, "Goal Test", DTOUtils.VALID_UUID);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GoalService service;

    @Before
    public void setUp() throws Exception {

        given(this.service.createGoal(GOAL_REQUEST))
                .willReturn(GOAL_RESPONSE);
        given(this.service.getGoal(DTOUtils.VALID_UUID))
                .willReturn(GOAL_RESPONSE);
        given(this.service.getGoalsByTeam(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(GOAL_RESPONSE, GOAL_RESPONSE)
                        .collect(Collectors.toList()));

    }

    @Test
    public void setGoal() throws Exception {

        this.mvc.perform(post("/goal")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(GOAL_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Goal Test")))
                .andExpect(jsonPath("$.team", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void getGoalWithValidUUID() throws Exception {

        this.mvc.perform(get("/goal/" + DTOUtils.VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Goal Test")));
    }

    @Test
    public void getGoalWithInvalidGuid() throws Exception {
        this.mvc.perform(get("/goal/invalid-uuid-format")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void getGoalByValidTeam() throws Exception {
        this.mvc.perform(get("/goal?team=" + DTOUtils.VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].title", containsInAnyOrder("Goal Test", "Goal Test")));
    }

    @Test
    public void getGoalByNonValidTeam() throws Exception {
        this.mvc.perform(get("/goal?team=" + DTOUtils.NON_VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getGoalByInvalidTeamUUID() throws Exception {
        this.mvc.perform(get("/goal?team=invalid-uuid-format")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }
}
