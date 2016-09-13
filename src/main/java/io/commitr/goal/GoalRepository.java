package io.commitr.goal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, String>{
	
	public Goal findByGuid(UUID guid);
}
