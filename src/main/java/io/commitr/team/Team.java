package io.commitr.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
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

    @Length(max = 255)
    @NotNull
    private String title;

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

    public static Team of(String name, UUID uuid, UUID identity) {
        Team t = new Team();
        t.setTitle(name);
        t.setUuid(uuid);
        t.setIdentity(identity);
        return t;
    }

}
