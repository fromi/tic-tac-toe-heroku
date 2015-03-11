package com.github.fromi.tictactoeheroku.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.github.fromi.tictactoeheroku.security.google.OpenIDConnectAuthenticationFilter;

@Configuration
@EnableWebMvcSecurity
public class URLPathMapping extends WebSecurityConfigurerAdapter {

    private static final String HOME = "/";
    public static final String USER = "/user";
    public static final String GAME = "/game";
    public static final String GAMES = "/games";
    public static final String WEB_SOCKET = "/web-socket";

    private static final String HTML_FILES = "/**/*.html";
    private static final String JAVASCRIPT_FILES = "/**/*.js";
    private static final String CSS_FILES = "/**/*.css";
    private static final String MAP_FILES = "/**/*.map";
    private static final String WEB_SOCKET_ALL_SUB_PATHS = WEB_SOCKET + "/**";
    private static final String GAME_SUB_PATHS = GAME + "/*";

    private static final String XSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationFilter() {
        return new OpenIDConnectAuthenticationFilter();
    }

    @Bean
    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
        return new OAuth2ClientContextFilter();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(XSRF_TOKEN_HEADER);
        return repository;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HTML_FILES, JAVASCRIPT_FILES, CSS_FILES, MAP_FILES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(authenticationFilter(), OAuth2ClientContextFilter.class)
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and().authorizeRequests()
                .antMatchers(GET, HOME, USER, GAMES, GAME_SUB_PATHS, WEB_SOCKET_ALL_SUB_PATHS).permitAll()
                .antMatchers(POST, GAME).authenticated()
                .anyRequest().denyAll();
    }

}
