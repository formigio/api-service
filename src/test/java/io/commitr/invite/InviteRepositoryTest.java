package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * Created by peter on 9/24/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class InviteRepositoryTest {

    @Autowired
    InviteRepository repository;

    @Autowired
    GoalRepository goalRepository;

    @Test
    public void testSaveInvite() throws Exception {
        Goal g = goalRepository.save(Goal.of(null, "Test Goal", DTOUtils.VALID_UUID));

        Invite invite = repository.save(Invite.of(null, g.getUuid(), "goal", "invitee", "inviter"));

        assertThat(invite.getId()).isGreaterThan(0L);
        assertThat(invite.getUuid()).isNotNull();

    }

    @Test
    public void testFindInviteByUuid() throws Exception {
        Goal g = goalRepository.save(Goal.of(null, "Test Goal", DTOUtils.VALID_UUID));

        Invite i = Invite.of(null, g.getUuid(), "goal", "invitee", "inviter");

        repository.save(i);

        Invite invite = repository.findByUuid(i.getUuid());

        assertThat(invite.getUuid()).isEqualTo(i.getUuid());

    }

    @Test
    public void testFindInviteByGoal() throws Exception {
        Goal g = goalRepository.save(Goal.of(null, "Test Goal", DTOUtils.VALID_UUID));

        Invite i = Invite.of(null, g.getUuid(), "goal", "invitee", "inviter");

        repository.save(i);

        List<Invite> invite = repository.findByEntityAndEntityType(g.getUuid(), "goal");

        assertThat(invite.size()).isEqualTo(1);
        assertThat(invite.get(0).getEntity()).isEqualTo(g.getUuid());

    }

    @Test
    public void testFindGoalByInvite() throws Exception {
        Goal g = goalRepository.save(Goal.of(null, "Test Goal", DTOUtils.VALID_UUID));

        Invite i = Invite.of(null, g.getUuid(), "goal", "invitee", "inviter");

        repository.save(i);

        Invite invite = repository.findByUuid(i.getUuid());

        Goal goal = goalRepository.findByUuid(invite.getEntity());

        assertThat(goal).isNotNull();
        assertThat(goal.getTitle()).isEqualTo("Test Goal");

    }

    @Test
    public void testDelete() throws Exception {
        Goal g = goalRepository.save(Goal.of(null, "Test Goal", DTOUtils.VALID_UUID));

        Invite i = Invite.of(null, g.getUuid(), "goal", "invitee", "inviter");

        repository.saveAndFlush(i);

        repository.delete(i);
    }
}
