package io.commitr.task;

import java.util.UUID;

/**
 * Created by peter on 9/14/16.
 */
public interface TaskService {
    public Task saveTask(TaskDTO dto);
    public void updateTask(TaskDTO dto);
    public TaskDTO getTask(UUID uuid);
    public void delete(UUID uuid);
}
