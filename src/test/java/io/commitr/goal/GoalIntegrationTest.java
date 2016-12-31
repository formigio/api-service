package io.commitr.goal;

import io.commitr.util.AbstractIntegrationTest;
import io.commitr.util.DTOUtils;
import io.commitr.util.RestTemplateConfiguration;
import io.commitr.util.XHeaderInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/12/16.
 */
public class GoalIntegrationTest extends AbstractIntegrationTest{

    @Test
    public void createGoalWithoutUUID() throws Exception{
        Goal response = postNewGoal(Goal.of(null, "first goal", DTOUtils.VALID_UUID));

        assertThat(response.getUuid()).isNotNull();
    }

    @Test
    public void createGoalWithUUID() throws Exception{
        Goal response = postNewGoal(Goal.of(DTOUtils.VALID_UUID, "first goal", DTOUtils.VALID_UUID));

        assertThat(response.getUuid()).isEqualByComparingTo(DTOUtils.VALID_UUID);
    }

    @Test
    public void createGoalWithInvalidGuid() throws Exception{

        String content = "{" +
                "    \"uuid\":\"invalid-string-format\"," +
                "    \"title\":\"Test Goal\"" +
                "}";

        ResponseEntity<Goal> goalResponse = restTemplate.postForEntity(new URI("http://localhost:"+ port +"/goal"),
                content,
                Goal.class);

        assertThat(goalResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getGoal() throws Exception {
        Goal response = postNewGoal(Goal.of(null, "first goal", DTOUtils.VALID_UUID));

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

        Goal g1 = postNewGoal(Goal.of(null, "first goal", team));


        Goal g2 = postNewGoal(Goal.of(null, "second goal", team));

        ResponseEntity<Goal[]> response = this.restTemplate.getForEntity(format("/goal?team=%s", team.toString()), Goal[].class);

        Goal[] goals = response.getBody();

        assertThat(goals.length).isEqualTo(2);
        assertThat(goals).containsExactlyInAnyOrder(g1, g2);

    }

    @Test
    public void getGoalsByNonValidTeam() throws Exception {
        ResponseEntity<Goal[]> response = this.restTemplate.getForEntity(format("/goal?team=%s", DTOUtils.NON_VALID_UUID_STRING), Goal[].class);

        Goal[] goals = response.getBody();

        assertThat(goals.length).isEqualTo(0);

    }

    public Goal postNewGoal(Goal goal) throws Exception{
        return this.restTemplate.postForObject("/goal",
                goal,
                Goal.class);
    }
}
