package com.rslakra.appsuite.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 5/7/20 3:11 PM
 */
@JsonDeserialize(using = WeekDayDeserializer.class)
//@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
//@JsonNaming(LowerCaseStrategy.class)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
//@JsonSerialize(using = WeekDaySerializer.class)
@JsonSerialize(using = LowerCaseSerializer.class)
public enum WeekDays {

    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    /**
     * @param weekDay
     * @return
     */
    @JsonCreator
    public static WeekDays forValues(@JsonProperty("weekDay") String weekDay) {
        return ofString(weekDay);
    }

    /**
     * @param weekDay
     * @return
     */
    public static WeekDays ofString(final String weekDay) {
        return Arrays.stream(WeekDays.values())
            .filter(entry -> entry.name().equalsIgnoreCase(weekDay))
            .findAny()
            .orElse(null);

    }

}
