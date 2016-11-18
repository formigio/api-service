package io.commitr.identity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by peter on 11/15/16.
 */
@Entity
@Data
public class Identity {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private String provider;

    @PrePersist
    void prePersist() {
        if (null==this.uuid) {
            this.uuid = UUID.randomUUID();
        }
    }

    public static Identity of(UUID uuid, String provider) {
        Identity i = new Identity();
        i.setUuid(uuid);
        i.setProvider(provider);
        return i;
    }

}
