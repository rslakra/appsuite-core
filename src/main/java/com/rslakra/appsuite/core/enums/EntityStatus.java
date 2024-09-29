package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 8/21/21 6:18 AM
 */
public enum EntityStatus {
    ACTIVE,
    INACTIVE,
    DELETED;

    /**
     * @param entityStatus
     * @return
     */
    public static EntityStatus ofString(final String entityStatus) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(entityStatus))
            .findFirst()
            .orElse(null);
    }
}
