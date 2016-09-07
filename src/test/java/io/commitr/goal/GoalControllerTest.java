package io.commitr.goal;

import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Peter Douglas on 9/5/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GoalController.class)
public class GoalControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GoalService service;

    @Test
    public void setGoal() throws Exception {

        Goal goal = new Goal();
        goal.setTitle("Goal Test");

        given(this.service.createGoal(goal))
                .willReturn(DTOUtils.createGoal(DTOUtils.VALID_UUID, "Goal Test"));
        this.mvc.perform(post("/goal")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(goal)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title",containsString("Goal Test")));
    }
}
