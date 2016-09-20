package io.commitr.task;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Task saveTask(TaskDTO task) {
        Task t = new Task();

        t.setUuid(task.getUuid());
        t.setTitle(task.getTitle());
        t.setGoal(goalRepository.findByUuid(task.getGoal()));
        t.setCompleted(task.getCompleted());

        taskRepository.save(t);

        return t;
    }

    @Override
    @Transactional
    public void updateTask(TaskDTO dto) {
        Task t = taskRepository.findByUuid(dto.getUuid());

        t.setTitle(dto.getTitle());
        t.setCompleted(dto.getCompleted());

        taskRepository.saveAndFlush(t);
    }

    @Override
    public TaskDTO getTask(UUID uuid) {
        Task t = taskRepository.findByUuid(uuid);

        TaskDTO dto = new TaskDTO();

        dto.setUuid(t.getUuid());
        dto.setTitle(t.getTitle());
        dto.setGoal(t.getGoal().getUuid());
        dto.setCompleted(t.getCompleted());

        return dto;
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        taskRepository.deleteByUuid(uuid);
    }

}
