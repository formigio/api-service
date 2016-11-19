package io.commitr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by peter on 11/19/16.
 */
@TestConfiguration
public class RestTemplateConfiguration {
    @Value("${authentication.basic.username}")
    private String username;

    @Value("${authentication.basic.password}")
    private String password;

    @Value("${headers.name}")
    private String name;

    @Value("${headers.value}")
    private String value;

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .additionalCustomizers(
                        new RestTemplateCustomizer() {
                            @Override
                            public void customize(RestTemplate restTemplate) {
                                restTemplate.setInterceptors(Stream.of(
                                        new ContentTypeInterceptor(MediaType.APPLICATION_JSON_UTF8),
                                        new XHeaderInterceptor(name, value),
                                        new BasicAuthorizationInterceptor(username, password))
                                        .collect(Collectors.toList()));
                            }
                        }
                );
    }
}
