package io.commitr.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 10/8/2016.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
    public List<Team> findByUuid(UUID uuid);
    public List<Team> findByIdentity(UUID uuid);
}
