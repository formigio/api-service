package io.commitr.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.commitr.goal.Goal;
import lombok.Data;
import lombok.NonNull;
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
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @JsonProperty("guid")
    private UUID uuid;

    @Length(max = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "goal_uuid")
    private Goal goal;

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
}
