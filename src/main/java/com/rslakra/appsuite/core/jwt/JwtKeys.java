package com.rslakra.appsuite.core.jwt;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 4/25/23 11:11 AM
 */
@Getter
public enum JwtKeys {
    // Issuer
    ISSUER("iss"),
    // Subject
    SUBJECT("sub"),
    // Audience
    AUDIENCE("aud"),
    // Expiration
    EXPIRATION("exp"),
    // Not Before
    NOT_BEFORE("nbf"),
    // Issued At
    ISSUED_AT("iat"),
    // JWT ID
    JWT_ID("jti"),
    ;

    private final String jwtKey;

    /**
     * @param jwtKey
     */
    JwtKeys(final String jwtKey) {
        this.jwtKey = jwtKey;
    }

    /**
     * @param jwtKey
     * @return
     */
    public static JwtKeys ofString(final String jwtKey) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(jwtKey))
            .findFirst()
            .orElse(null);
    }


}
