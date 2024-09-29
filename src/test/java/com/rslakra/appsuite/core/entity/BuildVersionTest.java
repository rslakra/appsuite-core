package com.rslakra.appsuite.core.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 6/16/22 5:49 PM
 */
public class BuildVersionTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(BuildVersionTest.class);

    @Test
    public void testBuildVersion() {
        BuildVersion buildVersion = new BuildVersion();
        LOGGER.debug("buildVersion:{}", buildVersion);
        assertNotNull(buildVersion);
        buildVersion.loadBuildProperties();
    }
}
