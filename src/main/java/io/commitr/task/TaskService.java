package io.commitr.task;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by peter on 9/14/16.
 */
public interface TaskService {
    public Task saveTask(Task dto);
    public Task updateTask(Task dto);
    public Task getTask(UUID uuid);
    public List<Task> getTaskByGoal(UUID uuid);
    public void delete(UUID uuid);
}
