package io.commitr.team;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.util.DTOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Predicates.and;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

    private final UUID VALID_IDENTITY = UUID.randomUUID();

    @Autowired
    private TeamRepository repository;

    @Test
    public void createTeam() throws Exception {
        repository.save(
                Team.of("Team One",
                        null,
                        DTOUtils.VALID_UUID));
    }

    @Test
    public void findTeamByUuid() throws Exception {
        repository.save(
                Team.of("Team To Find",
                        DTOUtils.VALID_UUID,
                        DTOUtils.VALID_UUID));

        Team team = repository.findByUuid(DTOUtils.VALID_UUID);

        assertThat(team).isNotNull();
        assertThat(team.getTitle()).isEqualTo("Team To Find");
    }

    @Test
    public void findTeamByNonValidUuid() throws Exception {
        Team team = repository.findByUuid(DTOUtils.NON_VALID_UUID);

        assertThat(team).isNull();
    }

    @Test
    public void findTeamByIdentity() throws Exception {
        repository.save(
                Team.of("Identity1",
                        null,
                        VALID_IDENTITY));

        repository.save(
                Team.of("Identity2",
                        null,
                        VALID_IDENTITY));

        List<Team> teams = repository.findByIdentity(VALID_IDENTITY);

        assertThat(teams).isNotNull();
        assertThat(teams.size()).isEqualTo(2);
        Assert.assertThat(teams.stream()
                .map(team -> team.getTitle())
                .collect(Collectors.toList()), containsInAnyOrder("Identity1", "Identity2"));
    }

    @Test
    public void findTeamByNonValidIdentity() throws Exception {
        List<Team> teams = repository.findByIdentity(DTOUtils.NON_VALID_UUID);

        assertThat(teams).isEmpty();
    }

    @Test
    public void updateTeamTitle() throws Exception {
        Team t = repository.save(
                Team.of("Title To Change",
                        DTOUtils.VALID_UUID,
                        DTOUtils.VALID_UUID));

        t.setTitle("Updated Title");

        Team teamUpdated = repository.save(t);

        assertThat(teamUpdated.getTitle()).isEqualTo("Updated Title");

    }
}
