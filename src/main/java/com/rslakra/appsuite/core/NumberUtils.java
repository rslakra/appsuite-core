package com.rslakra.appsuite.core;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created 2017-04-20 07:30:07 PM
 * @created 1.0.0
 */
public enum NumberUtils {
    INSTANCE;

    /**
     * Returns true if the given string is a boolean.
     *
     * @param str
     * @return
     */
    public static boolean isBoolean(String str) {
        if (BeanUtils.isEmpty(str)) {
            return false;
        }

        return "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str);
    }

    /**
     * Returns true if the given string is a number.
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (BeanUtils.isEmpty(str)) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
