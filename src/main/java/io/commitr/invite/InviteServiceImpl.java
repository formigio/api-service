package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
@Service
public class InviteServiceImpl implements InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    @Override
    public Invite saveInvite(Invite invite) {

        inviteRepository.save(invite);

        return invite;
    }

    @Override
    public Invite updateInvite(Invite invite) {
        return null;
    }

    @Override
    public Invite getInvite(UUID uuid) {
        return inviteRepository.findByUuid(uuid);
    }

    @Override
    public List<Invite> getInviteByEntityAndEntityType(UUID entity, String entityType) {
        return inviteRepository.findByEntityAndEntityType(entity, entityType);
    }

    @Override
    public void deleteInvite(UUID uuid) {
        Invite i = inviteRepository.findByUuid(uuid);

        if(null!=i){
            inviteRepository.delete(i);
        }
    }
}
