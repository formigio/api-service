package io.commitr.goal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, String>{
	
	public Goal findByGuid(String guid);
	
}
