package io.commitr.goal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID>{
	
	public Goal findByUuid(UUID uuid);

	public List<Goal> findByTeam(UUID uuid);
}
