package com.rslakra.appsuite.core.trace;

/**
 * @author Rohtash Lakra
 * @created 6/9/21 11:25 AM
 */
public class CallerFactory {

    enum CallerType {
        CallerBySecurityManager,
        CallerByStackTrace;
    }

    /**
     * @param callerType
     * @return
     */
    public static Caller getCaller(CallerType callerType) {
        return (callerType == CallerType.CallerBySecurityManager ? new CallerBySecurityManager()
                                                                 : new CallerByStackTrace());
    }

}
