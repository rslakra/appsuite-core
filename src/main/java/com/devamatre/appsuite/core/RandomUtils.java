package com.devamatre.appsuite.core;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @author Rohtash Lakra
 * @created 7/15/23 6:42 PM
 */
public enum RandomUtils {
    INSTANCE;

    private static final Random RANDOM = new Random();

    /**
     * @return
     */
    public static Random getRandom() {
        return RANDOM;
    }

    /**
     * @return
     */
    public static UUID nextRandomUuid() {
        return UUID.randomUUID();
    }

    /**
     * @param bound
     * @return
     */
    public static int nextRandomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    /**
     * @return
     */
    public static int nextRandomPercentage() {
        return nextRandomInt(100);
    }

    /**
     * @return
     */
    public static Long nextRandomLong() {
        return RANDOM.nextLong();
    }

    /**
     * @return
     */
    public static Double nextRandomDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * @return
     */
    public static BigDecimal nextRandomBigDecimal() {
        return BigDecimal.valueOf(RANDOM.nextDouble());
    }

    /**
     * @param bytes
     */
    public static void nextRandomBytes(byte[] bytes) {
        RANDOM.nextBytes(bytes);
    }

    /**
     * @return
     */
    public static Long nextRandomId() {
        return RANDOM.nextLong();
    }

    /**
     * @return
     */
    public static double nextRandomLatitude() {
        return RANDOM.nextDouble() * 180 - 90;
    }

    /**
     * @return
     */
    public static BigDecimal nextLatitudeBigDecimal() {
        return BigDecimal.valueOf(nextRandomLatitude());
    }

    /**
     * @return
     */
    public static double nextRandomLongitude() {
        return RANDOM.nextDouble() * 360 - 180;
    }

    /**
     * @return
     */
    public static BigDecimal nextLongitudeBigDecimal() {
        return BigDecimal.valueOf(nextRandomLongitude());
    }

    /**
     * Generates the new phone number
     *
     * @return
     */
    public static String nextRandomPhoneNumber() {
        return RANDOM.nextInt(999) + "-" + RANDOM.nextInt(999) + "-" + RANDOM.nextInt(9999);
    }

    /**
     * @param prefix
     * @return
     */
    public static String nextRandomString(String prefix) {
        return Objects.toString(prefix) + RANDOM.nextInt(1000000);
    }

    /**
     * @return
     */
    public static LocalDateTime nextDateTime() {
        return LocalDateTime.now();
    }

    /**
     * @return
     */
    public static String nextRandomEmail() {
        return nextRandomString("email") + "@email.com";
    }

    /**
     * @return
     */
    public static String nextRandomFirstName() {
        return nextRandomString("FirstName");
    }

    /**
     * @return
     */
    public static String nextRandomLastName() {
        return nextRandomString("LastName");
    }

}
