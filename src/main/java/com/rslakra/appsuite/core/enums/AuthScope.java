package com.rslakra.appsuite.core.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 12/19/22 11:06 AM
 */
@Getter
public enum AuthScope {

    NONE("provider", Arrays.asList("scope")),
    GOOGLE("provider", Arrays.asList("profile", "email", "openid")),
    FACEBOOK("provider", Arrays.asList("scope")),
    MICROSOFT("provider", Arrays.asList("scope")),
    ;

    private String provider;
    private List<String> scopes;

    /**
     * @param provider
     * @param scopes
     */
    AuthScope(final String provider, final List<String> scopes) {
        this.provider = provider;
        this.scopes = scopes;
    }

    /**
     * @param provider
     * @param scope
     * @return
     */
    public static AuthScope ofAuthScope(final String provider, final String scope) {
        return Arrays.stream(values())
            .filter(authScope -> authScope.getProvider().equals(provider))
            .filter(authScope -> authScope.getScopes().contains(scope))
            .findFirst()
            .orElse(NONE);
    }
}
