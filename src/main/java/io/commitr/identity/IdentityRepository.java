package io.commitr.identity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by peter on 11/15/16.
 */
public interface IdentityRepository extends JpaRepository<Identity, Long>
{
    Identity findByUuid(UUID uuid);
}
