package io.commitr.goal;

import java.util.UUID;

public interface GoalService {
	public Goal createGoal(Goal goal);
	
	public Goal getGoal(UUID uuid);
}
