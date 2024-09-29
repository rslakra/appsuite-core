package com.rslakra.appsuite.core.enums;

import lombok.Getter;

/**
 * @author Rohtash Lakra
 * @created 3/21/23 12:48 PM
 */
@Getter
public enum DateTimeFormat {
    LOCAL_DATE("yyyy-MM-dd"),
    LOCAL_TIME("HH:mm:ss.SSS"),
    LOCAL_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    ZONED_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSSz"),
    ;

    private final String pattern;

    DateTimeFormat(final String pattern) {
        this.pattern = pattern;
    }

}
