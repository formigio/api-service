package io.commitr.task;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

/**
 * Created by peter on 9/19/16.
 */
public interface TaskDTOInterface {
        UUID getUuid();

        String getTitle();

        @Value("#target.goal.uuid")
        UUID getGoal();

        Boolean getCompleted();
}
