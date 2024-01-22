package com.devamatre.appsuite.core.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 11:25 AM
 */
public class CallerByStackTrace extends AbstractCaller implements Caller {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(CallerByStackTrace.class);

    public CallerByStackTrace() {
        super();
        setCallerClass(getCallerClass(3));
    }

//    /**
//     * @return
//     */
//    @Override
//    public int getOffset() {
// return getOffset(Thread.currentThread().getStackTrace(), getClass());
//    }

    /**
     * @param depth Depth of class to get. 0 = current method, 1 = caller, etc.
     * @return
     */
    @Override
    public Class<?> getCallerClass(final int depth) {
        LOGGER.debug("+getCallerClass({})", depth);
        try {
            setCallerClass(Class.forName(Thread.currentThread().getStackTrace()[getOffset() + depth].getClassName()));
            return getCallerClass();
        } catch (ClassNotFoundException e) {
            LOGGER.error("-getCallerClass()", e);
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /**
     * @param classType
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean isCaller(Class<T> classType) {
        LOGGER.debug("isCaller({})", classType);
        return Arrays.stream(Thread.currentThread().getStackTrace())
            .anyMatch(element -> element.getClassName().equals(classType.getName()));
    }

    /**
     * @return
     */
    @Override
    public Method getLastMethod() {
        return null;
    }

}
