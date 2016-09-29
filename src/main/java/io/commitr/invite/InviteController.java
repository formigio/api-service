package io.commitr.invite;

import io.commitr.controller.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
@RestController
@RequestMapping(value = "/invite")
public class InviteController {

    @Autowired
    InviteService inviteService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Invite createInvite(@RequestBody Invite invite) {
        Invite i = inviteService.saveInvite(invite);

        if(null==i) {
            throw new ResourceNotFoundException();
        }

        return i;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Invite getInviteByUuid(@PathVariable UUID uuid) {
        Invite i = inviteService.getInvite(uuid);

        if(null==i){
            throw new ResourceNotFoundException();
        }

        return i;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Invite getInviteByGoal(@RequestParam("goal") UUID uuid) {
        Invite i = inviteService.getInviteByGoal(uuid);

        if(null==i){
            throw new ResourceNotFoundException();
        }

        return i;
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteInvite(@PathVariable UUID uuid) {
        inviteService.deleteInvite(uuid);
    }
}
