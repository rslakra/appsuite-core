package com.rslakra.appsuite.core.text;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 20:49:42
 */
public class ByteIteratorTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(ByteIteratorTest.class);

    @Test
    public void testByteIterator() {
        String value = "Lakra";
        LOGGER.debug(value.toString());
        Iterator<Byte> itr = new ByteIterator(value);
        assertNotNull(itr);
        TextUtils.logIterator(itr);
    }
}
