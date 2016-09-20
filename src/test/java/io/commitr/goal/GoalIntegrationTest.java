package io.commitr.goal;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GoalIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Test
    public void createGoalWithoutUUID() {
        Goal goal = DTOUtils.createGoal(null, "first goal");
        Goal response = this.restTemplate.postForObject("/goal", goal, Goal.class);

        assertThat(response.getUuid()).isNotNull();

    }

    @Test
    public void createGoalWithUUID() {
        Goal goal = DTOUtils.createGoal(DTOUtils.VALID_UUID, "first goal");
        Goal response = this.restTemplate.postForObject("/goal", goal, Goal.class);

        assertThat(response.getUuid()).isEqualByComparingTo(DTOUtils.VALID_UUID);
    }

    @Test
    public void createGoalWithInvalidGuid() throws Exception{

        String content = "{" +
                "    \"guid\":\"invalid-string-format\"," +
                "    \"title\":\"Test Goal\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Goal> goalResponse = restTemplate.exchange(new URI("http://localhost:"+ port +"/goal"), HttpMethod.POST, request, Goal.class);

        assertThat(goalResponse.getStatusCodeValue()).isBetween(400, 499);
    }

    @Test
    public void getGoal() throws Exception {
        Goal goal = DTOUtils.createGoal(null, "first goal");
        Goal response = this.restTemplate.postForObject("/goal", goal, Goal.class);

        goal = this.restTemplate.getForObject(format("/goal/%s", response.getUuid().toString()),Goal.class);

        assertThat(goal.getUuid()).isEqualByComparingTo(response.getUuid());
    }
}
