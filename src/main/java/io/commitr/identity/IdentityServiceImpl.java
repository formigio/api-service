package io.commitr.identity;

import io.commitr.team.Team;
import io.commitr.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by peter on 11/15/16.
 */
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    IdentityRepository repository;

    @Autowired
    TeamService teamService;

    @Override
    public Identity getIdentity(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public Identity saveIdenity(Identity identity) {

        return repository.save(identity);
    }
}
