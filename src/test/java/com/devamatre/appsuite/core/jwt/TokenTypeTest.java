package com.devamatre.appsuite.core.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 5/31/23 1:22 PM
 */
public class TokenTypeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenTypeTest.class);

    @Test
    public void testOfString() {
        LOGGER.debug("+testOfString()");
        TokenType tokenType = TokenType.ofString(null);
        assertNull(tokenType);
        tokenType = TokenType.ofString("ach");
        assertNotNull(tokenType);
        assertEquals(TokenType.ACH, tokenType);
        LOGGER.debug("-testOfString()");
    }
}
