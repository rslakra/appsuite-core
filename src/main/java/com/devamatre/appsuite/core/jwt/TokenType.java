package com.devamatre.appsuite.core.jwt;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 4/15/20 12:21 PM
 */
public enum TokenType {
    CREDIT_CARD,
    ACH,
    DEBIT_CARD,
    POSTPAID;

    /**
     * @param tokenType
     * @return
     */
    public static TokenType ofString(final String tokenType) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(tokenType))
            .findAny()
            .orElse(null);
    }
}
