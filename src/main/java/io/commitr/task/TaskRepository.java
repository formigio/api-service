package io.commitr.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Set;
import java.util.UUID;

/**
 * Created by peter on 9/13/16.
 */

public interface TaskRepository extends JpaRepository<Task, UUID>{

    Task findByUuid(UUID uuid);

    @Modifying
    Long deleteByUuid(UUID uuid);

    Set<Task> findByGoal(UUID goal);
}
