package io.commitr.goal;

import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Goal getGoal() {
        return null;
    }
}
