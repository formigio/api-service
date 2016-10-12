package io.commitr.goal;

import org.springframework.beans.factory.annotation.Autowired;
import io.commitr.controller.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository repository;

    @Override
    public Goal createGoal(Goal goal) {
        return repository.save(goal);
    }

    @Override
    public Goal getGoal(UUID uuid) {
        Goal g = repository.findByUuid(uuid);

        if (null==g) {
            throw new ResourceNotFoundException();
        }

        return g;
    }

    @Override
    public List<Goal> getGoalsByTeam(UUID uuid) {
        return repository.findByTeam(uuid);
    }
}
