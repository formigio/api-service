package io.commitr.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.commitr.annotation.ValidGoal;
import io.commitr.validator.ValidationGroups.Post;
import io.commitr.validator.ValidationGroups.Put;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by peter on 9/13/16.
 */
@Data
@Entity
public class Task {

    @Id
    @GenericGenerator(
            name = "task_sequence",
            strategy = "sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "sequence"
                    )

            })
    @GeneratedValue(generator = "task_sequence")
    @JsonIgnore
    private Long id;

    @NotNull(groups = {Put.class})
    private UUID uuid;

    @Length(max = 255)
    private String title;

    @NotNull
    @ValidGoal
    private UUID goal;

    private Boolean completed;

    @PrePersist
    void prePersist() {
        if (null==this.uuid) {
            this.uuid = UUID.randomUUID();
        }

        if (null==completed) {
            completed = false;
        }
    }

    public static Task of(UUID uuid, String title, UUID goal, Boolean completed) {
        Task t = new Task();
        t.setUuid(uuid);
        t.setTitle(title);
        t.setGoal(goal);
        t.setCompleted(completed);
        return t;
    }
}
