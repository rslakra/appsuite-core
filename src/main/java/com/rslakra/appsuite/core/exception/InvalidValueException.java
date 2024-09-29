package com.rslakra.appsuite.core.exception;

/**
 * Thrown when provided value is not able to do calculations (like divide by zero) or something else.
 *
 * @author Rohtash Lakra
 * @created 7/27/23 8:39 PM
 */
public class InvalidValueException extends RuntimeException {

    /**
     *
     */
    public InvalidValueException() {
    }

    /**
     * @param message
     */
    public InvalidValueException(String message) {
        super(message);
    }

    /**
     * @param pattern
     * @param args
     */
    public InvalidValueException(final String pattern, final Object... args) {
        this(String.format("Record already exists with %s!", String.format(pattern, args)));
    }


    /**
     * @param message
     * @param cause
     */
    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param pattern
     * @param cause
     * @param args
     */
    public InvalidValueException(final String pattern, final Throwable cause, final Object... args) {
        this(String.format("Record already exists with %s!", String.format(pattern, args)), cause);
    }

    /**
     * @param cause
     */
    public InvalidValueException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidValueException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
