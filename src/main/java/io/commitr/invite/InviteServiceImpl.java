package io.commitr.invite;

import io.commitr.goal.Goal;
import io.commitr.goal.GoalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
public class InviteServiceImpl implements InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private GoalService goalService;

    @Override
    public Invite saveInvite(Invite invite) {
        Goal g = goalService.getGoal(invite.getGoal());

        if(null==g) {
            return null;
        }

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
    public Invite getInviteByGoal(UUID uuid) {
        return inviteRepository.findByGoal(uuid);
    }

    @Override
    public void deleteInvite(UUID uuid) {
        Invite i = inviteRepository.findByUuid(uuid);

        if(null!=i){
            inviteRepository.delete(i);
        }
    }
}
