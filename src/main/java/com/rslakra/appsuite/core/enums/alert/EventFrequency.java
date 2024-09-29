package com.rslakra.appsuite.core.enums.alert;

import java.util.Arrays;

/**
 * Maintain Alphabetically Ordered.
 *
 * @author Rohtash Lakra
 * @created 9/14/23 11:18 AM
 */
public enum EventFrequency {

    CUSTOM,
    DAILY,
    HOURLY,
    MINUTE,
    MONTHLY,
    SECOND,
    WEEKLY,
    YEARLY,
    ;

    /**
     * @param eventFrequency
     * @return
     */
    public static EventFrequency ofString(final String eventFrequency) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(eventFrequency))
            .findFirst()
            .orElse(null);
    }

}
