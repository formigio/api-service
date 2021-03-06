package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.util.AbstractIntegrationTest;
import io.commitr.util.DTOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Peter Douglas on 9/29/2016.
 */
public class InviteIntegrationTest extends AbstractIntegrationTest{

    @Test
    public void createInviteWithValidGoal() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID), Goal.class);

        String content = "{" +
                "    \"uuid\":null," +
                "    \"entity\":\"" + response.getUuid().toString() + "\"," +
                "    \"entityType\":\"goal\"," +
                "    \"inviter\":\"inviter\"," +
                "    \"invitee\":\"invitee\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCodeValue()).isEqualTo(201);
        assertThat(inviteResponse.getBody()).isNotNull();
        assertThat(inviteResponse.getBody().getUuid()).isNotNull();
        assertThat(inviteResponse.getBody().getEntity()).isNotNull();
    }

    @Test
    public void createInviteWithInvalidGoal() throws Exception {
        String content = "{" +
                "    \"uuid\":null," +
                "    \"entity\":\"" + DTOUtils.NON_VALID_UUID_STRING + "\"," +
                "    \"entityType\":\"goal\"," +
                "    \"inviter\":\"invitee\"," +
                "    \"invitee\":\"inviter\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createInviteWithInvalidFormat() throws Exception {
        String content = "{" +
                "    \"uuid\":null," +
                "    \"entity\":\"invalid-guid-format\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Invite> inviteResponse = restTemplate.postForEntity("/invite", request, Invite.class);

        assertThat(inviteResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getInviteByUuid() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID), Goal.class);

        this.restTemplate.postForObject("/invite",
                Invite.of(DTOUtils.VALID_UUID,
                        response.getUuid(),
                        "goal",
                        "inviter",
                        "invitee"), Invite.class);

        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite/%s", DTOUtils.VALID_UUID_STRING), Invite.class);


        assertThat(invite).isNotNull();
        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invite.getBody().getEntity()).isEqualTo(response.getUuid());

    }

    @Test
    public void getInviteByBadPathUUID() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite/%s", "invalid-uuid-format"), Invite.class);

        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void getInviteByInvalidInvite() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite/%s", DTOUtils.NON_VALID_UUID_STRING), Invite.class);

        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Ignore
    @Test
    public void getInviteByGoal() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID), Goal.class);

        this.restTemplate.postForObject("/invite",
                Invite.of(null,
                        response.getUuid(),
                        "goal",
                        "invitee",
                        "inviter"), Invite.class);

        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?entity=%s&entityType=goal", response.getUuid().toString()), Invite.class);


        assertThat(invite).isNotNull();
        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invite.getBody().getUuid()).isNotNull();
        assertThat(invite.getBody().getEntity()).isEqualTo(response.getUuid());
    }

    @Test
    public void getInviteByBadParamUUID() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?entity=%s&entityType=goal", "invalid-uuid-format"), Invite.class);

        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Ignore
    @Test
    public void getInviteByInvalidGoal() throws Exception {
        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite?entity=%s&entityType=goal", DTOUtils.NON_VALID_UUID_STRING), Invite.class);

        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void deleteInvite() throws Exception {
        Goal response = this.restTemplate.postForObject("/goal",
                Goal.of(null, "first goal", DTOUtils.VALID_UUID), Goal.class);

        this.restTemplate.postForObject("/invite",
                Invite.of(DTOUtils.VALID_UUID,
                        response.getUuid(),
                        "goal",
                        "inviter",
                        "invitee"), Invite.class);

        ResponseEntity<Invite> inviteToDelete = this.restTemplate.getForEntity(
                format("/invite/%s", DTOUtils.VALID_UUID_STRING), Invite.class);

        assertThat(inviteToDelete).isNotNull();
        assertThat(inviteToDelete.getBody().getUuid()).isNotNull();

        this.restTemplate.delete(format("/invite/%s", inviteToDelete.getBody().getUuid().toString()));

        ResponseEntity<Invite> invite = this.restTemplate.getForEntity(
                format("/invite/%s", inviteToDelete.getBody().getUuid().toString()), Invite.class);

        assertThat(invite.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}
