package com.github.fromi.tictactoeheroku.security;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * This configuration makes sure that suffix patterns are not allowed for requests mappings.
 * For example, /game is handled by a Rest Controller, but /game.html is not.
 * Spring Boot makes it hard to set UseSuffixPatternMatch to false (see WebMvcAutoConfiguration).
 */
@Configuration
public class RequestMappingConfiguration {
    @Bean
    public RequestMappingHandlerMappingPostProcessor requestMappingHandlerMappingPostProcessor() {
        return new RequestMappingHandlerMappingPostProcessor();
    }

    public static class RequestMappingHandlerMappingPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            if (bean instanceof RequestMappingHandlerMapping) {
                ((RequestMappingHandlerMapping) bean).setUseSuffixPatternMatch(false);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            return bean;
        }
    }
}
