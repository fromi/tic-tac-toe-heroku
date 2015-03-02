package com.github.fromi.tictactoeheroku.security.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

import com.github.fromi.tictactoeheroku.security.URLPathMapping;
import com.google.common.base.Joiner;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketDestinationsMapping extends AbstractWebSocketMessageBrokerConfigurer {

    public static final String GAMES = "/games";
    public static final String GAME = "/game";
    public static final Joiner SLASH_JOINER = Joiner.on('/');

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(URLPathMapping.WEB_SOCKET).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(GAMES);
        registry.enableSimpleBroker(GAME);
        registry.setApplicationDestinationPrefixes("/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalMessagingArgumentResolver());
    }
}
