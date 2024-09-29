package com.rslakra.appsuite.core.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 12:01 PM
 */
public class CallerTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(CallerTest.class);
    private static String CLASS_NAME = CallerTest.class.getName();
    private static String CLASS_SIMPLE_NAME = CallerTest.class.getSimpleName();

    /**
     * @param classType
     */
    private void validateCallerClass(Class<?> classType) {
        LOGGER.debug("+validateCallerClass({})", classType);
        assertNotNull(classType);
        assertEquals(CallerTest.class, classType);
        assertEquals(CLASS_NAME, classType.getName());
        assertEquals(CLASS_SIMPLE_NAME, classType.getSimpleName());
        LOGGER.debug("-validateCallerClass()");
    }

    @Test
    public void testCallerBySecurityManager() {
        LOGGER.debug("+testCallerBySecurityManager()");
        Caller caller = CallerFactory.getCaller(CallerFactory.CallerType.CallerBySecurityManager);
        LOGGER.debug("caller:{}", caller);
        assertNotNull(caller);
        validateCallerClass(caller.getCallerClass());
        assertTrue(caller.isCaller(CallerTest.class));
        assertEquals("testCallerBySecurityManager", caller.getLastMethodName());
        LOGGER.debug("-testCallerBySecurityManager()");
    }

    @Test
    public void testCallerByStackTrace() {
        LOGGER.debug("+testCallerByStackTrace()");
        Caller caller = CallerFactory.getCaller(CallerFactory.CallerType.CallerByStackTrace);
        LOGGER.debug("caller:{}", caller);
        assertNotNull(caller);
        validateCallerClass(caller.getCallerClass());
        assertTrue(caller.isCaller(CallerTest.class));
        assertEquals("testCallerByStackTrace", caller.getLastMethodName());
        LOGGER.debug("-testCallerByStackTrace()");
    }
}
