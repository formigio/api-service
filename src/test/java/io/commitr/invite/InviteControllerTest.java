package io.commitr.invite;

import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(InviteController.class)
@WithMockUser
public class InviteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InviteService service;

    @Before
    public void setUp() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("test");
        ctx.refresh();

        given(service.saveInvite(Invite.of(null, DTOUtils.VALID_UUID)))
                .willReturn(Invite.of(DTOUtils.VALID_UUID, DTOUtils.VALID_UUID));

        given(service.saveInvite(Invite.of(null, DTOUtils.NON_VALID_UUID)))
                .willReturn(null);

        given(service.getInvite(DTOUtils.VALID_UUID))
                .willReturn(Invite.of(DTOUtils.VALID_UUID, DTOUtils.VALID_UUID));

        given(service.getInvite(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        given(service.getInviteByGoal(DTOUtils.VALID_UUID))
                .willReturn(Invite.of(DTOUtils.VALID_UUID, DTOUtils.VALID_UUID));

        given(service.getInviteByGoal(DTOUtils.NON_VALID_UUID))
                .willReturn(null);
    }

    @Test
    public void testPostInviteWithValidGoal() throws Exception {
        String inviteJson = "{\n" +
                "    \"guid\":null,\n" +
                "    \"goal\":\"" + DTOUtils.VALID_UUID_STRING + "\"\n" +
                "}";

        this.mvc.perform(post("/invite")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(inviteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.goal", containsString(DTOUtils.VALID_UUID_STRING)));
    }

    @Test
    public void testPostInviteWithInvalidGoal() throws Exception {
        String inviteJson = "{\n" +
                "    \"guid\":null,\n" +
                "    \"goal\":\"" + DTOUtils.NON_VALID_UUID_STRING + "\"\n" +
                "}";

        this.mvc.perform(post("/invite")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(inviteJson))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetInviteWithValidInvite() throws Exception {
        this.mvc.perform(get(format("/invite/%s", DTOUtils.VALID_UUID_STRING))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.goal", containsString(DTOUtils.VALID_UUID_STRING)));

    }

    @Test
    public void testGetInviteWithInvalidInvite() throws Exception {
        this.mvc.perform(get(format("/invite/%s", DTOUtils.NON_VALID_UUID))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetInviteWithValidGoal() throws Exception {
        this.mvc.perform(get(format("/invite?goal=%s", DTOUtils.VALID_UUID_STRING))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.guid", containsString(DTOUtils.VALID_UUID_STRING)))
                .andExpect(jsonPath("$.goal", containsString(DTOUtils.VALID_UUID_STRING)));

    }

    @Test
    public void testGetInviteWithInvalidGoal() throws Exception {
        this.mvc.perform(get(format("/invite?goal=%s", DTOUtils.NON_VALID_UUID_STRING))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteInviteWithInviteUuid() throws Exception {
        this.mvc.perform(delete(format("/invite/%s", DTOUtils.VALID_UUID_STRING))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
