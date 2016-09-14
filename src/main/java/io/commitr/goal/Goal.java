package io.commitr.goal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

@Data
@Entity
public class Goal {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	@NotNull
	@JsonProperty("guid")
	private UUID uuid;

	@Length(max = 255)
	private String title;

	@PrePersist
	void prePersist() {
		if (null==this.uuid) {
			this.uuid = UUID.randomUUID();
		}
	}
}
