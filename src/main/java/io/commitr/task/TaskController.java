package io.commitr.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TaskDTO createTask(@RequestBody TaskDTO dto) {

        Task t = taskService.saveTask(dto);

        TaskDTO responseDTO = new TaskDTO();

        responseDTO.setUuid(t.getUuid());
        responseDTO.setTitle(t.getTitle());
        responseDTO.setGoal(t.getGoal().getUuid());
        responseDTO.setCompleted(t.getCompleted());

        return responseDTO;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public TaskDTO getTask(@PathVariable UUID uuid) {
        return taskService.getTask(uuid);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateTask(@RequestBody TaskDTO dto) {
        taskService.updateTask(dto);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable UUID uuid) {
        taskService.delete(uuid);
    }
}
