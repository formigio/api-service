package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.goal.GoalService;
import io.commitr.goal.GoalServiceImpl;
import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by Peter Douglas on 9/27/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InviteServiceTest {

    @Mock
    Invite inviteMock;

    @Mock
    Goal goalMock;

    @MockBean
    InviteRepository inviteRepository;

    @MockBean
    GoalService goalService;

    @Autowired
    InviteService service;

    @Before
    public void setUp() throws Exception {
        given(inviteRepository.save(inviteMock))
                .willReturn(inviteMock);

        given(inviteRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(inviteMock);

        given(inviteRepository.findByUuid(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        given(inviteRepository.findByGoal(DTOUtils.VALID_UUID))
                .willReturn(inviteMock);

        given(inviteRepository.findByGoal(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        given(goalService.getGoal(DTOUtils.VALID_UUID))
                .willReturn(goalMock);

        given(goalService.getGoal(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        when(inviteMock.getId()).thenReturn(1L);
        when(inviteMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
        when(inviteMock.getGoal()).thenReturn(DTOUtils.VALID_UUID);

        when(goalMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
    }

    @Test
    public void testSaveInviteWithValidGoal() throws Exception {
        Invite invite = service.saveInvite(inviteMock);

        assertThat(invite).isNotNull();
        verify(inviteRepository, times(1)).save(inviteMock);
    }

    @Test
    public void testSaveInviteWithNonValidGoal() throws Exception {

        Invite invite = service.saveInvite(Invite.of(DTOUtils.VALID_UUID, DTOUtils.NON_VALID_UUID));

        assertThat(invite).isNull();
    }

    @Test
    public void testFindWhenValidUUID() throws Exception {
        Invite invite = service.getInvite(DTOUtils.VALID_UUID);

        assertThat(invite).isNotNull();
    }

    @Test
    public void testFindWhenNonValidUUID() throws Exception {
        Invite invite = service.getInvite(DTOUtils.NON_VALID_UUID);

        assertThat(invite).isNull();

    }

    @Test
    public void testFindWhenValidGoal() throws Exception {
        Invite invite = service.getInviteByGoal(DTOUtils.VALID_UUID);

        assertThat(invite).isNotNull();
    }

    @Test
    public void testFindWhenNonValidGoal() throws Exception {
        Invite invite = service.getInviteByGoal(DTOUtils.NON_VALID_UUID);

        assertThat(invite).isNull();

    }

    @Test
    public void testDisassociateInvite() throws Exception {
        service.deleteInvite(DTOUtils.VALID_UUID);

        verify(inviteRepository).delete(inviteMock);

    }

    @Configuration
    public static class Config {
        @Mock
        InviteRepository inviteRepository;

        @Mock
        GoalRepository goalRepository;

        @Mock
        GoalServiceImpl goalService;

        @Primary
        @Bean
        GoalService goalService() {
            return this.goalService;
        }

        @Primary
        @Bean
        GoalRepository goalRepository() {
            return this.goalRepository;
        }

        @Bean
        InviteRepository inviteRepository() {
            return this.inviteRepository;
        }

        @Bean
        InviteService inviteService() {
            return new InviteServiceImpl();
        }
    }
}
