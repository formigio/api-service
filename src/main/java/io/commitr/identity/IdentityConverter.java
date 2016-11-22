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

    private final Pattern identityPattern = Pattern.compile("^\\w+-\\w+-\\d+:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    @Override
    public Identity convert(String source) {

        Identity convertedIdentity = null;

        if(identityPattern.matcher(source).matches()){
            convertedIdentity = Identity.of(UUID.fromString(source.split(":")[1]), "cognito");
        }

        return convertedIdentity;
    }
}
