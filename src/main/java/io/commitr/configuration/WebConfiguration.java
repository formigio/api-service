package io.commitr.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

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

        @Autowired
        private MessageSource messageSource;

        @Autowired
        private IdentityConfiguration identityConfiguration;

        private final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

        private Pattern identityPattern;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            if(Objects.isNull(identityPattern)){
                identityPattern = Pattern.compile(identityConfiguration.getIdentity());
            }
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
                throw new ServletException("Identity Filter just supports HTTP requests");
            }

            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse res = (HttpServletResponse)response;


            String identityHeader = req.getHeader("x-identity-id");

            if(!Objects.isNull(identityHeader) && identityPattern.matcher(identityHeader).matches()) {
                logger.info(identityHeader);
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, messageSource.getMessage("missing.header",null,req.getLocale()));
            }
        }

        @Override
        public void destroy() {

        }
    }
}
