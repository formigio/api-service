package io.commitr.goal;

import io.commitr.util.DTOUtils;
import io.commitr.util.JsonUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Peter Douglas on 9/5/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GoalController.class)
public class GoalControllerTest {


    private final Goal GOAL_RESPONSE = Goal.of(DTOUtils.VALID_UUID, "Goal Test", DTOUtils.VALID_UUID);
    private final Goal GOAL_REQUEST = Goal.of(null, "Goal Test", DTOUtils.VALID_UUID);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private GoalService service;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        given(this.service.createGoal(GOAL_REQUEST))
                .willReturn(GOAL_RESPONSE);
        given(this.service.getGoal(DTOUtils.VALID_UUID))
                .willReturn(GOAL_RESPONSE);
        given(this.service.getGoalsByTeam(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(GOAL_RESPONSE, GOAL_RESPONSE)
                        .collect(Collectors.toList()));

    }

    @Test
    public void setGoal() throws Exception {

        this.mvc.perform(post("/goal")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.convertObject(GOAL_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Goal Test")))
                .andExpect(jsonPath("$.team", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void getGoalWithValidUUID() throws Exception {

        this.mvc.perform(get("/goal/" + DTOUtils.VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.title", containsString("Goal Test")));
    }

    @Test
    public void getGoalWithInvalidGuid() throws Exception {
        this.mvc.perform(get("/goal/invalid-uuid-format")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void getGoalByValidTeam() throws Exception {
        this.mvc.perform(get("/goal?team=" + DTOUtils.VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].title", containsInAnyOrder("Goal Test", "Goal Test")));
    }

    @Test
    public void getGoalByNonValidTeam() throws Exception {
        this.mvc.perform(get("/goal?team=" + DTOUtils.NON_VALID_UUID_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getGoalByInvalidTeamUUID() throws Exception {
        this.mvc.perform(get("/goal?team=invalid-uuid-format")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }

    private Authentication getOauthTestAuthentication() {
        return new OAuth2Authentication(getOauth2Request(), getAuthentication());
    }
    private OAuth2Request getOauth2Request () {
        String clientId = "oauth-client-id";
        Map<String, String> requestParameters = Collections.emptyMap();
        boolean approved = true;
        String redirectUrl = "http://my-redirect-url.com";
        Set<String> responseTypes = Collections.emptySet();
        Set<String> scopes = Collections.emptySet();
        Set<String> resourceIds = Collections.emptySet();
        Map<String, Serializable> extensionProperties = Collections.emptyMap();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities,
                approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);

        return oAuth2Request;
    }

    private Authentication getAuthentication() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");

        User userPrincipal = new User("user", "", true, true, true, true, authorities);

        HashMap<String, String> details = new HashMap<String, String>();
        details.put("user_name", "bwatkins");
        details.put("email", "bwatkins@test.org");
        details.put("name", "Brian Watkins");

        TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);
        token.setDetails(details);

        return token;
    }

    private OAuth2ClientContext getOauth2ClientContext () {
        OAuth2ClientContext mockClient = mock(OAuth2ClientContext.class);
        when(mockClient.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("my-fun-token"));

        return mockClient;
    }
}
