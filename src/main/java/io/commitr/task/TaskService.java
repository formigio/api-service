package io.commitr.task;

import java.util.UUID;

/**
 * Created by peter on 9/14/16.
 */
public interface TaskService {
    public Task createTask(Task task);
    public Task getTask(UUID uuid);
    public Task updateTask(Task task);
    public void delete(Task task);
}
