package com.rslakra.appsuite.core.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rslakra.appsuite.core.BeanUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @created 3/27/23 11:35 AM
 */
public class WeekDaysTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(WeekDaysTest.class);

    @Test
    public void testOfString() {
        LOGGER.debug("+testOfString()");
        WeekDays weekDays = WeekDays.ofString(WeekDays.SUNDAY.name());
        LOGGER.debug("weekDays: {}", weekDays);
        assertEquals(WeekDays.SUNDAY, weekDays);

        WeekDays nullWeekDay = BeanUtils.findEnumByClass(WeekDays.class, "thruway");
        LOGGER.debug("nullWeekDay: {}", nullWeekDay);
        assertNull(nullWeekDay);

        WeekDays weekDayByClass = BeanUtils.findEnumByClass(WeekDays.class, WeekDays.MONDAY.name());
        LOGGER.debug("weekDayByClass: {}", weekDayByClass);
        assertEquals(WeekDays.MONDAY, weekDayByClass);

        WeekDays weekDayByFieldName = BeanUtils.findEnumByClass(WeekDays.class, "friday");
        LOGGER.debug("weekDayByFieldName: {}", weekDayByFieldName);
        assertNotNull(weekDayByFieldName);
        assertEquals(WeekDays.FRIDAY, weekDayByFieldName);
        LOGGER.debug("-testOfString()");
    }
}
