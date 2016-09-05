package io.commitr.goal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Goal {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;
	private String guid;
	private String title;
}
