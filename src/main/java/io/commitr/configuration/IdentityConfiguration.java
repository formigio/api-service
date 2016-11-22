package io.commitr.configuration;

import io.commitr.validator.IdentityValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * Created by peter on 11/12/16.
 */

@Configuration
@Profile("local")
public class IdentityConfiguration {

    @Value("${authentication.provider}")
    @Getter
    private String provider;

    @Value("${authentication.identity}")
    @Getter
    private String identity;

}
