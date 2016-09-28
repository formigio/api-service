package io.commitr.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by peter on 9/24/16.
 */
public interface InviteRepository extends JpaRepository<Invite, Long>{

    Invite findByUuid(UUID uuid);

    Invite findByGoal(UUID uuid);
}
