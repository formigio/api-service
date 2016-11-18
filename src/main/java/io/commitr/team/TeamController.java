package io.commitr.team;

import io.commitr.identity.Identity;
import io.commitr.identity.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import io.commitr.controller.ResourceNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Peter Douglas on 10/8/2016.
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService service;

    @Autowired
    private MessageSource messageSource;

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
    public List<Team> getTeamByIdentity(@RequestHeader("x-identity-id") @NotNull Identity identity) {

        List<Team> teams = service.getTeamByIdentity(identity.getUuid());

        if(Objects.isNull(teams) || teams.size() == 0) {
            teams = Stream.of(service.saveTeam(
                    Team.of(messageSource.getMessage("team.default.name", null, Locale.getDefault()),
                            null,
                            identity.getUuid())))
                    .collect(Collectors.toList());
        }

        return teams;
    }
}
