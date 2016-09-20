package io.commitr.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Created by peter on 9/17/16.
 */
@Data
public class TaskDTO implements TaskDTOInterface {
    @JsonProperty("guid")
    private UUID uuid;
    private String title;
    private UUID goal;
    private Boolean completed;
}