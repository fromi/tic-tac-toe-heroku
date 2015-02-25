package com.github.fromi.tictactoeheroku.security;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.annotation.support.PrincipalMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;

import java.lang.annotation.Annotation;

/**
 * Allows resolving the {@link Authentication#getPrincipal()} using the
 * {@link AuthenticationPrincipal} annotation inside methods annotated with
 * {@link org.springframework.messaging.handler.annotation.MessageMapping}
 *
 * @see org.springframework.security.web.bind.support.AuthenticationPrincipalArgumentResolver
 */
public class AuthenticationPrincipalMessagingArgumentResolver extends PrincipalMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return findMethodAnnotation(AuthenticationPrincipal.class, parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        Object principal = super.resolveArgument(parameter, message);
        if (principal instanceof Authentication) {
            return ((Authentication) principal).getPrincipal();
        } else {
            return principal;
        }
    }

    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, MethodParameter parameter) {
        T annotation = parameter.getParameterAnnotation(annotationClass);
        if(annotation != null) {
            return annotation;
        }
        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
        for(Annotation toSearch : annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
            if(annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
