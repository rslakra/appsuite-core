package com.rslakra.appsuite.core.security.pbkdf2;

import com.rslakra.appsuite.core.BeanUtils;
import com.rslakra.appsuite.core.IOUtils;
import com.rslakra.appsuite.core.security.GuardUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * This class implements the <code>PBKDF2</code> in pure java.
 * <p>
 * https://www.ietf.org/rfc/rfc2898.txt
 *
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since 11/21/2016 04:04:10 PM
 */
public class PBKDF2Generator {

    /* The secret keys algorithm */
    public static final String PBKDF2_WITH_HMAC_SHA512 = "PBKDF2WithHmacSHA512";
    public static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";

    /* Secure Random Algorithm */
    public static final String SHA1PRNG = "SHA1PRNG";

    /* Iterations - Strong */
    public static final int ITERATIONS = 10000;

    /* Key Length */
    public static final int KEY_LENGTH = 16;

    /* PBKDF2 parameters */
    private PBKDF2Params parameters;

    /**
     * @param parameters
     */
    public PBKDF2Generator(PBKDF2Params parameters) {
        this.parameters = parameters;
    }

    /**
     * Generates the <code>PBKDF2</code> secret key.
     *
     * @param password
     * @param salt
     * @param iterations
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     */
    private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) throws NoSuchAlgorithmException {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(parameters.getAlgorithm());
            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }

    /**
     * Returns the <code>PBKDF2</code> bytes of the given password.
     *
     * @param password
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] deriveKey(String password, int keyLength) throws NoSuchAlgorithmException {
        return pbkdf2(password.toCharArray(), parameters.getSalt(), parameters.getIterations(), keyLength);
    }

    /**
     * Returns the <code>PBKDF2</code> hex string of the given password.
     *
     * @param password
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String keyAsHexString(String password, int keyLength) throws NoSuchAlgorithmException {
        return IOUtils.toHexString(deriveKey(password, keyLength));
    }

    /**
     * Returns the <code>PBKDF2</code> hex string of the given password.
     *
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String keyAsHexString(String password) throws NoSuchAlgorithmException {
        return IOUtils.toHexString(deriveKey(password, PBKDF2Generator.KEY_LENGTH));
    }

    /**
     * Returns the <code>PBKDF2</code> bytes of the given password.
     *
     * @param password
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String hashPassword(String password, int keyLength) throws NoSuchAlgorithmException {
        String pbkdf2String = null;
        if (BeanUtils.isNotEmpty(password)) {
            String saltHexString = IOUtils.toHexString(parameters.getSalt());
            String keyAsHexString = keyAsHexString(password, keyLength);
            pbkdf2String = (parameters.getIterations() + ":" + saltHexString + ":" + keyAsHexString);
            pbkdf2String = GuardUtils.encodeToBase64String(pbkdf2String);
        }

        return pbkdf2String;
    }

    /**
     * Returns the <code>PBKDF2</code> bytes of the given password.
     *
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        return hashPassword(password, PBKDF2Generator.KEY_LENGTH);
    }

    /**
     * Validates the password with the hashed password.
     *
     * @param password
     * @param hashedPassword
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public boolean validatePassword(String password, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean validPassword = false;
        if (BeanUtils.isNotEmpty(password)) {
            hashedPassword = GuardUtils.decodeToBase64String(hashedPassword);
            String[] parts = hashedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = IOUtils.toHexBytes(parts[1]);
            byte[] hash = IOUtils.toHexBytes(parts[2]);
            byte[] pbkdf2Hash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);

            int difference = hash.length ^ pbkdf2Hash.length;
            for (int i = 0; i < hash.length && i < pbkdf2Hash.length; i++) {
                difference |= hash[i] ^ pbkdf2Hash[i];
            }

            validPassword = (difference == 0);
        }

        return validPassword;
    }
}
