package io.commitr.goal;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GoalRepositoryTest {

    private final UUID VALID_TEAM = UUID.randomUUID();

    @Autowired
    private GoalRepository repository;

    @Test
    public void createGoal() throws Exception {
        repository.save(
                Goal.of(null,
                        "Test Goal",
                        DTOUtils.VALID_UUID));
    }

    @Test
    public void findGoal() throws Exception {
        repository.save(
                Goal.of(DTOUtils.VALID_UUID,
                        "Test Goal",
                        DTOUtils.VALID_UUID));

        Goal goal = repository.findByUuid(DTOUtils.VALID_UUID);

        assertThat(goal.getUuid().toString())
                .containsPattern("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        assertThat(goal.getTitle()).isEqualTo("Test Goal");
    }

    @Test
    public void findGoalsByTeam() throws Exception {
        repository.save(
                Goal.of(null,
                        "Test Goal",
                        VALID_TEAM));
        repository.save(
                Goal.of(null,
                        "Test Goal",
                        VALID_TEAM));

        List<Goal> goalsByTeam = repository.findByTeam(VALID_TEAM);

        assertThat(goalsByTeam.size()).isEqualTo(2);

    }

    @Test
    public void findGoalsByNonValidTeam() throws Exception {
        List<Goal> goalsByTeam = repository.findByTeam(DTOUtils.NON_VALID_UUID);

        assertThat(goalsByTeam).isNotNull();
        assertThat(goalsByTeam.size()).isZero();

    }
}
