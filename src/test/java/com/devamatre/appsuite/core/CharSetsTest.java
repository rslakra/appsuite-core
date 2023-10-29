package com.devamatre.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.devamatre.appsuite.core.text.TextUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * @author Rohtash Lakra
 * @created 7/21/22 4:38 PM
 */
public class CharSetsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(CharSetsTest.class);

    /**
     * @return
     */
    private static Stream<Arguments> charSetsData() {
        return Stream.of(
            Arguments.of(null, CharSets.UTF_8),
            Arguments.of(CharSets.ASCII.toCharset(), CharSets.ASCII),
            Arguments.of(CharSets.ISO_8859_1.toCharset(), CharSets.ISO_8859_1),
            Arguments.of(CharSets.UTF_8.toCharset(), CharSets.UTF_8),
            Arguments.of(CharSets.UTF_16.toCharset(), CharSets.UTF_16),
            Arguments.of(TextUtils.NULL, CharSets.UTF_8)
        );
    }

    /**
     * @param charSetName
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("charSetsData")
    public void testCharSets(final String charSetName, final CharSets expected) {
        LOGGER.debug("testCharSets({}, {})", charSetName, expected);
        assertEquals(expected, CharSets.ofString(charSetName));
    }
}
