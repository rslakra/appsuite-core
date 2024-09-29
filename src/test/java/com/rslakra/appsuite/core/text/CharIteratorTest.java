package com.rslakra.appsuite.core.text;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 20:51:48
 */
public class CharIteratorTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(CharIteratorTest.class);

    @Test
    public void testCharIterator() {
        String value = "Lakra";
        LOGGER.debug(value.toString());
        Iterator<Character> itr = new CharIterator(value);
        assertNotNull(itr);
        TextUtils.logIterator(itr);
    }
}
