package io.commitr.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by peter on 10/1/16.
 */
@Data
@Entity
public class Team {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Max(value = 255)
    @NotNull
    private String name;

    @NotNull
    private UUID uuid;

    @NotNull
    private UUID identity;

    @PrePersist
    void prePersist() {
        if (null==this.uuid) {
            this.uuid = UUID.randomUUID();
        }
    }

}
