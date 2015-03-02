package com.github.fromi.tictactoeheroku.security.mapping;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class RequestMappingHandlerMappingPostProcessor implements BeanPostProcessor {

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
