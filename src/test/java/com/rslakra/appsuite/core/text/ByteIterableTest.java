package com.rslakra.appsuite.core.text;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 20:47:14
 */
public class ByteIterableTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(ByteIterableTest.class);

    @Test
    public void testByteIterable() {
        String value = "Lakra";
        LOGGER.debug(value.toString());
        Iterator<Byte> itr = new ByteIterable(value).iterator();
        assertNotNull(itr);
        TextUtils.logIterator(itr);
    }

}
