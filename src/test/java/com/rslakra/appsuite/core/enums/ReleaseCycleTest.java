package com.rslakra.appsuite.core.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 1/7/22 11:02 AM
 */
public class ReleaseCycleTest {

    // LOGGER
    private final Logger LOGGER = LoggerFactory.getLogger(ReleaseCycleTest.class);

    @Test
    public void testReleaseCycle() {
        ReleaseCycle releaseCycle = ReleaseCycle.of(null);
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNull(releaseCycle);

        releaseCycle = ReleaseCycle.of(ReleaseCycle.ALPHA.name());
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNotNull(releaseCycle);
        assertEquals(ReleaseCycle.ALPHA, releaseCycle);

        releaseCycle = ReleaseCycle.of("ALPHA");
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNotNull(releaseCycle);
        assertEquals(ReleaseCycle.ALPHA, releaseCycle);

        releaseCycle = ReleaseCycle.of("Alpha");
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNotNull(releaseCycle);
        assertEquals(ReleaseCycle.ALPHA, releaseCycle);

        releaseCycle = ReleaseCycle.of("alpha");
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNotNull(releaseCycle);
        assertEquals(ReleaseCycle.ALPHA, releaseCycle);

        releaseCycle = ReleaseCycle.of("gamma");
        LOGGER.debug("releaseCycle:{}", releaseCycle);
        assertNull(releaseCycle);
    }
}
