package com.rslakra.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * @author Rohtash Lakra
 * @created 3/25/21 6:12 PM
 */
public class RangeTest {

    @Test
    public void testRange() {
        Range range = Range.builder().from(1).to(5).build();
        assertNotNull(range);
        assertEquals(1, range.getFrom());
        assertEquals(5, range.getTo());
        assertEquals(false, range.isReverse());
    }

}
