package com.github.fromi.tictactoeheroku.security.google;

import static com.google.common.base.Strings.nullToEmpty;

import javax.annotation.Resource;

import org.springframework.security.oauth2.client.filter.state.StateKeyGenerator;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

/**
 * An OAuth2 StateKeyGenerator that includes in the state sent to third party a redirection path to be use on callback
 */
public class RedirectingStateKeyGenerator extends RandomValueStringGenerator implements StateKeyGenerator {
    public static final String REDIRECT = "redirect";
    public static final int STATE_TOKEN_LENGTH = 30;

    public RedirectingStateKeyGenerator() {
        super(STATE_TOKEN_LENGTH);
    }

    @Resource
    private AccessTokenRequest accessTokenRequest;

    @Override
    public String generateKey(OAuth2ProtectedResourceDetails resource) {
        return generate() + nullToEmpty(accessTokenRequest.getFirst(REDIRECT));
    }
}
