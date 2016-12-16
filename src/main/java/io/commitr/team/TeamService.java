package io.commitr.team;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 10/8/2016.
 */
public interface TeamService {
    public Team saveTeam(Team team);
    public List<Team> getTeam(UUID uuid);
    public Team updateTeam(Team team);
    public List<Team> getTeamByIdentity(UUID uuid);
    public void deleteTeam(Team team);
}
