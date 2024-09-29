package com.rslakra.appsuite.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 5/7/20 6:10 PM
 */
//@JsonDeserialize(using = DistanceUnitTypeDeserializer.class)
@JsonSerialize(using = LowerCaseSerializer.class)
public enum DistanceUnitType {
    MILE,
    KILOMETER,
    METER,
    CENTIMETER,
    MILLIMETER;


    @JsonCreator
    public static DistanceUnitType forValues(@JsonProperty("distance") String distance) {
        return Arrays.stream(values())
            .filter(entry -> entry.name().equalsIgnoreCase(distance))
            .findFirst()
            .orElse(null);
    }

}
