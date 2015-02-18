package com.github.fromi.tictactoeheroku.security;

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

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebMvcSecurity
public class PathConfiguration extends WebSecurityConfigurerAdapter {

    private static final String HOME = "/";
    private static final String JAVASCRIPT_FILES = "/**/*.js";
    private static final String JAVASCRIPT_MAP_FILES = "/**/*.js.map";
    private static final String HTML_FILES = "/**/*.html";
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HOME, JAVASCRIPT_FILES, JAVASCRIPT_MAP_FILES, HTML_FILES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(authenticationFilter(), OAuth2ClientContextFilter.class)
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository())
                .and().authorizeRequests()
                .antMatchers(GET, "/user", "/ws/**").permitAll()
                .antMatchers(POST, "/game").authenticated()
                .anyRequest().denyAll();
    }

}
