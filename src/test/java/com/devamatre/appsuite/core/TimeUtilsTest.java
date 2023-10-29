package com.devamatre.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author Rohtash Lakra
 * @created 8/2/21 10:51 AM
 */
public class TimeUtilsTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtilsTest.class);

    /**
     * @return
     */
    private Date getLastDayOfMonth() {
        LocalDate localDate = LocalDate.parse("2019-09-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        localDate = localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear()));
        return TimeUtils.toDate(localDate);
    }

    @Test
    public void testTimeZone() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        LOGGER.debug("defaultTimeZone:" + defaultTimeZone);

        TimeZone timeZoneById = TimeZone.getTimeZone("Brazil/East");
        LOGGER.debug("timeZoneById:" + timeZoneById);

        timeZoneById = TimeZone.getTimeZone("America/Sao_Paulo");
        LOGGER.debug("timeZoneById:" + timeZoneById);
    }

    @Test
    public void testString() {
        final Date date = new Date();
        LOGGER.debug("Date: {}", date);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        LOGGER.debug("GMT Date: {}", TimeUtils.toString(timeZone, TimeUtils.UTC_DATE_FORMAT, date));

        timeZone = TimeZone.getTimeZone("PST");
        LOGGER.debug("PST Date: {}", TimeUtils.toString(timeZone, TimeUtils.UTC_DATE_FORMAT, date));

        timeZone = TimeZone.getTimeZone("EST");
        LOGGER.debug("EST Date: {}", TimeUtils.toString(timeZone, TimeUtils.UTC_DATE_FORMAT, date));

        timeZone = TimeZone.getTimeZone("IST");
        LOGGER.debug("IST Date: {}", TimeUtils.toString(timeZone, TimeUtils.UTC_DATE_FORMAT, date));

        timeZone = null;
        LOGGER.debug("Default Date: {}", TimeUtils.toString(timeZone, TimeUtils.UTC_DATE_FORMAT, date));
    }


    @Test
    public void testDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        LOGGER.debug("calendar:" + TimeUtils.toString(TimeUtils.DATE_ONLY_FORMAT, calendar));
        LOGGER.debug("isMarkerDate:" + TimeUtils.isMarkerDate(calendar));
        LOGGER.debug("isDateAfterEpoch:" + TimeUtils.isDateAfterEpoch(calendar));

        Calendar reset = Calendar.getInstance();
        reset.setTimeInMillis(0);
        LOGGER.debug("reset:" + TimeUtils.toString(TimeUtils.DATE_ONLY_FORMAT, reset));
        LOGGER.debug("isMarkerDate:" + TimeUtils.isMarkerDate(reset));
        LOGGER.debug("isDateAfterEpoch:" + TimeUtils.isDateAfterEpoch(reset));

        Calendar epoch = Calendar.getInstance();
        Date epochDate = Date.from(Instant.parse("1970-01-01T00:00:00.00Z"));
        LOGGER.debug("epochDate:" + TimeUtils.toString(TimeUtils.DATE_ONLY_FORMAT, epochDate));
        epoch.setTimeInMillis(epochDate.getTime());
        LOGGER.debug("epoch:" + TimeUtils.toString(TimeUtils.DATE_ONLY_FORMAT, epoch));
        LOGGER.debug("isMarkerDate:" + TimeUtils.isMarkerDate(epoch));
        LOGGER.debug("isDateAfterEpoch:" + TimeUtils.isDateAfterEpoch(epoch));
    }

    /**
     * @param period
     */
    public static void logDate(Long period) {
        Date date = TimeUtils.newDate(period);
        LOGGER.debug("{}", date);
        String dateFormatted = TimeUtils.toString(TimeUtils.DATE_ONLY_FORMAT, date);
        LOGGER.debug(dateFormatted);
    }

    @Test
    public void testPrintDate() {
        //
        logDate(1559347200000L);
        logDate(1467331200000L);

        LOGGER.debug("{}", new Date(System.currentTimeMillis() - 24 * 3600 * 1000));
        LOGGER.debug("{}", new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
        LOGGER.debug("{}", new Date(System.currentTimeMillis() + 24 * 3600 * 1000));

        String pattern = "mm-dd-yyyy";
        Date date = TimeUtils.getDateByDayOfMonth(3);
        LOGGER.debug("date:" + date + ", " + TimeUtils.toString(pattern, date));

        pattern = "mm-dd-yyyy";
        date = TimeUtils.getDateByDayOfMonth(15);
        LOGGER.debug("date:" + date + ", " + TimeUtils.toString(pattern, date));

        pattern = "dd-MMM-yy";
        date = TimeUtils.getDateByDayOfMonth(25);
        LOGGER.debug("date:" + date + ", " + TimeUtils.toString(pattern, date));

        LOGGER.debug("getDateOnlyByDayOfMonth:" + TimeUtils.getDateOnlyByDayOfMonth(5));
        LOGGER.debug("getLastDayOfMonth:" + getLastDayOfMonth());
        LOGGER.debug("lastDayOfMonth:" + TimeUtils.lastDayOfMonth());
        LOGGER.debug("getNextMonthDateByDayOfMonth:" + TimeUtils.getNextMonthDateByDayOfMonth(9));
        LOGGER.debug("getNextMonthDateByDayOfMonth:" + TimeUtils.getNextMonthDateByDayOfMonth(2));

        testTimeZone();
        testDateString();
    }

    @Test
    public void testWeekOfYear() {
        final Date dateObject = new Date(1610784000000l);
        assertEquals(3, TimeUtils.weekOfYear(dateObject.getTime()));
        assertEquals(3, TimeUtils.weekOfYear(dateObject));
        assertEquals(1, TimeUtils.weekOfYear(2021, 1, 1));
        assertEquals(2, TimeUtils.weekOfYear(2021, 1, 7));
    }

    /**
     *
     */
    @Test
    public void testCountBusinessDaysBetweenDatesJava8() {
        LocalDate AUG_1ST = LocalDate.of(2021, 8, 1);
        LocalDate AUG_2ND = LocalDate.of(2021, 8, 2);
        LocalDate AUG_3RD = LocalDate.of(2021, 8, 3);
        LocalDate AUG_4TH = LocalDate.of(2021, 8, 4);
        LocalDate AUG_5TH = LocalDate.of(2021, 8, 5);
        LocalDate AUG_6TH = LocalDate.of(2021, 8, 6);
        LocalDate AUG_7TH = LocalDate.of(2021, 8, 7);
        LocalDate AUG_8TH = LocalDate.of(2021, 8, 8);
        LocalDate AUG_9TH = LocalDate.of(2021, 8, 9);
        LocalDate AUG_10TH = LocalDate.of(2021, 8, 10);
        LocalDate AUG_15TH = LocalDate.of(2021, 8, 15);
        LocalDate AUG_20TH = LocalDate.of(2021, 8, 20);
        LocalDate AUG_25TH = LocalDate.of(2021, 8, 25);
        LocalDate SEP_5TH = LocalDate.of(2021, 9, 5);
        LocalDate SEP_10TH = LocalDate.of(2021, 9, 10);

        final Optional<List<LocalDate>> holidays = Optional.of(Arrays.asList(
            // 2020
            LocalDate.of(2020, 11, 11),
            LocalDate.of(2020, 11, 26),
            LocalDate.of(2020, 12, 25),

            // 2021
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2021, 1, 18),
            LocalDate.of(2021, 2, 15),
            LocalDate.of(2021, 3, 31),
            LocalDate.of(2021, 5, 31),
            LocalDate.of(2021, 7, 5),
            LocalDate.of(2021, 9, 6),
            LocalDate.of(2021, 9, 9),
            LocalDate.of(2021, 9, 24)
        ));

        // same day : 0 days between
        assertEquals(0, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_1ST, holidays).size());
        assertEquals(1, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_2ND, holidays).size());
        assertEquals(3, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_4TH, holidays).size());
        // end on week-end
        assertEquals(5, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_7TH, holidays).size());
        assertEquals(5, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_8TH, holidays).size());

        // next week
        assertEquals(3, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, AUG_7TH, holidays).size());
        assertEquals(4, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, AUG_9TH, holidays).size());
        assertEquals(5, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, AUG_10TH, holidays).size());
        assertEquals(8, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, AUG_15TH, holidays).size());
        assertEquals(13, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, AUG_20TH, holidays).size());
        // start on saturday
        assertEquals(0, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_7TH, AUG_7TH, holidays).size());
        assertEquals(0, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_7TH, AUG_8TH, holidays).size());
        assertEquals(1, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_7TH, AUG_9TH, holidays).size());
        // start on sunday
        assertEquals(0, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_8TH, AUG_8TH, holidays).size());
        assertEquals(1, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_8TH, AUG_9TH, holidays).size());
        assertEquals(2, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_8TH, AUG_10TH, holidays).size());
        // go to next week
        assertEquals(10, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, AUG_15TH, holidays).size());
        // next month
        assertEquals(25, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_1ST, SEP_5TH, holidays).size());
        assertEquals(18, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_15TH, SEP_10TH, holidays).size());
        assertEquals(14, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_20TH, SEP_10TH, holidays).size());
        assertEquals(11, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_25TH, SEP_10TH, holidays).size());
        // start sat, go to next month
        assertEquals(26, TimeUtils.countBusinessDaysBetweenDatesJava8(AUG_4TH, SEP_10TH, holidays).size());
    }

}
