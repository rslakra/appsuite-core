package com.rslakra.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * @author Rohtash Lakra
 * @created 8/25/21 5:47 PM
 */
public class UnsafeUtilsTest {

    @Test
    public void testIsUnsafeSupported() {
        assertFalse(UnsafeUtils.isUnsafeSupported());
    }
}
