package io.commitr.task;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

/**
 * Created by peter on 9/15/16.
 */
public interface TaskDTO {
    UUID getUuid();

    String getTitle();

    @Value("#target.goal.uuid")
    UUID getGoal();

    Boolean getCompleted();
}
