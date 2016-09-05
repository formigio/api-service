package io.commitr.goal;

import org.springframework.stereotype.Service;

@Service
public class GoalService {
	public Goal createGoal(){
		return new Goal();
	}
	
	public Goal getGoal() {
		return new Goal();
	}
}
