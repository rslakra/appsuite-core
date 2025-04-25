package com.rslakra.appsuite.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rohtash Lakra
 * @created 4/7/21 5:27 PM
 */
public class NumberUtilsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(NumberUtilsTest.class);

    /**
     * The data provider for the <code>isBoolean</code> method.
     *
     * @return
     */
    private static Stream<Arguments> isBooleanData() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of("true", true),
                Arguments.of("false", true)
        );
    }

    /**
     * Test method for {@link NumberUtils#isBoolean(String)}.
     *
     * @param str
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("isBooleanData")
    public void testIsBoolean(String str, boolean expected) {
        LOGGER.debug("testIsBoolean({}, {})", str, expected);
        assertEquals(NumberUtils.isBoolean(str), expected);
    }

    /**
     * The data provider for the <code>isNumber</code> method.
     *
     * @return
     */
    private static Stream<Arguments> isNumberData() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of("10", true),
                Arguments.of("20.4", true)
        );
    }

    /**
     * Test method for {@link NumberUtils#isNumber(String)}.
     *
     * @param str
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("isNumberData")
    public void testIsNumber(String str, boolean expected) {
        LOGGER.debug("testIsNumber({}, {})", str, expected);
        assertEquals(NumberUtils.isNumber(str), expected);
    }

}
