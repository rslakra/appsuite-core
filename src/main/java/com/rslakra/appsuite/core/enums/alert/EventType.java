package com.rslakra.appsuite.core.enums.alert;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 9/14/23 11:28 AM
 */
public enum EventType {

    ALERT,
    CHAT,
    EMAIL,
    OTP,
    PUSH_NOTIFICATION,
    SMS,
    ;

    /**
     * @param eventType
     * @return
     */
    public static EventType ofString(final String eventType) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(eventType))
            .findFirst()
            .orElse(null);
    }

}
