package io.commitr.commitment;

import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by Peter Douglas on 8/24/2016.
 */

public interface CommitmentRepository extends Repository<Commitment, String> {
    Commitment findByGuid(String guid);
    List<Commitment> findAll();
}
