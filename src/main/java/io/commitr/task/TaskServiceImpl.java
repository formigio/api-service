package io.commitr.task;

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
    TaskRepository repository;

    @Override
    @Transactional
    public Task saveTask(Task task) {
        return repository.save(task);
    }

    @Override
    public TaskDTO getTask(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }
}
