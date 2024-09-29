package com.rslakra.appsuite.core.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 11:25 AM
 */
public class CallerBySecurityManager extends AbstractCaller implements Caller {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(CallerBySecurityManager.class);

    public CallerBySecurityManager() {
        super();
        setCallerClass(getCallerClass(2));
    }

//    /**
//     * @return
//     */
//    @Override
//    public int getOffset() {
// return getOffset(Thread.currentThread().getStackTrace(), getClass()) + 1;
//    }

    /**
     * @param depth Depth of class to get. 0 = current method, 1 = caller, etc.
     * @return
     */
    @Override
    public Class<?> getCallerClass(final int depth) {
        LOGGER.debug("getCallerClass({})", depth);
        Class<?> abstractCaller = getClassContext()[getOffset() + depth];
        if (abstractCaller == AbstractCaller.class) {
            abstractCaller = getClassContext()[getOffset() + depth + 1];
        }
        setCallerClass(abstractCaller);

        return getCallerClass();
    }

    /**
     * @param classType
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean isCaller(final Class<T> classType) {
        LOGGER.debug("isCaller({})", classType);
        return Arrays.stream(getClassContext()).anyMatch(classContext -> classContext == classType);
    }

//    /**
////     * @return
////     */
////    @Override
////    public Method getLastMethod() {
////        if (!hasMethod()) {
////            Method[] methods = getCallerClass().getMethods();
////            LOGGER.debug("methods:{}", Arrays.toString(methods));
////            setMethod(null);
////        }
////
////        return super.getLastMethod();
////    }

}
