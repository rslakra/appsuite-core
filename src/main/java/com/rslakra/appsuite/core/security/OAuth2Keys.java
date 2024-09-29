package com.rslakra.appsuite.core.security;

import java.io.File;
import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 5/24/23 6:16 PM
 */
public enum OAuth2Keys {

    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type"),
    GRANT_TYPE_VALUE("client_credentials"),
    TOKEN_PATH_COMPONENT("/oauth/token"),
    REFRESH_PATH_COMPONENT("/oauth/refresh"),
    ;

    private final String keyName;

    OAuth2Keys(String keyName) {
        this.keyName = keyName;
    }

    /**
     * Returns the <code>keyName</code>.
     *
     * @return
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * @param keyName
     * @return
     */
    public static OAuth2Keys ofString(String keyName) {
        return Arrays.stream(values())
            .filter(entry -> entry.getKeyName().equalsIgnoreCase(keyName))
            .findFirst()
            .orElse(null);
    }

    /**
     * @param baseUrl
     * @return
     */
    public String buildUrlPrefixWith(String baseUrl) {
        String pathComponent = getKeyName().startsWith(File.separator) ? getKeyName().substring(1) : getKeyName();
        return String.join(File.separator, baseUrl, pathComponent);
    }


}
