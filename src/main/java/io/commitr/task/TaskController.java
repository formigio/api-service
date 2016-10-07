package io.commitr.task;

import io.commitr.annotation.ValidGoal;
import io.commitr.controller.ResourceNotFoundException;
import io.commitr.validator.ValidationGroups.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by peter on 9/14/16.
 */
@RestController
@RequestMapping(value = "/task")
@Validated
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Validated(Post.class) @RequestBody Task dto) {

        Task t = taskService.saveTask(dto);

        if (null==t) {
            throw new ResourceNotFoundException();
        }

        return t;
    }

    @GetMapping("/{uuid}")
    public Task getTask(@PathVariable UUID uuid) {
        Task t = taskService.getTask(uuid);

        if (null==t) {
            throw new ResourceNotFoundException();
        }

        return t;
    }

    @GetMapping
    public List<Task> getTasksByGoal(@Validated @ValidGoal @RequestParam(value = "goal") UUID uuid) {
        List<Task> tasks = taskService.getTaskByGoal(uuid);

        if(null==tasks) {
            throw new ResourceNotFoundException();
        }

        return tasks;
    }

    @PutMapping
    public void updateTask(@Validated @RequestBody Task dto) {
        Task t = taskService.updateTask(dto);

        if (null==t) {
            throw new ResourceNotFoundException();
        }
    }

    @DeleteMapping("/{uuid}")
    public void deleteTask(@PathVariable UUID uuid) {
        Task t = taskService.getTask(uuid);
        if(null != t) {
            taskService.delete(t);
        }
    }
}
