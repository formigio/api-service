package io.commitr.identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

/**
 * Created by peterfdouglas on 10/19/2016.
 */

@Service
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    IdentityRepository repository;

    @Override
    public Identity getIdentity() {

        Identity identity = Identity.of(null, null, null);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .reduce(" ", String::concat).contains("ROLE_ANONYMOUS")) {

            identity = repository.findByPrincipleId(principalAsString());

            if (Objects.isNull(identity)) {
                identity = Identity.of(null, principalAsString(), null);

                repository.save(identity);
            }
        }

        return identity;
    }

    private String principalAsString() {
        String principal = null;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            principal = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }

        return principal;
    }
}
