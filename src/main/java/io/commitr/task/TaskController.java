package io.commitr.task;

import io.commitr.controller.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by peter on 9/14/16.
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task dto) {

        Task t = taskService.saveTask(dto);

        if (null==t) {
            throw new ResourceNotFoundException();
        }

        return t;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public Task getTask(@PathVariable UUID uuid) {
        Task t = taskService.getTask(uuid);

        if (null==t) {
            throw new ResourceNotFoundException();
        }

        return t;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Task> getTasksByGoal(@RequestParam(value = "goal") UUID uuid) {
        List<Task> tasks = taskService.getTaskByGoal(uuid);

        if(null==tasks) {
            throw new ResourceNotFoundException();
        }

        return tasks;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateTask(@RequestBody Task dto) {
        Task t = taskService.updateTask(dto);

        if (null==t) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable UUID uuid) {
        taskService.delete(uuid);
    }
}
