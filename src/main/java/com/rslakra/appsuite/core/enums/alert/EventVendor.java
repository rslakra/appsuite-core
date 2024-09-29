package com.rslakra.appsuite.core.enums.alert;

import java.util.Arrays;

/**
 * These are the external SAAS (on cloud/on-prem) vendors, which provide actual notification transmission using their
 * infrastructure and technologies. They maybe paid enterprise services like AWS SNS, MailChimp etc.
 *
 *
 * <pre>
 *
 * - SMS Vendor Integration Service
 * - Email Vendor Integration Service
 * - App Push Notification Vendor Integration Service
 * - WhatsApp Vendor Integration Service
 * - Telegram Vendor Integration Service
 * </pre>
 *
 * @author Rohtash Lakra
 * @created 9/14/23 11:28 AM
 */
public enum EventVendor {

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
    public static EventVendor ofString(final String eventType) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(eventType))
            .findFirst()
            .orElse(null);
    }

}
