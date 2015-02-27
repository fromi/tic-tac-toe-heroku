package com.github.fromi.tictactoeheroku.security;

import java.security.Principal;

import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.support.MissingSessionUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.security.web.bind.support.AuthenticationPrincipalArgumentResolver;

/**
 * Allows resolving the {@link Authentication#getPrincipal()} using the
 * {@link AuthenticationPrincipal} annotation inside methods annotated with
 * {@link org.springframework.messaging.handler.annotation.MessageMapping}
 *
 * @see org.springframework.security.web.bind.support.AuthenticationPrincipalArgumentResolver
 * @see org.springframework.messaging.simp.annotation.support.PrincipalMethodArgumentResolver
 */
// TODO: Spring Security 4 handles that, upgrade as soon as possible
public class AuthenticationPrincipalMessagingArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationPrincipalArgumentResolver delegate = new AuthenticationPrincipalArgumentResolver();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return delegate.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        Principal principal = SimpMessageHeaderAccessor.getUser(message.getHeaders());
        if (principal == null) {
            throw new MissingSessionUserException(message);
        }
        return principal instanceof Authentication ? ((Authentication) principal).getPrincipal() : principal;
    }
}
