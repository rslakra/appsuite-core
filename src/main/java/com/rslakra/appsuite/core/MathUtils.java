package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 09/16/2019 11:56:31 AM
 */
public enum MathUtils {
    INSTANCE;

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);

    /**
     * Returns the <code>number</code> after right shifting the <code>number</code> to the given <code>rightShift</code>
     * values.
     *
     * @param number
     * @param rightShift
     * @return
     */
    public static final int rightShift(int number, int rightShift) {
        return (number / Double.valueOf(Math.pow(2, rightShift)).intValue());
    }

    /**
     * Returns the <code>number</code> after right shifting the <code>number</code> to the given <code>leftShift</code>
     * values.
     *
     * @param number
     * @param leftShift
     * @return
     */
    public static final int leftShift(int number, int leftShift) {
        return (number * Double.valueOf(Math.pow(2, leftShift)).intValue());
    }

    /**
     * Sets the scale to 2 decimal places with rounding half-up.
     *
     * @param newScale
     * @param bigDecimal
     * @return
     */
    public static final BigDecimal setRoundingScale(final int newScale, final BigDecimal bigDecimal) {
        return (bigDecimal == null ? bigDecimal : bigDecimal.setScale(newScale, RoundingMode.HALF_UP));
    }

    /**
     * Sets the scale to 2 decimal places with rounding half-up.
     *
     * @param bigDecimal
     * @return
     */
    public static final BigDecimal setRounding2Scale(final BigDecimal bigDecimal) {
        return setRoundingScale(2, bigDecimal);
    }

    /**
     * Approach - 1
     * <p>
     * Counts digits by running loop.
     *
     * @param number
     * @return
     */
    public static int countDigitsNaiveApproach1(final long number) {
        LOGGER.debug("+countDigitsNaiveApproach1({})", number);

        int digits = 0;
        long tempNumber = number;
        while (tempNumber != 0) {
            digits++;
            tempNumber /= 10;
        }

        LOGGER.debug("-countDigitsNaiveApproach1(), {} has {} digit(s).", number, digits);
        return digits;
    }


    /**
     * Approach - 2
     * <p>
     * Counts digits by running loop.
     *
     * @param number
     * @return
     */
    public static int countDigitsNaiveApproach2(final long number) {
        LOGGER.debug("+countDigitsNaiveApproach2({})", number);

        int digits = 0;
        long tempNumber = 1;
        while (tempNumber <= number) {
            digits++;
            tempNumber *= 10;
        }

        LOGGER.debug("-countDigitsNaiveApproach2(), {} has {} digit(s).", number, digits);
        return digits;
    }

    /**
     * @param number
     * @return
     */
    public static int countDigitsNaive(final long number) {
        LOGGER.debug("+countDigitsNaive({})", number);
        int digits = Double.valueOf(Math.log10(number) + 1).intValue();
        LOGGER.debug("-countDigitsNaive(), {} has {} digit(s).", number, digits);
        return digits;
    }

    /**
     * Returns the number of digits in the given number and base.
     *
     * @param number
     * @param base
     */
    public static int countDigits(final long number, final int base) {
        LOGGER.debug("countDigits({}, {})", number, base);
        /* Calculating log using base changing the property and then taking it floor and then adding 1. */
        if (base == 10) {
            return (number == 0 ? 1 : Double.valueOf(Math.log10(number) + 1).intValue());
        } else {
            return Double.valueOf(Math.floor(Math.log(number) / Math.log(base)) + 1).intValue();
        }
    }

    /**
     * Returns the number of digits in the given number and base 10.
     *
     * @param number
     * @return
     */
    public static int countDecimalDigits(final long number) {
        return countDigits(number, 10);
    }

    /**
     * Returns the number of digits in the given number and base 10.
     *
     * @param number
     * @return
     */
    public static int countBinaryDigits(final long number) {
        return countDigits(number, 2);
    }

    /**
     * Returns the first <code>digits</code> digits number.
     *
     * @param digits
     * @return
     */
    public static long firstNDigitNumber(final int digits) {
        return Double.valueOf(Math.pow(10, digits - 1)).longValue();
    }

    /**
     * @param number
     * @return
     */
    public static int toInteger(long number) {
        return Long.valueOf(number).intValue();
    }

    /**
     * Returns the integer equivalent.
     *
     * @param numString
     * @return
     */
    public static int toInteger(final String numString) {
        return Integer.parseInt(numString);
    }

    /**
     * @param count
     * @param withZero
     * @return
     */
    private static List<Integer> listNaturalNumbers(final int count, final boolean withZero) {
        LOGGER.debug("+listNaturalNumbers({}, {})", count, withZero);
        List<Integer> listIntegers = new ArrayList<>(count);
        if (withZero) {
            listIntegers.add(0);
        }

        for (int i = 1; i <= count; i++) {
            listIntegers.add(i);
        }

        LOGGER.debug("-listNaturalNumbers(), listIntegers: {}", listIntegers);
        return listIntegers;
    }

    /**
     * natural numbers.
     *
     * @param count
     * @return
     */
    public static List<Integer> listNaturalNumbers(int count) {
        return listNaturalNumbers(count, false);
    }

    /**
     * @param count
     * @return
     */
    public static List<Integer> listNaturalNumbersWithZero(int count) {
        return listNaturalNumbers(count, true);
    }

    /**
     * @param count
     * @return
     */
    public static List<Integer> listEvenNumbers(int count) {
        LOGGER.debug("+listEvenNumbers({})", count);
        final List<Integer> evenNumbers = new ArrayList<>(count);
        int i = 0;
        while (count > 0 && evenNumbers.size() < count) {
            evenNumbers.add((i += 2));
        }

        LOGGER.debug("-listEvenNumbers(), evenNumbers: {}", evenNumbers);
        return evenNumbers;
    }

    /**
     * @param numbers
     * @return
     */
    public static boolean isEvenCheckUsingModulus(int numbers) {
        return (numbers % 2 == 0);
    }

    /**
     * @param numbers
     * @return
     */
    public static boolean isEvenCheckUsingOperators(int numbers) {
        return ((numbers / 2) /* quotient */ * 2 == numbers);
    }

    /**
     * @param numbers
     * @return
     */
    public static boolean isEvenCheckUsingBit(int numbers) {
        return ((numbers & 1) == 0);
    }

    /**
     * @param listNumbers
     * @return
     */
    public static boolean hasEvenNumbers(List<Integer> listNumbers) {
        listNumbers.forEach(num -> {
            if (!isEvenCheckUsingBit(num)) {
                throw new RuntimeException("MathUtils are not even!");
            }
        });

        return true;
    }

    /**
     * @param listNumbers
     * @return
     */
    public static List<Integer> filterEvenNumbers(List<Integer> listNumbers) {
        final List<Integer> evenNumbers = new ArrayList<>(listNumbers.size());
        listNumbers.forEach(num -> {
            if (!isEvenCheckUsingBit(num)) {
                throw new RuntimeException("MathUtils are not even!");
            }

            evenNumbers.add(num);
        });

        return evenNumbers;
    }

    /**
     * Returns true if the value is not null and value == -1 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean lessThanZero(final BigDecimal value) {
        return (value != null && value.signum() == -1);
    }

    /**
     * Returns true if the <code>leftValue</code> and <code>rightValue</code> are not null and the
     * <code>leftValue</code> <= <code>rightValue</code> otherwise false.
     *
     * @param leftValue
     * @param rightValue
     * @return
     */
    public static boolean lessThanEqualTo(final BigDecimal leftValue, final BigDecimal rightValue) {
        return (leftValue != null && rightValue != null && leftValue.compareTo(rightValue) <= 0);
    }

    /**
     * Returns true if the value is not null and value <= 0 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean lessThanEqualToZero(final BigDecimal value) {
        return lessThanEqualTo(value, BigDecimal.ZERO);
    }

    /**
     * Returns true if the value is not null and value == 0 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean equalToZero(final BigDecimal value) {
        return (value != null && value.signum() == 0);
    }

    /**
     * Returns true if the value is not null and value != 0 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean notEqualToZero(final BigDecimal value) {
        return (value != null && value.signum() != 0);
    }

    /**
     * Returns true if the value is not null and value == 1 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean greaterThanZero(final BigDecimal value) {
        return (value != null && value.signum() == 1);
    }

    /**
     * Returns true if the <code>leftValue</code> and <code>rightValue</code> are not null and the
     * <code>leftValue</code> >= <code>rightValue</code> otherwise false.
     *
     * @param leftValue
     * @param rightValue
     * @return
     */
    public static boolean greaterThanEqualToZero(final BigDecimal leftValue, final BigDecimal rightValue) {
        return (leftValue != null && rightValue != null && leftValue.compareTo(rightValue) >= 0);
    }

    /**
     * Returns true if the value is not null and value >= 0 otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean greaterThanEqualToZero(final BigDecimal value) {
        return greaterThanEqualToZero(value, BigDecimal.ZERO);
    }

    /**
     * Returns true if the <code>amount</code> is < 0 (Zero) otherwise false.
     *
     * @param amount
     * @return
     */
    public static boolean isVoidCharge(final BigDecimal amount) {
        return lessThanZero(amount);
    }

}
