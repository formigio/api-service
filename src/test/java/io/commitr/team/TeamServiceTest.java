package io.commitr.team;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import io.commitr.goal.GoalService;
import io.commitr.goal.GoalServiceImpl;
import io.commitr.util.DTOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamServiceTest {

    @Mock
    Team teamMock;

    @MockBean
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Before
    public void setUp() throws Exception {
        given(this.teamRepository.save(teamMock))
                .willReturn(Team.of("Test Team", DTOUtils.VALID_UUID, DTOUtils.VALID_UUID));
        given(this.teamRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(Team.of("Test Team", DTOUtils.VALID_UUID, DTOUtils.VALID_UUID));
        given(this.teamRepository.findByIdentity(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(teamMock, teamMock).collect(Collectors.toList()));

        when(teamMock.getTitle()).thenReturn("Test Team");
        when(teamMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);
        when(teamMock.getIdentity()).thenReturn(DTOUtils.VALID_UUID);
    }

    @Test
    public void saveTeam() throws Exception {
        Team team = teamService.saveTeam(teamMock);

        assertThat(team).isNotNull();
        assertThat(team.getUuid()).isNotNull();
        assertThat(team.getIdentity()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getTitle()).isEqualTo("Test Team");
    }

    @Test
    public void getTeamByUuid() throws Exception {
        Team team = teamService.getTeam(DTOUtils.VALID_UUID);

        assertThat(team).isNotNull();
        assertThat(team.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getIdentity()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getTitle()).isEqualTo("Test Team");
    }

    @Test
    public void getTeamByNonValidUuid() throws Exception {
        Team team = teamService.getTeam(DTOUtils.NON_VALID_UUID);

        assertThat(team).isNull();

    }

    @Test
    public void getTeamByIdentity() throws Exception {
        List<Team> teams = teamService.getTeamByIdentity(DTOUtils.VALID_UUID);

        assertThat(teams).isNotNull();
        assertThat(teams.size()).isEqualTo(2);
        assertThat(teams.stream()
                .map(team -> team.getTitle())
                .reduce(" ", String::concat).contains("Team Mock Title"));
    }

    @Test
    public void getTeamByNonValidIdentity() throws Exception {
        List<Team> teams = teamService.getTeamByIdentity(DTOUtils.NON_VALID_UUID);

        assertThat(teams).isEmpty();
    }

    @Test
    public void updateTeam() throws Exception {
        Team team = teamService.updateTeam(teamMock);

        assertThat(team.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getIdentity()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getTitle()).isEqualTo("Test Team");
    }

    @Configuration
    static class ServiceConfig {
        @Mock
        TeamRepository repository;

        @Primary
        @Bean
        public TeamRepository teamRepository() {
            return repository;
        }

        @Bean
        public TeamService teamService() {
            return new TeamServiceImpl();
        }
    }

}