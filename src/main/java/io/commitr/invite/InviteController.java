package io.commitr.invite;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import io.commitr.controller.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
@RestController
@RequestMapping(value = "/invite")
public class InviteController {

    @Autowired
    InviteService inviteService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Invite createInvite(@RequestBody Invite invite) {
        Invite i = inviteService.saveInvite(invite);

        if(null==i) {
            throw new ResourceNotFoundException();
        }

        return i;
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(value = HttpStatus.OK)
    public Invite getInviteByUuid(@PathVariable UUID uuid) {
        Invite i = inviteService.getInvite(uuid);

        if(null==i){
            throw new ResourceNotFoundException();
        }

        return i;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Invite> getInviteByEntity(@RequestParam("entity") UUID entity,
                                    @RequestParam("entityType") String entityType) {
        List<Invite> i = inviteService.getInviteByEntityAndEntityType(entity, entityType);

        return i;
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteInvite(@PathVariable UUID uuid) {
        inviteService.deleteInvite(uuid);
    }
}
