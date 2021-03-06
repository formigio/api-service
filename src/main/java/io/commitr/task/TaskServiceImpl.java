package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by peter on 9/15/16.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    GoalRepository goalRepository;

    @Override
    @Transactional
    public Task saveTask(Task task) {
        Goal g = goalRepository.findByUuid(task.getGoal());

        if (null==g) {
            return null;
        }

        taskRepository.saveAndFlush(task);

        return task;
    }

    @Override
    @Transactional
    public Task updateTask(Task dto) {
        Task t = taskRepository.findByUuid(dto.getUuid());

        if(null==t) {
            return t;
        }

        t.setTitle(dto.getTitle());
        t.setCompleted(dto.getCompleted());

        taskRepository.saveAndFlush(t);

        return t;
    }

    @Override
    public Task getTask(UUID uuid) {
        return taskRepository.findByUuid(uuid);
    }

    @Override
    public List<Task> getTaskByGoal(UUID uuid) {

        Goal g = goalRepository.findByUuid(uuid);

        if (null==g) {
            return null;
        }

        return taskRepository.findByGoal(uuid);
    }

    @Override
    @Transactional
    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
