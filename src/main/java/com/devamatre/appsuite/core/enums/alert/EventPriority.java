package com.rslakra.appsuite.core.enums.alert;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 9/14/23 11:25 AM
 */
public enum EventPriority {

    HIGH,
    IMPORTANT,
    LOW,
    MEDIUM,
    URGENT,
    ;

    /**
     * @param eventPriority
     * @return
     */
    public static EventPriority ofString(final String eventPriority) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(eventPriority))
            .findFirst()
            .orElse(null);
    }
}
