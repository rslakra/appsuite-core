package com.rslakra.appsuite.core.entity;

import java.math.BigDecimal;

/**
 * @author Rohtash Lakra
 * @created 3/27/23 4:24 PM
 */
public class Patient {

    private String name;
    private String gender;
    private BigDecimal height; // DECIMAL (2.2) - 4 digits in total like ##.##
    private BigDecimal weight; // DECIMAL (5.2) - 5 digts in total like ###.##

}
