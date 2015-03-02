package com.github.fromi.tictactoeheroku.security.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
