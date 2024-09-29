package com.rslakra.appsuite.core;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 5/4/22 4:29 PM
 */
public enum HashUtils {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(HashUtils.class);
    public static final String ALGO_SHA256 = "SHA-256";
    private MessageDigest messageDigest;

    /**
     * @return
     */
    private MessageDigest getMessageDigest() {
        if (Objects.isNull(messageDigest)) {
            try {
                messageDigest = MessageDigest.getInstance(ALGO_SHA256);
                LOGGER.info("Initialized messageDigest!");
            } catch (NoSuchAlgorithmException ex) {
                LOGGER.info(ex.getLocalizedMessage(), ex);
                ex.printStackTrace();
            }
        }

        return messageDigest;
    }

    /**
     * @param objects
     * @return
     */
    public static long hashCode(final Object... objects) {
        return Objects.hashCode(objects);
    }

    /**
     * @param objects
     * @return
     */
    public static int hashCodePositive(final Object... objects) {
        return (Objects.hash(objects) & 0x7FFFFFFF);
    }

    /**
     * @param value
     * @return
     */
    public static String sha256Hex(final String value) {
        final byte[] hashBytes = INSTANCE.getMessageDigest().digest(value.getBytes(StandardCharsets.UTF_8));
        final String sha256hex = new String(Hex.encode(hashBytes));
        return sha256hex;
    }

    /**
     * @param values
     * @return
     */
    public static long length(byte[] values) {
        return (values.length >> 0);
    }

    /**
     * @param key
     * @return
     */
    public static <T> int getHashIndex(final T key, final int capacity) {
        /** hash-code can be negative and dictionary can throw ArrayIndexOutOfBoundException, so always use the positive hash-code for HashTable. */
        return (hashCodePositive(key) % capacity);
    }

}
