package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 12/20/22 3:26 PM
 */
public enum BrandAccessory {
    BEATS,
    ELIZABETH_JAMES,
    FITBIT,
    JBL,
    MOPHIE,
    OTTERBOX,
    ZAGG,
    ;

    /**
     * @param brandAccessory
     * @return
     */
    public static BrandAccessory ofString(final String brandAccessory) {
        return Arrays.stream(values())
                .filter(entry -> entry.name().equalsIgnoreCase(brandAccessory))
                .findFirst()
                .orElse(null);
    }
}
