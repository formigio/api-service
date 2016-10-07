package io.commitr.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Peter Douglas on 10/6/2016.
 */
@Configuration
@EnableSwagger2
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
