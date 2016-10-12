package io.commitr.goal;

import java.util.List;
import java.util.UUID;

public interface GoalService {
	public Goal createGoal(Goal goal);
	
	public Goal getGoal(UUID uuid);

	public List<Goal> getGoalsByTask(UUID uuid);
}
