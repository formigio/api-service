package io.commitr.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by peter on 11/11/16.
 */
@Configuration
public class WebConfiguration {

    @Bean
    public Filter identityIdFilter() {
        return new IdentityIdFilter();
    }

    class IdentityIdFilter implements Filter {

        private final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            String identityHeader = ((HttpServletRequest)request).getHeader("x-identity-id");

            if(!Objects.isNull(identityHeader)) {
                logger.info(identityHeader);
            }

            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
