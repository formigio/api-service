package io.commitr.identity;


import io.commitr.configuration.IdentityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by peter on 11/15/16.
 */
public class IdentityConverter implements Converter<String, Identity> {

    IdentityConfiguration configuration;

    public IdentityConverter(IdentityConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Identity convert(String source) {

        Identity convertedIdentity = null;

        if(Pattern.matches(configuration.getIdentity(), source)){
            convertedIdentity = Identity.of(UUID.fromString(source.split(":")[1]), configuration.getProvider());
        }

        return convertedIdentity;
    }
}
