package io.commitr.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
public class Goal {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	@NotNull
	private UUID uuid;

	@Length(max = 255)
	private String title;

	@NotNull
	private UUID team;

	@PrePersist
	void prePersist() {
		if (null==this.uuid) {
			this.uuid = UUID.randomUUID();
		}
	}

	public static Goal of(UUID uuid, String title, UUID team) {
		Goal g = new Goal();
		g.setUuid(uuid);
		g.setTitle(title);
		g.setTeam(team);
		return g;
	}
}
