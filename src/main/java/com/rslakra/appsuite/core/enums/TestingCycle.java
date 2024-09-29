package com.rslakra.appsuite.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 1/7/22 11:14 AM
 */
@Getter
@AllArgsConstructor
public enum TestingCycle {

    WHITEBOX(
        "White-Box testing is considered as low-level testing. It is also called glass box, transparent box, clear box or code base testing."),
    BLACKBOX(
        "Black box testing is a high level of testing that focuses on the behavior of the software. It involves testing from an external or end-user perspective.");

    private final String description;

    /**
     * @param testingCycle
     * @return
     */
    public static TestingCycle ofString(final String testingCycle) {
        return Arrays.stream(TestingCycle.values())
            .filter(entry -> entry.name().equalsIgnoreCase(testingCycle))
            .findAny()
            .orElse(null);
    }

}
