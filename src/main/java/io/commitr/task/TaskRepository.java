package io.commitr.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by peter on 9/13/16.
 */

public interface TaskRepository extends JpaRepository<Task, UUID>{

    public Task findByUuid(UUID uuid);
}
