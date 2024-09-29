package com.rslakra.appsuite.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 1/7/22 10:50 AM
 */
@Getter
@AllArgsConstructor
public enum ReleaseCycle {

    PRE_ALPHA("Development Build Nightly Release"),
    ALPHA("Alpha Release"),
    BETA("Beta Release"),
    RC("Release Candidate aka Gamma Delta"),
    RTM("Release to manufacturing aka Release to marketing"),
    GA("General Availability"),
    PRODUCTION("Live Release aka Gold");

    private final String description;

    /**
     * @param releaseCycle
     * @return
     */
    public static ReleaseCycle of(final String releaseCycle) {
        return Arrays.stream(ReleaseCycle.values())
            .filter(e -> e.name().equalsIgnoreCase(releaseCycle))
            .findAny()
            .orElse(null);
    }
}
