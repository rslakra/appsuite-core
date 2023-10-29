package com.devamatre.appsuite.core;

import com.devamatre.appsuite.core.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rohtash Lakra
 * @created 8/2/21 10:38 AM
 */
public enum TimeUtils {
    INSTANCE;

    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOCAL_TIME_FORMAT = "HH:mm:ss.SSS";
    // If the millisecond part consists of 1, 2, 3 digits or is optional,
    public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]";
    // If the millisecond part consists of 1, 2, 3 digits or is optional,
    public static final String ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSS]z";

    public static final String DATE_ONLY_FORMAT = "dd-MMM-yy";
    public static final String DATE_FORMAT = "dd-MMM-yy hh:mm:ss a";
    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat();

    /**
     * @param period
     * @return
     */
    public static Date newDate(final Long period) {
        return new Date(period);
    }

    /**
     * Returns the date format with the pattern.
     *
     * @param pattern
     * @return
     */
    public static SimpleDateFormat newDateFormat(final String pattern) {
        return new SimpleDateFormat(Pattern.compile(pattern).pattern());
    }

    /**
     * @param pattern
     * @param date
     * @return
     */
    public static String toString(final TimeZone timeZone, final String pattern, final Date date) {
        final SimpleDateFormat dateFormat = newDateFormat(pattern);
        if (BeanUtils.isNotNull(timeZone)) {
            dateFormat.setTimeZone(timeZone);
        }

        return dateFormat.format(date);
    }

    /**
     * @param pattern
     * @param date
     * @return
     */
    public static String toString(final String pattern, final Date date) {
        return toString(UTC_TIME_ZONE, pattern, date);
    }

    /**
     * @param pattern
     * @param calendar
     * @return
     */
    public static String toString(final String pattern, final Calendar calendar) {
        return (calendar == null ? null : toString(pattern, calendar.getTime()));
    }

    /**
     * Converts the <code>Date</code> into <code>LocalDate</code> object.
     *
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(final Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Converts the <code>Date</code> into <code>LocalTime</code> object.
     *
     * @param date
     * @return
     */
    public static LocalTime toLocalTime(final Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Converts the <code>Date</code> into <code>LocalDateTime</code> object.
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @param localDate
     * @return
     */
    public static Date toDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param localDateTime
     * @return
     */
    public static Date toDate(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @return
     */
    public static LocalDate now() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @param day
     * @return
     */
    public static Date getDateByDayOfMonth(final int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * @param day
     * @return
     */
    public static Date getDateOnlyByDayOfMonth(int day) {
        return toDate(toLocalDate(getDateByDayOfMonth(day)));
    }

    /**
     * @param day
     * @return
     */
    public static Date getNextMonthDateByDayOfMonth(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return calendar.getTime();
    }

    /**
     * @return
     */
    public static Date lastDayOfMonth() {
        LocalDate localDate = LocalDate.now();
        ValueRange range = localDate.range(ChronoField.DAY_OF_MONTH);
        LocalDate newDate = localDate.withDayOfMonth(Long.valueOf(range.getMaximum()).intValue());
        return toDate(newDate);
    }

    /**
     * Returns true if the calendar is not null and calendar time is after epoch (1970/1/1).
     *
     * @param calendar
     * @return
     */
    public static boolean isDateAfterEpoch(final Calendar calendar) {
        return (calendar != null && calendar.getTime().after(Date.from(Instant.EPOCH)));
    }

    /**
     * Returns true if the calendar date is either null or calendar's timeInMills set to be 0. The marker date is set to
     * be empty to notify the validator or merger that the date to be set is null.
     *
     * @param calendar
     * @return
     */
    public static boolean isMarkerDate(final Calendar calendar) {
        return (calendar == null || calendar.getTimeInMillis() == 0);
    }

    /**
     * Returns true if the startTime and endTime are not null and startTime is before endTime.
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isValidateDates(final Calendar startTime, final Calendar endTime) {
        return (startTime != null && endTime != null && startTime.getTime().before(endTime.getTime()));
    }


    /**
     * @param day
     * @return
     */
    public static long getDateByDay(final int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime().getTime();
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long toMillis(final int year, final int month, final int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime().getTime();
    }

    /**
     * Returns the week from the given milliseconds.
     *
     * @param milliSeconds
     * @return
     */
    public static int weekOfYear(final Long milliSeconds) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Returns the week from the given milliseconds.
     *
     * @param date
     * @return
     */
    public static int weekOfYear(final Date date) {
        return weekOfYear(date.getTime());
    }

    /**
     * Returns the week from the given milliseconds.
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int weekOfYear(final int year, int month, int day) {
        // The ChronoField.ALIGNED_WEEK_OF_YEAR is not the accurate.
// return LocalDate.of(year, month, day).get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        return LocalDate.of(year, month, day).get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    /**
     * Counts business working days.
     * <p>
     * Counts the number of business days between two <code>startDate</code> and <code>endDate</code> dates. The
     * business days are considered all weekdays, excluding all holidays falling on weekdays.
     * <p>
     * The method takes an optional holiday list and uses predicates to check if a day is weekend or holiday.
     *
     * @param startDate
     * @param endDate
     * @param holidays
     * @return
     */
    public static List<LocalDate> countBusinessDaysBetweenDatesJava8(final LocalDate startDate, final LocalDate endDate,
                                                                     final Optional<List<LocalDate>> holidays) {
        LOGGER.debug("+countBusinessDaysBetweenDatesJava8({}, {}, {})", startDate, endDate, holidays);
        // Validate method arguments
        if (BeanUtils.isNull(startDate) || BeanUtils.isNull(endDate)) {
            throw new InvalidRequestException("Invalid Dates! startDate=%s, endDate=%s", startDate, endDate);
        }

        // Predicate 1: Is a given date is a holiday
        Predicate<LocalDate> isHoliday = date -> holidays.isPresent() && holidays.get().contains(date);
        LOGGER.debug("isHoliday: {}", isHoliday);

        // Predicate 2: Is a given date is a weekday
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                                                 || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        LOGGER.debug("isWeekend: {}", isWeekend);

        // Get all days between two dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        LOGGER.debug("daysBetween: {}", daysBetween);

        List<LocalDate> localDatesBetween = Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(daysBetween + 1)
            .collect(Collectors.toList());
        LOGGER.debug("localDatesBetween: {}", localDatesBetween);

        // Iterate over stream of all dates and check each day against any weekday or holiday
        List<LocalDate> localDates = Stream.iterate(startDate, date -> date.plusDays(1))
            .limit(daysBetween + 1)
            .filter(isHoliday.or(isWeekend).negate())
            .collect(Collectors.toList());
        LOGGER.debug("-countBusinessDaysBetweenDatesJava8(), localDates: {}", localDates);
        return localDates;
    }

// private static List<LocalDate> countBusinessDaysBetweenDatesJava9(final LocalDate startDate,
//                                                                      final LocalDate endDate,
//                                                                      final Optional<List<LocalDate>> holidays) {
// // Validate method arguments
// if (startDate == null || endDate == null) {
//            throw new IllegalArgumentException(
//                "Invalid method argument(s) to countBusinessDaysBetween (" + startDate + "," + endDate + "," + holidays
//                + ")");
// }
//
// // Predicate 1: Is a given date is a holiday
// Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
//                                                 && holidays.get().contains(date);
//
// // Predicate 2: Is a given date is a weekday
// Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
//                                                 || date.getDayOfWeek() == DayOfWeek.SUNDAY;
//
// // Iterate over stream of all dates and check each day against any weekday or
// // holiday
// List<LocalDate> businessDays = startDate.datesUntil(endDate)
//            .filter(isWeekend.or(isHoliday).negate())
//            .collect(Collectors.toList());
//
// return businessDays;
// }


    /**
     * @param date
     * @return
     */
    public static String toGMTDate(final Date date) {
        LOGGER.debug("toGMTDate({})", date);
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * @param date
     * @return
     */
    public static String toISODate(final Date date) {
        LOGGER.debug("toISODate({})", date);
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static Calendar ofDateTime(final int year, final int month, final int day, final int hour,
                                      final int minute) {
        LOGGER.debug("+ofDateTime({}, {}, {}, {}, {})", year, month, day, hour, minute);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        LOGGER.debug("-ofDateTime(), calendar: {}", calendar);
        return calendar;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Calendar ofDateTime(final int year, final int month, final int day) {
        return ofDateTime(year, month, day, 0, 0);
    }

    /**
     * @param dateString
     * @param pattern
     * @param timeZone
     * @return
     */
    public static Date parseDate(String dateString, String pattern, String timeZone) {
        LOGGER.debug("+parseDate({}, {}, {})", dateString, pattern, timeZone);
        Date date = null;
        if (BeanUtils.isNotEmpty(dateString)) {
            try {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                if (BeanUtils.isNotEmpty(timeZone)) {
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
                }

                // parse date
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException ex) {
                LOGGER.error("Error parsing date: {} with pattern:{} and timeZone:{}", dateString, pattern, timeZone);
            }
        }

        LOGGER.debug("-parseDate(), date:{}", date);
        return date;
    }

    /**
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateString, String pattern) {
        return parseDate(dateString, pattern, TimeZone.getDefault().getDisplayName());
    }

    /**
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        return parseDate(dateString, DATE_TIME_PATTERN);
    }

}

