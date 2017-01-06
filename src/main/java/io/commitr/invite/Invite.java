package io.commitr.invite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.commitr.annotation.ValidGoal;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by peter on 9/24/16.
 */
@Data
@Entity
public class Invite {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private UUID entity;

    @NotNull
    private String entityType;

    @NotNull
    private String invitee;

    @NotNull
    private String inviter;

    @PrePersist
    void prePersist() {
        if (null==this.uuid) {
            this.uuid = UUID.randomUUID();
        }
    }

    public static Invite of(UUID uuid, UUID entity, String entityType, String intivee, String inviter) {
        Invite i = new Invite();
        i.setUuid(uuid);
        i.setEntity(entity);
        i.setEntityType(entityType);
        i.setInviter(inviter);
        i.setInvitee(intivee);
        return i;
    }

}
