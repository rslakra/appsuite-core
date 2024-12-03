package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 12/20/22 3:26 PM
 */
public enum BrandDevice {
    AMAZON,
    APPLE,
    GOOGLE,
    MOTOROLA,
    SAMSUNG,
    ;

    /**
     * @param brandDevice
     * @return
     */
    public static BrandDevice ofString(final String brandDevice) {
        return Arrays.stream(values())
                .filter(entry -> entry.name().equalsIgnoreCase(brandDevice))
                .findFirst()
                .orElse(null);
    }
}
