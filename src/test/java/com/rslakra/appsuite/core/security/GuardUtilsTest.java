package com.rslakra.appsuite.core.security;

import com.rslakra.appsuite.core.security.pbkdf2.PBKDF2Generator;
import com.rslakra.appsuite.core.security.pbkdf2.PBKDF2Params;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since 11/23/2023 2:23 PM
 */
public class GuardUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuardUtilsTest.class);

    @Test
    public void testRsaAlgorithmConstants() {
        assertEquals("RSA/None/NoPadding", GuardUtils.ALGO_RSA_NONE_NO_PADDING);
        assertEquals("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", GuardUtils.ALGO_RSA_ECB_OAEP_SHA256_MGF1_PADDING);
    }

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

    @Test
    public void testEncryptWithPublicKeyPrivateMethodUsesOaep() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(GuardUtils.ALGO_RSA);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Method encryptMethod = GuardUtils.class.getDeclaredMethod("encryptWithPublicKey", String.class, PublicKey.class);
        encryptMethod.setAccessible(true);
        String encrypted = (String) encryptMethod.invoke(null, "payload", publicKey);

        assertNotNull(encrypted);
        assertNotEquals("payload", encrypted);
    }

    @Test
    public void testDecryptWithPublicKeyPrivateMethodReturnsNull() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(GuardUtils.ALGO_RSA);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Method encryptMethod = GuardUtils.class.getDeclaredMethod("encryptWithPublicKey", String.class, PublicKey.class);
        encryptMethod.setAccessible(true);
        String encrypted = (String) encryptMethod.invoke(null, "payload", publicKey);

        Method decryptMethod = GuardUtils.class.getDeclaredMethod("decryptWithPublicKey", String.class, PublicKey.class);
        decryptMethod.setAccessible(true);
        String decrypted = (String) decryptMethod.invoke(null, encrypted, publicKey);

        assertNull(decrypted);
    }

    @Test
    public void testEncryptWithPublicKeyEncodedCertificateInvalidInput() {
        String encrypted = GuardUtils.encryptWithPublicKey("payload", "invalid-cert");
        assertNull(encrypted);
    }

    @Test
    public void testDecryptWithPublicKeyEncodedCertificateInvalidInput() {
        String decrypted = GuardUtils.decryptWithPublicKey("payload", "invalid-cert");
        assertNull(decrypted);
    }

    @Test
    public void testSaltedPasswordUsesGeneratedSalt() {
        String saltedPassword = GuardUtils.saltedPassword("password");
        assertNotNull(saltedPassword);
        assertTrue(saltedPassword.endsWith("|password"));
        String salt = saltedPassword.substring(0, saltedPassword.indexOf('|'));
        byte[] decodedSalt = Base64.getDecoder().decode(salt);
        assertEquals(16, decodedSalt.length);
    }

    @Test
    public void testSymmetricEncryptDecryptWithoutIvRoundTrip() throws Exception {
        byte[] plainBytes = "secret-payload".getBytes();
        byte[] keyBytes = GuardUtils.getSHA256Hash("local-test-key");
        byte[] encryptedBytes = GuardUtils.encryptWithSymmetricKey(plainBytes, keyBytes, null);
        assertNotNull(encryptedBytes);
        assertTrue(encryptedBytes.length > GuardUtils.GCM_IV_SIZE);

        byte[] decryptedBytes = GuardUtils.decryptWithSymmetricKey(encryptedBytes, keyBytes, null);
        assertArrayEquals(plainBytes, decryptedBytes);
    }

    @Test
    public void testSymmetricEncryptDecryptWithIvRoundTrip() throws Exception {
        byte[] plainBytes = "secret-with-iv".getBytes();
        byte[] keyBytes = GuardUtils.getSHA256Hash("local-test-key");
        byte[] ivBytes = GuardUtils.getIVBytes("payload.dat");
        byte[] encryptedBytes = GuardUtils.encryptWithSymmetricKey(plainBytes, keyBytes, ivBytes);
        assertNotNull(encryptedBytes);

        byte[] decryptedBytes = GuardUtils.decryptWithSymmetricKey(encryptedBytes, keyBytes, ivBytes);
        assertArrayEquals(plainBytes, decryptedBytes);
    }
}
