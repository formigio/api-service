package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

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
        Goal g = goalRepository.save(DTOUtils.createGoal(null, "Test Goal"));

        Invite invite = repository.save(DTOUtils.createInvite(null, g.getUuid()));

        assertThat(invite.getId()).isGreaterThan(0L);
        assertThat(invite.getUuid()).isNotNull();

    }

    @Test
    public void testFindInviteByUuid() throws Exception {
        Goal g = goalRepository.save(DTOUtils.createGoal(null, "Test Goal"));

        Invite i = DTOUtils.createInvite(null, g.getUuid());

        repository.save(i);



    }

    @Test
    public void testFindInviteByGoal() throws Exception {


    }

    @Test
    public void testDeleteSave() throws Exception {


    }
}
