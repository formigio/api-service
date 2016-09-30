package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * Created by Peter Douglas on 9/29/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InviteIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Test
    public void createInviteWithValidGoal() throws Exception {
        Goal goal = DTOUtils.createGoal(null, "first goal");
        Goal response = this.restTemplate.postForObject("/goal", goal, Goal.class);

        String content = "{" +
                "    \"guid\":null," +
                "    \"goal\":\"" + response.getUuid().toString() + "\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(inviteResponse.getBody()).isNotNull();
        assertThat(inviteResponse.getBody().getUuid()).isNotNull();
        assertThat(inviteResponse.getBody().getGoal()).isNotNull();
    }

    @Test
    public void createInviteWithInvalidGoal() throws Exception {
        String content = "{" +
                "    \"guid\":null," +
                "    \"goal\":\"" + DTOUtils.NON_VALID_UUID_STRING + "\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createInviteWithInvalidFormat() throws Exception {
        Goal goal = DTOUtils.createGoal(null, "first goal");
        Goal response = this.restTemplate.postForObject("/goal", goal, Goal.class);

        String content = "{" +
                "    \"guid\":null," +
                "    \"goal\":\"invalid-guid-format\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getInviteByUuid() throws Exception {


    }

    @Test
    public void getInviteByBadPathUUID() throws Exception {


    }

    @Test
    public void getInviteByInvalidInvite() throws Exception {


    }

    @Test
    public void getInviteByGoal() throws Exception {


    }

    @Test
    public void getInviteByBadParamUUID() throws Exception {


    }

    @Test
    public void getInviteByInvalidGoal() throws Exception {


    }

    @Test
    public void deleteInvite() throws Exception {


    }
}
