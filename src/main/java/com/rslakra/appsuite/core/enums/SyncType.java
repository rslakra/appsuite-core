package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 12/20/22 3:26 PM
 */
public enum SyncType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    /**
     * @param syncType
     * @return
     */
    public static SyncType ofString(final String syncType) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(syncType))
            .findFirst()
            .orElse(null);
    }
}
