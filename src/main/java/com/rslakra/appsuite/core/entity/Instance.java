package com.rslakra.appsuite.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 1/11/23 9:05 AM
 */
@Getter
@Setter
@NoArgsConstructor
public class Instance {

    private String family;
    private String generation;
    private String properties;
    private String size;
}
