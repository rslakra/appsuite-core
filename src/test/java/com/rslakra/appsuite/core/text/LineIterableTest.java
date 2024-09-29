package com.rslakra.appsuite.core.text;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @created 7/15/22 10:57 AM
 */
public class LineIterableTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(LineIterableTest.class);

    @Test
    public void testLineIterable() {
        String value = "Lakra";
        LOGGER.debug(value.toString());
        Iterator<Character> itr = new LineIterable(value).iterator();
        assertNotNull(itr);
        TextUtils.logIterator(itr);
    }

}
