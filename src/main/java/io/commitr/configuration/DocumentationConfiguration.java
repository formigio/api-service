package io.commitr.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Peter Douglas on 10/6/2016.
 */
@Configuration
public class DocumentationConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.commitr"))
                .paths(paths())
                .build();
    }

    private Predicate<String> paths() {
        return or(
                regex("/goal/*"),
                regex("/invite/*"),
                regex("/task/*"),
                regex("/team/*"));
    }
}
