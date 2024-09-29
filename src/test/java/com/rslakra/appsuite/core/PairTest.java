package com.rslakra.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * @author Rohtash Lakra
 * @created 4/7/21 5:27 PM
 */
public class PairTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(PairTest.class);


    private static Stream<Arguments> pairData() {
        final Object[] floats = new Object[]{10.3f, 53.7f};
        return Stream.of(
            Arguments.of(Pair.NULL, true),
            Arguments.of(Pair.of("name", "name"), true),
            Arguments.of(Pair.of(1, 1), true),
            Arguments.of(Pair.of(false, false), true),
            Arguments.of(Pair.of(true, true), true),
            Arguments.of(Pair.of(Pair.EMPTY_ARRAY, Pair.EMPTY_ARRAY), true),
            Arguments.of(Pair.of(floats, floats), true),
            Arguments.of(Pair.of("firstName", "Rohtash"), false),
            Arguments.of(Pair.of("index", 10), false),
            Arguments.of(Pair.of("exists", false), false)
        );
    }

    @ParameterizedTest
    @MethodSource("pairData")
    public void testPair(Pair pair, Boolean isSame) {
        LOGGER.debug("testPair({}, {})", pair, isSame);
        if (isSame) {
            assertEquals(pair.getKey(), pair.getValue());
            assertEquals(pair.getLeft(), pair.getRight());
        } else {
            assertNotEquals(pair.getKey(), pair.getValue());
            assertNotEquals(pair.getLeft(), pair.getRight());
        }
    }

    @Test
    public void testCompareTo() {
        Pair leftPair = Pair.of("firstName", "lastName");
        LOGGER.debug("leftPair:{}", leftPair);
        assertEquals(0, leftPair.compareTo(leftPair));

        Pair nextPair = Pair.of("firstName", "name");
        LOGGER.debug("leftPair:{}, nextPair:{}", leftPair, nextPair);
        assertTrue(leftPair.compareTo(nextPair) < 0);

        Pair rightPair = Pair.of("eachName", "lastName");
        LOGGER.debug("leftPair:{}, rightPair:{}", leftPair, rightPair);
        assertTrue(leftPair.compareTo(rightPair) > 0);
    }
}
