package com.rslakra.appsuite.core.trace;

import java.lang.reflect.Method;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 11:21 AM
 */
public interface Caller {

    /**
     * @return
     */
    default int getOffset() {
        return 0;
    }

    /**
     * Returns the Class of the method which is one level above the current method.
     * <p>
     * For example, if you call WhoCalled.$.getCallingClass in the Example.whoCalled(), which was called by Main.main(),
     * it will return Main.class.
     *
     * @return Calling class
     */
    Class<?> getCallerClass();

    /**
     * Returns the Class of the method which is (depth) levels above the current method.
     * <p>
     * For example, if you call WhoCalled.$.getCallingClass(0) in the Example class, it will return Example.class.
     *
     * @param depth Depth of class to get. 0 = current method, 1 = caller, etc.
     * @return Calling class
     */
    Class<?> getCallerClass(int depth);

    /**
     * Returns whether the given class is one of the calling classes
     *
     * @param classType Class to search for in callers
     * @return true if the class classType is a caller of this class
     */
    <T> boolean isCaller(Class<T> classType);

    /**
     * @return
     */
    public Method getLastMethod();

    /**
     * @return
     */
    public String getLastMethodName();

}
