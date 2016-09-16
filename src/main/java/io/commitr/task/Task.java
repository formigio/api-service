package io.commitr.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.prism.shader.DrawCircle_Color_AlphaTest_Loader;
import io.commitr.goal.Goal;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
    @JoinColumn(name = "goal_uuid", nullable = false, updatable = false)
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
