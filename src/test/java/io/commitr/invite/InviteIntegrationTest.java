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

import static java.lang.String.format;
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
        Goal response = this.restTemplate.postForObject("/goal",
                DTOUtils.createGoal(null, "first goal"), Goal.class);

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

        this.restTemplate.postForObject("/invite", DTOUtils.createInvite(DTOUtils.VALID_UUID, DTOUtils.VALID_UUID), Invite.class);

        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(format("/invite/%s", DTOUtils.VALID_UUID_STRING), Invite.class);


        assertThat(invite).isNotNull();
        assertThat(invite.getStatusCodeValue()).isEqualTo(200);
        assertThat(invite.getBody().getGoal()).isEqualTo(DTOUtils.VALID_UUID);

    }

    @Test
    public void getInviteByBadPathUUID() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(format("/invite/%s", "invalid-uuid-format"), Invite.class);

        assertThat(invite.getStatusCodeValue()).isEqualTo(400);

    }

    @Test
    public void getInviteByInvalidInvite() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(format("/invite/%s", DTOUtils.NON_VALID_UUID_STRING), Invite.class);

        assertThat(invite.getStatusCodeValue()).isEqualTo(404);

    }

    @Test
    public void getInviteByGoal() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                DTOUtils.createGoal(null, "first goal"), Goal.class);

        this.restTemplate.postForObject("/invite", DTOUtils.createInvite(null,
                response.getUuid()), Invite.class);

        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?goal=%s", response.getUuid().toString()), Invite.class);


        assertThat(invite).isNotNull();
        assertThat(invite.getStatusCodeValue()).isEqualTo(200);
        assertThat(invite.getBody().getUuid()).isNotNull();
        assertThat(invite.getBody().getGoal()).isEqualTo(DTOUtils.VALID_UUID);
    }

    @Test
    public void getInviteByBadParamUUID() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?goal=%s", "invalid-uuid-format"), Invite.class);

        assertThat(invite.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getInviteByInvalidGoal() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?goal=%s", DTOUtils.NON_VALID_UUID_STRING), Invite.class);

        assertThat(invite.getStatusCodeValue()).isEqualTo(400);

    }

    @Test
    public void deleteInvite() throws Exception {


    }
}
