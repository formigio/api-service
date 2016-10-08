package io.commitr.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 10/8/2016.
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository repository;

    @Override
    public Team saveTeam(Team team) {
        return repository.save(team);
    }

    @Override
    public Team getTeam(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public Team updateTeam(Team team) {
        return saveTeam(team);
    }

    @Override
    public List<Team> getTeamByIdentity(UUID uuid) {
        return repository.findByIdentity(uuid);
    }

    @Override
    public void deleteTeam(Team team) {
        repository.delete(team);
    }
}
