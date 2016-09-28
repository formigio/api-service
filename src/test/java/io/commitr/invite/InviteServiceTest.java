package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

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
    GoalRepository goalRepository;

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

        given(goalRepository.save(goalMock))
                .willReturn(goalMock);

        given(goalRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(goalMock);

        given(goalRepository.findByUuid(DTOUtils.NON_VALID_UUID))
                .willReturn(null);

        when(inviteMock.getId()).thenReturn(anyLong());
        when(inviteMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
        when(inviteMock.getGoal()).thenReturn(DTOUtils.VALID_UUID);

        when(goalMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
    }

    @Test
    public void testSaveInviteWithValidGoal() throws Exception {
            Invite invite = service.saveInvite(inviteMock);

        assertThat(invite).isNotNull();
    }

    @Test
    public void testSaveInviteWithNonValidGoal() throws Exception {

        Invite invite = service.saveInvite(DTOUtils.createInvite(DTOUtils.VALID_UUID, DTOUtils.NON_VALID_UUID));

        assertThat(invite).isNull();
    }

    @Test
    public void testFindWhenValidUUID() throws Exception {


    }

    @Test
    public void testFindWhenNonValidUUID() throws Exception {


    }

    @Test
    public void testFindWhenValidGoal() throws Exception {


    }

    @Test
    public void testFindWhenNonValidGoal() throws Exception {


    }

    @Test
    public void testDisassociateInvite() throws Exception {


    }
}
