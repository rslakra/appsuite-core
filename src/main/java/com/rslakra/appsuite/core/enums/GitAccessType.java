package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 10/14/22 12:26 PM
 */
public enum GitAccessType {
    ADMIN,
    COLLABORATOR,
    GUEST;

    /**
     * @param gitAccessType
     * @return
     */
    public static GitAccessType ofString(final String gitAccessType) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(gitAccessType))
            .findFirst()
            .orElse(null);
    }
}
