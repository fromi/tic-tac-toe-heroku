package com.github.fromi.tictactoeheroku.security.google;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2FragmentUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Resource
    private AccessTokenRequest accessTokenRequest;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        return "/#" + accessTokenRequest.getStateKey().substring(RedirectingStateKeyGenerator.STATE_TOKEN_LENGTH);
    }
}
