package io.commitr.goal;

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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoalServiceTest {

    @Mock
    Goal goalMock;

    @MockBean
    private GoalRepository goalRepository;

    @Autowired
    private GoalService goalService;

    @Before
    public void setUp() throws Exception {
        given(this.goalRepository.save(goalMock))
                .willReturn(goalMock);
        given(this.goalRepository.findByUuid(DTOUtils.VALID_UUID))
                .willReturn(goalMock);
        given(this.goalRepository.findByTeam(DTOUtils.VALID_UUID))
                .willReturn(Stream.of(goalMock, goalMock).collect(Collectors.toList()));

        when(goalMock.getTitle()).thenReturn("Test Goal");
        when(goalMock.getUuid()).thenReturn(DTOUtils.VALID_UUID);

    }

    @Test
    public void createGoal() throws Exception {

        Goal goal = goalService.createGoal(goalMock);

        assertThat(goal.getUuid()).isNotNull();
        assertThat(goal.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(goal.getTitle()).isEqualTo("Test Goal");
    }

    @Test
    public void getGoal() throws Exception {

        Goal goal = goalService.getGoal(DTOUtils.VALID_UUID);

        assertThat(goal.getUuid()).isNotNull();
        assertThat(goal.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(goal.getTitle()).isEqualTo("Test Goal");
    }

    @Test
    public void getGoalsByTeam() throws Exception {
        List<Goal> goalsByTeam = goalService.getGoalsByTeam(DTOUtils.VALID_UUID);

        assertThat(goalsByTeam).isNotNull();
        assertThat(goalsByTeam.size()).isEqualTo(2);
        assertThat(goalsByTeam.stream()
                .map(team -> team.getTitle())
                .reduce(" ", String::concat)).contains("Test Goal");

    }

    @Test
    public void getGoalsByNonValidTeam() throws Exception {
        List<Goal> emptyGoals = goalService.getGoalsByTeam(DTOUtils.NON_VALID_UUID);

        assertThat(emptyGoals).isEmpty();
    }

    @Configuration
    static class ServiceConfig {
        @Mock
        GoalRepository repository;

        @Primary
        @Bean
        public GoalRepository goalRepository() {
            return repository;
        }

        @Bean
        public GoalService goalService() {
            return new GoalServiceImpl();
        }
    }

}