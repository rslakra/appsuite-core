package com.rslakra.appsuite.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 5/24/23 7:07 PM
 */
public class OAuth2KeysTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(OAuth2KeysTest.class);

    @Test
    public void testBuildUrlPrefixWith() {
        String apiBaseUrl = "https://www.rslakra.com";
        String tokenPathUrl = OAuth2Keys.TOKEN_PATH_COMPONENT.buildUrlPrefixWith(apiBaseUrl);
        assertEquals(tokenPathUrl, apiBaseUrl + OAuth2Keys.TOKEN_PATH_COMPONENT.getKeyName());

        tokenPathUrl = OAuth2Keys.REFRESH_PATH_COMPONENT.buildUrlPrefixWith(apiBaseUrl);
        assertEquals(tokenPathUrl, apiBaseUrl + OAuth2Keys.REFRESH_PATH_COMPONENT.getKeyName());
    }
}
