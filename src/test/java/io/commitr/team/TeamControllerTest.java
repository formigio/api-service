package io.commitr.team;

import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Peter Douglas on 9/5/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TeamController.class)
@WithMockUser
public class TeamControllerTest {

    private static final String TITLE = "Team Test";
    private static final Team TEAM = Team.of(TITLE, DTOUtils.VALID_UUID, DTOUtils.VALID_UUID);

    @Value("${headers.value}")
    private String headerValue;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamService service;

    @Before
    public void setUp() throws Exception {
        given(this.service.saveTeam(TEAM))
                .willReturn(TEAM);
        given(this.service.getTeam(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(TEAM).collect(Collectors.toList()));
        given(this.service.getTeam(DTOUtils.NON_VALID_UUID))
                .willReturn(null);
        given(this.service.getTeamByIdentity(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(TEAM, TEAM).collect(Collectors.toList()));
        given(this.service.getTeamByIdentity(UUID.fromString(headerValue.split(":")[1])))
                .willReturn(Stream.of(TEAM).collect(Collectors.toList()));
        given(this.service.getTeamByIdentity(DTOUtils.NON_VALID_UUID))
                .willReturn(null);
    }

    @Test
    public void postTeam() throws Exception {
        this.mvc.perform(post("/team")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(TEAM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.title",containsString(TITLE)))
                .andExpect(jsonPath("$.uuid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.identity", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void putTeam() throws Exception {
        this.mvc.perform(put("/team")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(TEAM)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.title",containsString(TITLE)))
                .andExpect(jsonPath("$.uuid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.identity", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void deleteTeam() throws Exception {
        this.mvc.perform(delete("/team/" + DTOUtils.VALID_UUID_STRING)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void getTeamWithValidUUID() throws Exception {
        this.mvc.perform(get("/team/" + DTOUtils.VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].title",containsString(TITLE)))
                .andExpect(jsonPath("$[0].uuid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$[0].identity", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void getTeamWithNonValidUUID() throws Exception {
        this.mvc.perform(get("/team/" + DTOUtils.NON_VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTeamWithInvalidUUID() throws Exception {
        this.mvc.perform(get("/team/invalid-uuid-format")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

    }

    @Ignore
    @Test
    public void getTeamsWithValidIdentity() throws Exception {
        this.mvc.perform(get("/team")
                .header("x-identity-id", headerValue)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(1)));

    }

    @Test
    public void getTeamsWithMissingHeader() throws Exception {
        this.mvc.perform(get("/team")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
