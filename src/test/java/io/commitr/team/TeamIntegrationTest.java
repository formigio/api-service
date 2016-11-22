package io.commitr.team;

import io.commitr.util.AbstractIntegrationTest;
import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/12/16.
 */
public class TeamIntegrationTest extends AbstractIntegrationTest {

    private final UUID VALID_IDENTITY = UUID.fromString(DTOUtils.VALID_UUID_STRING.replace('a','f'));

    @Value("${headers.value}")
    private String headerIdentity;

    @Test
    public void createTeamWithoutUUID() {
        Team response = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, DTOUtils.VALID_UUID),
                Team.class);

        assertThat(response.getUuid()).isNotNull();

    }

    @Test
    public void createTeamWithUUID() {
        Team response = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, DTOUtils.VALID_UUID),
                Team.class);

        assertThat(response.getUuid()).isNotNull();
    }

    @Test
    public void createTeamWithInvalidGuid() throws Exception {

        String content = "{" +
                "    \"uuid\":\"invalid-string-format\"," +
                "    \"title\":\"Test Team\"" +
                "    \"identity\":\"invalid-string-format\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<String>(content, headers);

        ResponseEntity<Team> goalResponse = restTemplate.exchange(new URI("http://localhost:" + port + "/team"), HttpMethod.POST, request, Team.class);

        assertThat(goalResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getTeam() throws Exception {
        Team response = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, DTOUtils.VALID_UUID),
                Team.class);

        Team team = this.restTemplate.getForObject(format("/team/%s", response.getUuid().toString()), Team.class);

        assertThat(team.getUuid()).isEqualByComparingTo(response.getUuid());
    }

    @Test
    public void getNonExistentTeam() throws Exception {

        ResponseEntity<Team> response = this.restTemplate.getForEntity(format("/team/%s", DTOUtils.NON_VALID_UUID_STRING), Team.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void getTeamWithInitialTeamGet() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("x-identity-id", "region-us-1:" + DTOUtils.VALID_UUID_STRING);

        HttpEntity<String> request = new HttpEntity<String>(
                new String(JsonUtils.convertObject(Team.of("test first header", null, null))), headers);

        ResponseEntity<Team[]> team = restTemplate.exchange(new URI("http://localhost:" + port + "/team"), HttpMethod.GET, request, Team[].class);

        assertThat(team.getBody().length).isEqualTo(1);
        assertThat(team.getBody()[0].getIdentity()).isEqualTo(DTOUtils.VALID_UUID);
    }

    @Test
    public void getTeamByValidIdentity() throws Exception {

        UUID headerUUID = UUID.fromString(headerIdentity.split(":")[1]);

        Team t1 = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, headerUUID),
                Team.class);

        Team t2 = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, headerUUID),
                Team.class);

        Team[] teams = this.restTemplate.getForObject("/team", Team[].class);

        assertThat(teams.length).isEqualTo(2);
        assertThat(teams).containsExactlyInAnyOrder(t1, t2);

    }
}
