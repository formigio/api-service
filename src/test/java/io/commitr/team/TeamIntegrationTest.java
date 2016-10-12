package io.commitr.team;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamIntegrationTest {

    private final UUID VALID_IDENTITY = UUID.fromString(DTOUtils.VALID_UUID_STRING.replace('a','f'));

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

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
    public void getTeamByValidIdentity() throws Exception {
        Team t1 = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, VALID_IDENTITY),
                Team.class);

        Team t2 = this.restTemplate.postForObject("/team",
                Team.of("Test Team", null, VALID_IDENTITY),
                Team.class);

        Team[] teams = this.restTemplate.getForObject(format("/team?identity=%s", VALID_IDENTITY.toString()), Team[].class);

        assertThat(teams.length).isEqualTo(2);
        assertThat(teams).containsExactlyInAnyOrder(t1, t2);

    }
}
