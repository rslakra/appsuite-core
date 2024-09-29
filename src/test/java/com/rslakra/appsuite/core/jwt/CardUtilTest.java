package com.rslakra.appsuite.core.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Key;
import java.security.PublicKey;

/**
 * @author Rohtash Lakra
 * @created 4/15/20 12:40 PM
 */
public class CardUtilTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(CardUtilTest.class);

    //    @Test
    public void testCardTokenGeneration() {
        LOGGER.debug("+testCardTokenGeneration()");
        final String PAYMENT_PUBLIC_KEY_URL = "https://payment-service.devamatre.com/.well-known/jwks.json";
        final String cardNumber = "4111111111111111";
        final String cvc = "123";
        final String idAddress = "10.0.0.127";
        String cardEncryptedToken = null;
        try {
            Key publicKey = JWTUtils.INSTANCE.fetchPublicKey(PAYMENT_PUBLIC_KEY_URL, 5 * 60);
            CardUtils.INSTANCE.setPublicKey((PublicKey) publicKey);
            cardEncryptedToken =
                CardUtils.INSTANCE.newEncryptedToken(TokenType.CREDIT_CARD, cardNumber, cvc, idAddress);
            LOGGER.debug("cardEncryptedToken: {}}", cardEncryptedToken);
            assertNotNull(cardEncryptedToken);
            LOGGER.debug(cardEncryptedToken);
        } catch (JoseException | IOException ex) {
            ex.printStackTrace();
            assertNull(cardEncryptedToken);
        }
        LOGGER.debug("-testCardTokenGeneration()");
    }
}
