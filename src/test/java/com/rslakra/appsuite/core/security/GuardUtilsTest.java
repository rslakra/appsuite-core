package com.rslakra.appsuite.core.security;

import com.rslakra.appsuite.core.security.pbkdf2.PBKDF2Generator;
import com.rslakra.appsuite.core.security.pbkdf2.PBKDF2Params;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since 11/23/2023 2:23 PM
 */
public class GuardUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuardUtilsTest.class);

    @Test
    public void testGuardUtils() throws NoSuchAlgorithmException, InvalidKeySpecException {
        LOGGER.debug("+testGuardUtils()");
        String password = "Password";
        LOGGER.debug("password:{}", password);
        byte[] salt = GuardUtils.getSalt(PBKDF2Generator.SHA1PRNG);
        LOGGER.debug("salt:{}", Arrays.toString(salt));
        PBKDF2Params pbkdf2Params = new PBKDF2Params(salt);
        LOGGER.debug("pbkdf2Params:{}", pbkdf2Params);
        PBKDF2Generator pbkdf2Generator = new PBKDF2Generator(pbkdf2Params);
        LOGGER.debug("pbkdf2Generator:{}", pbkdf2Generator);
        String keyAsHexString = pbkdf2Generator.keyAsHexString(password);
        LOGGER.debug("keyAsHexString:{}", keyAsHexString);
        String newKeyAsHexString = pbkdf2Generator.keyAsHexString(password);
        LOGGER.debug("newKeyAsHexString:{}", newKeyAsHexString);
        String hashedPassword = pbkdf2Generator.hashPassword(password);
        LOGGER.debug("hashedPassword:{}", hashedPassword);
        boolean matched = pbkdf2Generator.validatePassword(password, hashedPassword);
        LOGGER.debug("matched:{}, password:{}, hashedPassword:{}", matched, password, hashedPassword);
        matched = pbkdf2Generator.validatePassword(password, hashedPassword);
        LOGGER.debug("2nd time matched:{}, password:{}, hashedPassword:{}", matched, password, hashedPassword);
        LOGGER.debug("-testGuardUtils()");
    }
}
