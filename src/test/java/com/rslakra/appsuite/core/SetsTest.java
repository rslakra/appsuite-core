package com.rslakra.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.Spliterator;

/**
 * @author Rohtash Lakra
 * @created 3/19/20 1:55 PM
 */
public class SetsTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(SetsTest.class);

    @Test
    public void testCreateSet() {
        //unique values
        Set<String> cardTypes = Sets.asSet("VISA", "MSTR", "AMEX", "DISC");
        assertEquals(4, cardTypes.size());
        cardTypes.forEach(item -> LOGGER.debug(item));
        LOGGER.debug("\n");
        printSpliterator(cardTypes);

        //duplicate values
        cardTypes = Sets.asSet("VISA", "MSTR", "MSTR", "AMEX", "DISC");
        assertEquals(4, cardTypes.size());
        cardTypes.forEach(item -> LOGGER.debug(item));
        LOGGER.debug("\n");
        printSpliterator(cardTypes);
    }

    /**
     * @param types
     */
    private void printSpliterator(final Set<String> types) {
        BeanUtils.assertNonNull(types);
        Spliterator<String> spliterator = types.spliterator();
        LOGGER.debug("characteristics: {}, estimateSize: {}", spliterator.characteristics(),
                     spliterator.estimateSize());
    }

    @Test
    public void testAsSet() {
        Set<String> nameSet = Sets.asSet("Rohtash", "Singh", "Lakra");
        BeanUtils.assertNonNull(nameSet);
        assertEquals(3, nameSet.size());
        assertEquals("Rohtash", nameSet.iterator().next());
    }
}
