package com.github.fromi.tictactoeheroku.security.google;

import static java.util.Collections.singletonList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.state.StateKeyGenerator;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.redirect.AbstractRedirectResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfigurer {
    private static final String PROFILE_SCOPE = "profile";
    static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Value("${google.oauth2.discovery-document-url}")
    private String discoveryDocumentURL;

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    @Value("${google.oauth2.redirect-uri}")
    private String redirectURI;

    @Bean
    public OAuth2ProtectedResourceDetails googleOAuth2Details() {
        AuthorizationCodeResourceDetails googleOAuth2Details = discoverGoogleOpenIdConfiguration();
        googleOAuth2Details.setAuthenticationScheme(form);
        googleOAuth2Details.setClientAuthenticationScheme(form);
        googleOAuth2Details.setClientId(clientId);
        googleOAuth2Details.setClientSecret(clientSecret);
        googleOAuth2Details.setScope(singletonList(PROFILE_SCOPE));
        googleOAuth2Details.setUseCurrentUri(false);
        googleOAuth2Details.setPreEstablishedRedirectUri(redirectURI);
        return googleOAuth2Details;
    }

    private AuthorizationCodeResourceDetails discoverGoogleOpenIdConfiguration() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().mixIn(AbstractRedirectResourceDetails.class, DetailsMixIn.class).build();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
        RestTemplate restTemplate = new RestTemplate(singletonList(converter));
        return restTemplate.getForEntity(discoveryDocumentURL, AuthorizationCodeResourceDetails.class).getBody();
    }

    @Resource
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    public StateKeyGenerator stateKeyGenerator() {
        return new RedirectingStateKeyGenerator();
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestOperations googleOAuth2RestTemplate() {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(googleOAuth2Details(), oAuth2ClientContext);
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
        accessTokenProvider.setStateKeyGenerator(stateKeyGenerator());
        oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        return oAuth2RestTemplate;
    }

    private interface DetailsMixIn {
        @JsonProperty("authorization_endpoint")
        void setUserAuthorizationUri(String userAuthorizationUri);

        @JsonProperty("token_endpoint")
        void setAccessTokenUri(String accessTokenUri);
    }
}
