package io.commitr.invite;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.UUID;

/**
 * Created by Peter Douglas on 9/28/2016.
 */
public interface InviteService {
    public Invite saveInvite(Invite invite);
    public Invite updateInvite(Invite invite);
    public Invite getInvite(UUID uuid);
    public Invite getInviteByGoal(UUID uuid);
    public void deleteInvite(UUID uuid);
}
