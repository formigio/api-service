package io.commitr.invite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @JsonProperty(value = "guid")
    @NotNull
    private UUID uuid;

    @NotNull
    private UUID goal;
}
