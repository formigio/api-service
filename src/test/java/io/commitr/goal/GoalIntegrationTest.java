package io.commitr.goal;

import io.commitr.team.Team;
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
import java.util.UUID;

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
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID),
                Goal.class);

        assertThat(response.getUuid()).isNotNull();

    }

    @Test
    public void createGoalWithUUID() {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(DTOUtils.VALID_UUID, "first goal", DTOUtils.VALID_UUID),
                Goal.class);

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

        assertThat(goalResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getGoal() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID),
                Goal.class);

        Goal goal = this.restTemplate.getForObject(format("/goal/%s", response.getUuid().toString()),Goal.class);

        assertThat(goal.getUuid()).isEqualByComparingTo(response.getUuid());
    }

    @Test
    public void getNonExistentGoal() throws Exception {

        ResponseEntity<Goal> response = this.restTemplate.getForEntity(format("/goal/%s", DTOUtils.NON_VALID_UUID_STRING),Goal.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void getGoalsByValidTeam() throws Exception {
        UUID team = UUID.randomUUID();

        Goal g1 = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", team),
                Goal.class);


        Goal g2 = this.restTemplate.postForObject("/goal",
                Goal.of(null, "second goal", team),
                Goal.class);

        ResponseEntity<Goal[]> response = this.restTemplate.getForEntity(format("/goal?team=%s", team.toString()), Goal[].class);

        Goal[] goals = response.getBody();

        assertThat(goals.length).isEqualTo(2);
        assertThat(goals).containsExactlyInAnyOrder(g1, g2);

    }

    @Test
    public void getGoalsByNonValidTeam() throws Exception {
        ResponseEntity<Goal> response = this.restTemplate.getForEntity(format("/goal?team=%s", DTOUtils.NON_VALID_UUID_STRING), Goal.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}
