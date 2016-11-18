package io.commitr.identity;

import java.util.UUID;

/**
 * Created by peter on 11/15/16.
 */
public interface IdentityService {
    public Identity getIdentity(UUID uuid);
    public Identity saveIdenity(Identity identity);
}
