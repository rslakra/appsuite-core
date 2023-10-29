package com.devamatre.appsuite.core.trace;

import com.devamatre.appsuite.core.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 3:31 PM
 */
public abstract class AbstractCaller extends SecurityManager implements Caller {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCaller.class);

    // callerClass
    private Class<?> callerClass;

    // lastMethod
    private Method lastMethod;

    // lastMethodName
    private String lastMethodName;

    public AbstractCaller() {
    }

    /**
     * @return
     */
    @Override
    public Class<?> getCallerClass() {
        return callerClass;
    }

    /**
     * @param depth Depth of class to get. 0 = current method, 1 = caller, etc.
     * @return
     */
    @Override
    public Class<?> getCallerClass(int depth) {
        return callerClass;
    }

    /**
     * @param callerClass
     * @return
     */
    protected void setCallerClass(final Class<?> callerClass) {
        LOGGER.debug("setCallerClass({})", callerClass);
        this.callerClass = callerClass;
    }

    /**
     * @return
     */
    @Override
    public Method getLastMethod() {
        return lastMethod;
    }

    /**
     * @param lastMethod
     */
    protected void setLastMethod(final Method lastMethod) {
        LOGGER.debug("setLastMethod({})", lastMethod);
        this.lastMethod = lastMethod;
    }


    /**
     * @param classType
     * @return
     */
    protected int getOffset(final StackTraceElement[] stackTraceElements, final Class<?> classType) {
        for (int index = 0; index < stackTraceElements.length; index++) {
            if (stackTraceElements[index].getClassName().equals(classType.getName()) && "init"
                .equals(stackTraceElements[index].getMethodName())) {
                return index;
            }
        }

        return 0;
    }

    /**
     * @return
     */
    @Override
    public int getOffset() {
        LOGGER.debug("getOffset(), result 1");
        return 1;
    }

    /**
     * @param classType
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean isCaller(Class<T> classType) {
        LOGGER.debug("isCaller({})", classType);
        return false;
    }

    /**
     * @return
     */
    protected boolean hasMethod() {
        return (lastMethod != null);
    }

    /**
     * @return
     */
    @Override
    public String getLastMethodName() {
        if (BeanUtils.isEmpty(lastMethodName)) {
            Optional<StackTraceElement>
                stackTraceElement =
                Arrays.stream(Thread.currentThread().getStackTrace())
                    .filter(element -> element.getClassName().equals(getCallerClass().getName())).findFirst();
            if (stackTraceElement.isPresent()) {
                setLastMethodName(stackTraceElement.get().getMethodName());
            }
        }

        return lastMethodName;
    }

    /**
     * @param lastMethodName
     */
    protected void setLastMethodName(final String lastMethodName) {
        LOGGER.debug("setLastMethodName({})", lastMethodName);
        this.lastMethodName = lastMethodName;
    }
}
