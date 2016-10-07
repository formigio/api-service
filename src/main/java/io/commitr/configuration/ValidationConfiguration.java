package io.commitr.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peter Douglas on 10/5/2016.
 * From lds.org with additions to disable Entity validations.
 */
@Configuration
public class ValidationConfiguration {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(final Validator validator) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator);

        return processor;
    }

    @Bean
    public ValidationConfigurationBeanPostProcessor validationConfigurationBeanPostProcessor(final Validator validator) {
        return new ValidationConfigurationBeanPostProcessor(validator);
    }

    private class ValidationConfigurationBeanPostProcessor implements BeanPostProcessor{

        private final Validator validator;

        private ValidationConfigurationBeanPostProcessor(Validator validator) {
            this.validator = validator;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof LocalContainerEntityManagerFactoryBean) {

                Map<String,Object> props = new HashMap<>();
                props.put("javax.persistence.validation.factory", validator);
                props.put("javax.persistence.validation.mode", "none");

                LocalContainerEntityManagerFactoryBean.class.cast(bean)
                        .getJpaPropertyMap()
                        .putAll(props);
            }

            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }
}
