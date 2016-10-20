package io.commitr.identity;

import lombok.Data;
import org.apache.catalina.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Created by peterfdouglas on 10/19/2016.
 */
@Entity
@Data
public class Identity implements UserDetails{

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String providerId;

    @NotNull
    @JsonIgnore
    private String principleId;

    @NotNull
    private UUID uuid;

    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    void prePersist() {
        if (null==this.uuid) {
            this.uuid = UUID.randomUUID();
        }
    }


    public static Identity of(String providerId, String principleId, UUID uuid) {
        Identity i = new Identity();
        i.setProviderId(providerId);
        i.setPrincipleId(principleId);
        i.setUuid(uuid);
        return i;
    }
}
