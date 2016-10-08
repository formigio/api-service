package io.commitr.team;

import org.springframework.beans.factory.annotation.Autowired;
import io.commitr.controller.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Peter Douglas on 10/8/2016.
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@RequestBody Team team) {
        return service.saveTeam(team);
    }

    @PutMapping
    public Team updateTeam(@RequestBody Team team) {
        return service.saveTeam(team);
    }

    @DeleteMapping("/{uuid}")
    public void deleteTeam(@PathVariable UUID uuid) {
        Team t = service.getTeam(uuid);

        service.deleteTeam(t);
    }

    @GetMapping("/{uuid}")
    public Team getTeam(@PathVariable UUID uuid) {
        Team team = service.getTeam(uuid);

        if(Objects.isNull(team)) {
            throw new ResourceNotFoundException();
        }

        return team;
    }

    @GetMapping
    public List<Team> getTeamByIdentity(@RequestParam("identity") UUID uuid) {
        List<Team> teams = service.getTeamByIdentity(uuid);

        if(Objects.isNull(teams) || teams.size() == 0) {
            throw new ResourceNotFoundException();
        }

        return teams;
    }
}
