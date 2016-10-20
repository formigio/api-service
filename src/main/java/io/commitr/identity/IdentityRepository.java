package io.commitr.identity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by peterfdouglas on 10/19/2016.
 */
public interface IdentityRepository extends JpaRepository<Identity, Long>{

    public Identity findByUuid(UUID uuid);

    public Identity findByPrincipleId(String principleId);

    public Identity findByProviderIdAndPrincipleId(String provider, String principle);
}
