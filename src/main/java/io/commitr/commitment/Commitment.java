package io.commitr.commitment;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Peter Douglas on 8/24/2016.
 */
@Entity
@Data
public class Commitment {
    @Id
    private String guid;
    private String title;
    private String shortText;
    private String longText;
}
