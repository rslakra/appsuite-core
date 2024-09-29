package com.rslakra.appsuite.core.exception;

import com.rslakra.appsuite.core.BeanUtils;

/**
 * @author Rohtash Lakra
 * @created 10/28/22 1:32 PM
 */
public class ServerRuntimeException extends RuntimeException {

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     */
    public ServerRuntimeException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     *
     * @param message
     */
    public ServerRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param message
     * @param cause
     */
    public ServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains
     * the class and detail message of <tt>cause</tt>). This constructor is useful for exceptions that are little more
     * than wrappers for other throwables (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause
     */
    public ServerRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and
     * writable stack trace enabled or disabled.
     *
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    protected ServerRuntimeException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Returns the default message if <code>message</code> is null or empty.
     *
     * @param message
     * @param defaultMessage
     * @return
     */
    public static String nullSafeMessage(final String message, final String defaultMessage) {
        return (BeanUtils.isEmpty(message) ? defaultMessage : message);
    }
}
