package com.rslakra.appsuite.core.exception;

/**
 * @author Rohtash Lakra
 * @created 8/2/21 10:44 AM
 */
public class InvalidRequestException extends IllegalArgumentException {

    /**
     *
     */
    public InvalidRequestException() {
        super();
    }

    /**
     * @param string
     */
    public InvalidRequestException(String string) {
        super(string);
    }

    /**
     * @param pattern
     * @param args
     */
    public InvalidRequestException(String pattern, Object... args) {
        super(String.format(pattern, args));
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}
