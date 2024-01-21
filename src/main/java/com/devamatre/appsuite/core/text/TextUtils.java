/******************************************************************************
 * Copyright (C) Devamatre 2009 - 2022. All rights reserved.
 *
 * This code is licensed to Devamatre under one or more contributor license 
 * agreements. The reproduction, transmission or use of this code, in source 
 * and binary forms, with or without modification, are permitted provided 
 * that the following conditions are met:
 * <pre>
 *  1. Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * </pre>
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Devamatre reserves the right to modify the technical specifications and or 
 * features without any prior notice.
 *****************************************************************************/
package com.devamatre.appsuite.core.text;

import com.devamatre.appsuite.core.BeanUtils;
import com.devamatre.appsuite.core.exception.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created 2012-04-20 07:30:07 PM
 * @created 1.0.0
 */
public enum TextUtils {
    INSTANCE;

    /* constants */
    public static final char SPACE = '\u0020';
    public static final char SPACE_SEPARATOR = '\u00A0';
    public static final char LINE_SEPARATOR = '\u2007';
    public static final char PARAGRAPH_SEPARATOR = '\u202F';
    //    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String STR_SPACE = "" + SPACE;
    public static final String STR_LINE = "" + LINE_SEPARATOR;
    public static final String STR_PARAGRAPH = "" + PARAGRAPH_SEPARATOR;

    /* Horizontal Tabulation ('\t') */
    public static final char HTAB = '\u0009';
    /* Vertical Tabulation */
    public static final char VTAB = '\u000B';
    public static final char FORM_FEED = '\u000C';
    public static final char FILE_SEPARATOR = '\u001C';
    public static final char GROUP_SEPARATOR = '\u001D';
    public static final char RECORD_SEPARATOR = '\u001E';
    public static final char UNIT_SEPARATOR = '\u001F';
    public static final char LINE_FEED = '\n';
    public static final char CARRIAGE_RETURN = '\r';
    public static final String NEW_LINE = "\n";
    public static final String DELIMITER = "" + HTAB;
    public static final String VER_DELIMITER = "" + VTAB;
    public static final String EMPTY_STR = "";
    public static final String NULL = "null";

    // LOGGER.
    private static final Logger LOGGER = LoggerFactory.getLogger(TextUtils.class);

    /* valid email expression. */
    private static String
            VALID_EMAIL_EXPRESSION =
            "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    private static final String[] UNITS = {" ", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    private static final String[]
            TENS =
            {" ", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private static final String[]
            HUNDREDS =
            {" ", "Ten", "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    private static final String[] PREFIXES = {" ", "Thousand", "Lakhs", "Million"};

    /**
     * Removes leading whitespace of the specified string using expressions.
     *
     * @param str
     * @return
     */
    public static String lTrim(final String str) {
        return str.replaceAll("^\\s+", "");
    }

    /**
     * Remove trailing whitespace of the specified string using expressions.
     *
     * @param str
     * @return
     */
    public static String rTrim(final String str) {
        return str.replaceAll("\\s+$", "");
    }

    /**
     * Replaces multiple whitespace between words with single whitespace using expressions.
     *
     * @param str
     * @return
     */
    public static String iTrim(final String str) {
        return str.replaceAll("\\b\\s{2,}\\b", " ");
    }

    /**
     * Removes all redundant (leading, trailing and centric) whitespace of the specified string using expressions.
     *
     * @param str
     * @return String
     */
    public static String trim(final String str) {
        return iTrim(lTrim(rTrim(str)));
    }

    /**
     * Removes all redundant (leading, trailing and centric) delimiter characters from the specified string.
     *
     * @param str
     * @param delimiter
     * @return
     */
    public static String trim(String str, String delimiter) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+trim(" + str + ", " + delimiter + ")");
        }

        if (BeanUtils.isNotEmpty(str, delimiter)) {
            StringBuilder strBuilder = new StringBuilder(str);
            int index = strBuilder.indexOf(delimiter);
            while (index != -1 && index < strBuilder.length()) {
                if (delimiter.length() == 1) {
                    strBuilder.deleteCharAt(index);
                } else {
                    strBuilder.delete(index, index + delimiter.length());
                }
                index = strBuilder.indexOf(delimiter);
            }
            str = strBuilder.toString();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-trim(), str: " + str);
        }
        return str;
    }

    /**
     * Remove insentric White Spaces from a String.
     *
     * @param str
     * @return returns a String
     */
    public static String trims(final String str) {
        String orignal = str;
        if (BeanUtils.isNotEmpty(str)) {
            orignal = str.trim();
            String search = "  ";
            int index = 0;
            do {
                index = orignal.indexOf(search);
                if (index != -1) {
                    orignal = orignal.substring(0, index) + " " + orignal.substring(index + search.length());
                }
            } while (index != -1);
        }

        return orignal;
    }

    /**
     * Returns true if the string contains a space, otherwise false.
     *
     * @param str - to be checked for space.
     * @return true, if string contains space, otherwise false.
     */
    public static boolean hasSpace(String str) {
        if (BeanUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Invalid String!, str: " + str);
        }

        boolean hasSpace = false;
        for (int index = 0; index < str.length(); index++) {
            if (Character.isSpaceChar(str.charAt(index))) {
                hasSpace = true;
                break;
            }
        }
        return hasSpace;
    }

    /**
     * Check whether the specified string only contains digits including dot(.). If the specified string is null or
     * empty, it will return false.
     *
     * @param str - to be checked.
     * @return true if the string contains all the digits including dot, otherwise false.
     */
    public static boolean isNumeric(String str) {
        if (BeanUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Invalid String!, str: " + str);
        }

        boolean numeric = true;
        for (int index = 0; numeric && index < str.length(); index++) {
            if (!Character.isDigit(str.charAt(index)) && str.charAt(index) != '.') {
                numeric = false;
            }
        }
        return numeric;
    }

    /**
     * Check whether entered string is only contains digits including dot(.) not any alphabets is null
     */
    public boolean isDigitsWithDot(String str) {
        boolean isNumber = true;
        if (str == null) {
            return isNumber;
        }
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                isNumber = false;
            }
        }

        return isNumber;
    }

    /**
     * Returns true if the string contains only digits. The $ avoids a partial match, i.e. 1b.
     *
     * @param string
     * @return
     */
    public static boolean isDigits(String string) {
        return (string != null && string.length() > 0 && string.matches("^[0-9]*$"));
    }

    /**
     * @param string
     * @param useLambda
     * @return
     */
    public static boolean isDigits(String string, boolean useLambda) {
        if (useLambda) {
            return (string != null && string.chars().allMatch(x -> Character.isDigit(x)));
        } else {
            return isDigits(string);
        }
    }

    /**
     * @param data
     */
    public static void logTokens(String data) {
        System.out.println("data:" + data);
        if (data != null) {
            try {
                String decoded = URLDecoder.decode(data, "utf-8");
                System.out.println("decoded:" + decoded);
                String[] tokens = decoded.split("&");
                for (int i = 0; i < tokens.length; i++) {
                    String[] token = tokens[i].split("=");
                    if (token.length > 1) {
                        System.out.println(token[0] + "=" + token[1]);
                    } else {
                        System.out.println(token[0] + "=");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Replaces the find string to the with string. It is a multi-purpose method which can replace a single or multiple
     * characters.
     *
     * @param str
     * @param find
     * @param with
     * @return
     */
    public static String replace(String str, String find, String with) {
        if (str != null) {
            StringBuilder strBuilder = new StringBuilder(str);
            int index = strBuilder.indexOf(find);
            while (index != -1 && index < strBuilder.length()) {
                strBuilder.replace(index, index + find.length(), with);
                index = strBuilder.indexOf(find);
            }
            str = strBuilder.toString();
        }
        return str;
    }

    /**
     * Returns true if a valid email address is passed. The email address criteria is: This means an email can start
     * with any combination of letters and numbers that is followed by any number of periods and letters and numbers. It
     * must have a @ character followed by a valid host name.
     * <p>
     * Expression Pattern Used: "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-
     * ]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b"
     *
     * @param eMailAddress
     * @return
     */
    public static boolean isValidEmail(final String eMailAddress) {
        Pattern pattern = Pattern.compile(VALID_EMAIL_EXPRESSION);
        Matcher matcher = pattern.matcher(VALID_EMAIL_EXPRESSION);
        return matcher.find();
    }

    /**
     * It returns true if the string matches exactly either "true"/"True" or "yes"/"Yes".
     *
     * @param str
     * @return
     */
    public static boolean isTrueOrYes(String str) {
        return str.matches("[tT]rue|[yY]es");
    }

    /**
     * @param itr
     */
    public static void logIterator(final Iterator<?> itr) {
        BeanUtils.assertNonNull(itr, "Iterator should not be null!");
        while (itr.hasNext()) {
            LOGGER.debug(itr.next().toString());
        }
    }


    /**
     * Returns the padded string. It fills your string with up to repeat characters with a whitespace and then all
     * whitespace are replaced with the provided padding string.
     *
     * @param str
     * @param repeat
     * @param padStr
     * @return
     */
    public static String padRight(String str, int repeat, String padStr) {
        return String.format("%1$-" + repeat + "s", str).replaceAll(" ", padStr);
    }

    /**
     * Returns the padded string. It fills your string with up to repeat characters with a whitespace and then all
     * whitespace are replaced with the provided padding string.
     *
     * @param str
     * @param repeat
     * @param padStr
     * @return
     */
    public static String padLeft(String str, int repeat, String padStr) {
        return String.format("%1$" + repeat + "s", str).replaceAll(" ", padStr);
    }

    /**
     * Returns the padded string. It fills your string with up to repeat characters with a whitespace and then all
     * whitespace are replaced with the provided padding string.
     *
     * @param str
     * @param repeat
     * @param padChar
     * @return
     */
    public static String padCenter(String str, int repeat, String padChar) {
        if (BeanUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Invalid String!, str: " + str);
        }

        if (repeat > 0) {
            int pads = repeat - str.length();
            if (pads > 0) {
                str = padLeft(str, str.length() + pads / 2, padChar);
                str = padRight(str, repeat, padChar);
            }
        }
        return str;
    }

    /**
     * Returns the string of padded characters.
     *
     * @param repeat
     * @param padChar
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static String repeat(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Invalid Index!, repeat: " + repeat);
        }
        final char[] cBuffer = new char[repeat];
        for (int i = 0; i < cBuffer.length; i++) {
            cBuffer[i] = padChar;
        }
        return new String(cBuffer);
    }

    /**
     * Returns the string truncated from the start index to end index.
     *
     * @param str
     * @param start The beginning index, inclusive.
     * @param end   The ending index, exclusive.
     * @return
     */
    public static String truncate(String str, int start, int end) {
        if (BeanUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Invalid String!, str: " + str);
        }

        if (start < 0 || start > end || end > str.length()) {
            throw new StringIndexOutOfBoundsException("Invalid Index!, start: " + start + ", endIndex:" + end);
        }

        StringBuilder sBuilder = new StringBuilder(str);
        sBuilder.delete(start, end);
        return sBuilder.toString();
    }

    /**
     * Returns the string in sentence case.
     *
     * @param str
     * @return
     */
    public static String toSentenceCase(String str) {
        if (BeanUtils.isEmpty(str)) {
            throw new NullPointerException("Invalid Parameter!, str: " + str);
        }

        return new StringBuilder().append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * @param str
     * @return
     */
    public static String toTitleCase(String str) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+toTitleCase(" + str + ")");
        }
        if (BeanUtils.isEmpty(str)) {
            throw new NullPointerException("Invalid Parameter!, str: " + str);
        }

        StringBuilder strBuilder = new StringBuilder();
        String[] words = str.split(" ");
        for (int index = 0; index < words.length; index++) {
            if (BeanUtils.isNotEmpty(words[index])) {
                strBuilder.append(toSentenceCase(words[index]));
                if (index < words.length - 1) {
                    strBuilder.append(SPACE);
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-toTitleCase(), result: " + strBuilder.toString());
        }
        return strBuilder.toString();
    }

    /**
     * @param str
     * @return
     */
    public static String toToggleCase(String str) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+toToggleCase(" + str + ")");
        }
        if (BeanUtils.isEmpty(str)) {
            throw new NullPointerException("Invalid Parameter!, str: " + str);
        }

        /* remove all unwanted spaces if any. */
        StringBuilder strBuilder = new StringBuilder();
        char ch = 0;
        for (int index = 0; index < str.length(); index++) {
            ch = str.charAt(index);
            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }
            strBuilder.append(ch);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-toToggleCase(), result: " + strBuilder.toString());
        }
        return strBuilder.toString();
    }

    /**
     * Returns the string created by joining the specified elements, separated by using the specified delimiter. if the
     * delimiter is null or empty default delimiter space is used. if the passed elements array is null, return null;
     *
     * @param elements
     * @return
     */
    public static String join(String[] elements, String delimiter) {
        StringBuilder sBuilder = null;
        if (elements != null) {
            if (BeanUtils.isEmpty(delimiter)) {
                delimiter = STR_SPACE;
            }
            sBuilder = new StringBuilder();
            for (int i = 0; i < elements.length; i++) {
                sBuilder.append(elements[i]);
                if (i < (elements.length - 1)) {
                    sBuilder.append(delimiter);
                }
            }
        }

        return (sBuilder == null ? null : sBuilder.toString());
    }

    /**
     * Returns the string created by joining the specified elements using the default space delimiter. if the passed
     * elements array is null, return null;
     *
     * @param elements
     * @return
     */
    public static String join(String[] elements) {
        return join(elements, null);
    }

    /**
     * Returns the elements of string separated by using the specified delimiter. if delimiter is null or empty, space
     * is used as default delimiter
     *
     * @param str
     * @return
     */
    public static String[] split(String str, String delimiter) {
        String[] elements = null;
        if (BeanUtils.isNotEmpty(str)) {
            if (BeanUtils.isEmpty(delimiter)) {
                delimiter = STR_SPACE;
            }
            StringTokenizer sTokenizer = new StringTokenizer(str, delimiter);
            elements = new String[sTokenizer.countTokens()];
            for (int i = 0; sTokenizer.hasMoreTokens(); i++) {
                elements[i] = sTokenizer.nextToken();
            }
        }
        return elements;
    }

    /**
     * Converts a string into an array of strings.
     *
     * @param str
     * @return returns an array of String
     */
    public static String[] split(String str, char delimiter) {
        String[] words = null;
        if (BeanUtils.isNotEmpty(str)) {
            List<String> segments = new LinkedList<String>();
            char[] chars = str.toCharArray();
            String newStr = null;
            int startIdx = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == delimiter) {
                    /* ignore extra spaces/delimiter characters */
                    if (i == startIdx) {
                        startIdx++;
                        continue;
                    }
                    newStr = new String(chars, startIdx, (i - startIdx));
                    segments.add(newStr);
                    startIdx = i + 1;
                }
            }
            newStr = new String(chars, startIdx, (chars.length - startIdx));
            segments.add(newStr);
            words = toString(segments);
        }
        return words;
    }

    /**
     * Returns the elements of string separated by default delimiter (space).
     *
     * @param str
     * @return
     */
    public static String[] split(String str) {
        return split(str, " ");
    }

    /**
     * Returns the string after replacing \\(double back-slashes) with single back-slash and //(double slashes) with
     * single slash.
     *
     * @param str
     * @return
     */
    public static String trimDoubleSlashes(String str) {
        if (str != null) {
            while (str.contains("//")) {
                str = str.replaceAll("//", "/");
            }
            while (str.contains("\\\\")) {
                str = str.replace("\\\\", "\\");
            }
        }
        return str;
    }

    /**
     * Prints elements of string at output stream.
     *
     * @param elements
     */
    public static void printLine(String... elements) {
        if (elements != null && elements.length > 0) {
            for (String str : elements) {
                LOGGER.info(str);
            }
        }
    }

    /**
     * Returns a <code>String</code> object that is filled with the number of specified character. if <code>times <=
     * 0</code>, an empty
     * <code>String</code> object is returned.
     *
     * @param times
     * @return
     */
    public static String fill(char cChar, int times) {
        StringBuilder indentType = new StringBuilder();
        if (times > 0) {
            for (int i = 0; i < times; i++) {
                indentType.append(cChar);
            }
        }

        return indentType.toString();
    }

    /**
     * Returns a <code>String</code> object that is filled with the number of spaces. if <code>times <= 0</code>, an
     * empty <code>String</code> object is returned.
     *
     * @param times
     * @return
     */
    public static String fill(int times) {
        return fill(SPACE_SEPARATOR, times);
    }

    /**
     * @param str
     * @return
     */
    public static long countWords(String str) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("+countWords(" + str + ")");
        }

        if (BeanUtils.isEmpty(str)) {
            throw new NullPointerException("Invalid Parameter!, str: " + str);
        }

        long words = 0;
        int index = 0;
        boolean whiteSpace = true;
        while (index < str.length()) {
            char cChar = str.charAt(index++);
            // LOGGER.debug("cChar:" + cChar);
            boolean isWhitespace = Character.isWhitespace(cChar);
            if (whiteSpace && !isWhitespace) {
                words++;
            }
            whiteSpace = isWhitespace;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-countWords(), words: " + words);
        }
        return words;
    }

    /* optimize the below given method. */

    /**
     * Returns the unique/duplicate set of string elements.
     *
     * @param elements
     * @param ignoreCase
     * @param unique
     * @return
     */
    public static Set<String> filter(String[] elements, boolean ignoreCase, boolean unique) {
        Set<String> uniques = null;
        Set<String> duplicates = null;
        if (elements != null) {
            uniques = new LinkedHashSet<String>();
            duplicates = new LinkedHashSet<String>();
            for (int i = 0; i < elements.length; i++) {
                String str = elements[i];
                /* remove if the word ends with non-letter character. */
                char cChar = str.charAt(str.length() - 1);
                if (!Character.isLetter(cChar)) {
                    str = str.substring(0, elements[i].length() - 1);
                }

                if (ignoreCase) {
                    str = str.toLowerCase();
                }

                if (!uniques.add(str)) {
                    duplicates.add(str);
                }
                // if (log.isDebugEnabled()) {
                // log.debug("uniques: " + uniques);
                // log.debug("duplicates: " + duplicates);
                // }
            }
        }
        return (unique ? uniques : duplicates);
    }

    /**
     * Returns the string representation of the specified <code>object</code>.
     *
     * @param object
     * @param excludePackageName
     * @param includeClassName
     * @return
     */
    public static String toString(Object object, boolean excludePackageName, boolean includeClassName) {
        return INSTANCE.newToString(excludePackageName, includeClassName).toString(object);
    }

    /**
     * Returns the string representation of the specified <code>object</code>.
     *
     * @param object
     * @param includeClassName
     * @return
     */
    public static String toString(Object object, boolean includeClassName) {
        return INSTANCE.newToString(includeClassName).toString(object);
    }

    /**
     * Returns the string representation of the specified <code>object</code>.
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return INSTANCE.newToString().toString(object);
    }

    /**
     * Converts an array of objects into an array of strings.
     *
     * @param elements
     * @return
     */
    public static String[] toStringArray(Object... elements) {
        String[] strElements = null;
        if (elements != null) {
            strElements = new String[elements.length];
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] instanceof String) {
                    strElements[i] = elements[i].toString();
                }
            }
        }
        return strElements;
    }

    /**
     * Converts the collection into an array of strings.
     *
     * @param elements
     * @return
     */
    public static String[] toString(Collection<String> elements) {
        String[] strElements = null;
        if (elements != null) {
            strElements = new String[elements.size()];
            Object[] oElements = elements.toArray();
            for (int i = 0; i < oElements.length; i++) {
                strElements[i] = oElements[i].toString();
            }
        }
        return strElements;
    }

    /**
     * Returns the string of unique words. if the ignoreCase is true, the word is considered unique, if any of the
     * letter of the word in different case.
     *
     * @param elements
     * @param ignoreCase
     * @return
     */
    public static String findUnique(String[] elements, boolean ignoreCase) {
        Set<String> uniques = filter(elements, ignoreCase, true);
        return join(toString(uniques));
    }

    /**
     * Returns the string of unique words without ignoring by case.
     * <p>
     * Find Unique Words in a String
     *
     * @param elements
     * @return
     */
    public static String findUnique(String[] elements) {
        return findUnique(elements, false);
    }

    /**
     * Returns the string of duplicate words. if the ignoreCase is true, the word is considered unique, if any of the
     * letter of the word in different case.
     *
     * @param elements
     * @param ignoreCase
     * @return
     */
    public static String findDuplicate(String[] elements, boolean ignoreCase) {
        Set<String> duplicates = filter(elements, ignoreCase, false);
        return join(toString(duplicates));
    }

    /**
     * Returns the string of duplicate words.
     *
     * @param elements
     * @return
     */
    public static String findDuplicate(String[] elements) {
        return findDuplicate(elements, false);
    }

    /**
     * Converts the specified string into boolean value. if the specified string is null or empty, it returns false.
     *
     * @param value
     * @return
     */
    public static boolean toBoolean(String value) {
        return (BeanUtils.isNotEmpty(value) && Boolean.valueOf(value.trim()).booleanValue());
    }

    /**
     * Converts the specified string into integer value. if the specified string is null or empty, it returns -1.
     *
     * @param value
     * @return
     */
    public static int toInteger(String value) {
        int result = -1;
        if (BeanUtils.isNotEmpty(value)) {
            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Error parsing value: " + value, nfe);
            }
        }
        return result;
    }

    /**
     * Converts the specified string into long value. if the specified string is null or empty, it returns -1.
     *
     * @param value
     * @return
     */
    public static long toLong(String value) {
        long result = -1L;
        if (BeanUtils.isNotEmpty(value)) {
            try {
                result = Long.parseLong(value);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Error parsing value: " + value, nfe);
            }
        }
        return result;
    }

    /**
     * Tests the number of words in the string provided by user.
     *
     * @param active
     */
    public static void printWords(int number, boolean active) {
        if (active) {
            int num = 0;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a Number : ");
            try {
                num = Integer.parseInt(in.readLine());
            } catch (IOException ioe) {
                System.out.println("Exception : " + ioe.toString());
            }
            if (num != -1) {
                number = num;
            }
        }
        System.out.println("Num : " + number);
        System.out.println("Size : " + numbersToWords(number));
    }

    /**
     * Converts a number less than 100 to it's equivalent word(s)
     *
     * @param num
     * @return
     */
    public static String onesConverter(int num) {
        System.out.println("onesConverter Num : " + num);
        String str = " ";
        if (num < 10) {
            str = UNITS[num];
        } else if (num < 20) {
            str = TENS[num % 10];
        } else if (num < 100) {
            System.out.println("Hundreds[num] : " + HUNDREDS[num / 10] + " " + UNITS[num % 10]);
            str = HUNDREDS[num / 10] + " " + UNITS[num % 10];
        }
        System.out.println("onesConverter str : " + str);
        return str;
    }

    /**
     * Converts a number less than 1000 to it's equivalent word(s)
     *
     * @param num
     * @return
     */
    public static String twosConverter(int num) {
        System.out.println("twosConverter Num : " + num);
        String str = " ";
        System.out.println("(num / 100) : " + (num / 100));
        if ((num / 100) > 0) {
            str += onesConverter(num / 100) + " Hundred ";
        }
        System.out.println("(num % 100) : " + (num % 100));
        str += onesConverter(num % 100);
        System.out.println("twosConverter str : " + str);
        return str;
    }

    /**
     * Converts a number less than 10000 to it's equivalent word(s)
     *
     * @param num
     * @return
     */
    public static String threesConverter(int num) {
        System.out.println("threesConverter Num : " + num);
        String str = " ";
        System.out.println("(num / 1000) : " + (num / 1000));
        if ((num / 1000) > 0) {
            str += twosConverter(num / 1000) + " " + PREFIXES[num / 1000];
            System.out.println("threesConverter str : " + str);
        }
        System.out.println("(num % 1000) : " + (num % 1000));
        str += twosConverter(num % 1000);
        System.out.println("threesConverter str : " + str);
        return str;
    }

    public static String numbersToWords(int num) {
        System.out.println("numerToWord Num : " + num);
        String str = "";
        int ctr = 0;
        if (num > 0) {
            System.out.println("numerToWord (num % 1000) : " + (num % 1000));
            while (ctr < PREFIXES.length && num > 0) {
                str += PREFIXES[ctr] + threesConverter(num % 1000) + PREFIXES[ctr];
                System.out.println("numerToWord str : " + str);
                System.out.println("numerToWord Before Dividing Num : " + num);
                num = num / 1000;
                System.out.println("numerToWord After Dividing Num : " + num);
                ctr++;
            }
        }
        return str;
    }

    /**
     * Returns the reversed string using recursion.
     *
     * @param str
     * @return
     */
    public String reverse(String str) {
        if (null == str || str.isEmpty()) {
            return "";
        }
        return reverse(str.substring(1)) + str.substring(0, 1);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String digits = "2";
        System.out.println(digits + " is digits=" + isDigits(digits));
        System.out.println(digits + " is digits (lamda)=" + isDigits(digits, true));
        digits = "1234a";
        System.out.println(digits + " is digits=" + isDigits(digits));
        System.out.println(digits + " is digits (lamda)=" + isDigits(digits, true));
        digits = "1234$";
        System.out.println(digits + " is digits=" + isDigits(digits));
        System.out.println(digits + " is digits (lamda)=" + isDigits(digits, true));
        digits = "1234!";
        System.out.println(digits + " is digits=" + isDigits(digits));
        System.out.println(digits + " is digits (lamda)=" + isDigits(digits, true));
        digits = "0123.4";
        System.out.println(digits + " is digits=" + isDigits(digits));
        System.out.println(digits + " is digits (lamda)=" + isDigits(digits, true));
    }

    /**
     * Returns the ToString object.
     *
     * @return
     */
    public ToString newToString() {
        return new ToString();
    }

    /**
     * Returns the ToString object.
     *
     * @return
     */
    public ToString newToString(boolean excludePackageName, boolean includeClassName) {
        return new ToString(excludePackageName, includeClassName);
    }

    /**
     * Returns the ToString object.
     *
     * @return
     */
    public ToString newToString(boolean includeClassName) {
        return new ToString(includeClassName);
    }

    /**
     * ToString
     *
     * @version 1.0.0
     * @created 2012-04-20 07:30:07 PM
     * @created 1.0.0
     */
    private final class ToString {

        /**
         * excludePackageName
         */
        private boolean excludePackageName;

        /**
         * includeClassName
         */
        private boolean includeClassName;

        /**
         * Parameterized Constructor.
         *
         * @param excludePackageName
         * @param includeClassName
         */
        public ToString(boolean excludePackageName, boolean includeClassName) {
            this.excludePackageName = excludePackageName;
            this.includeClassName = includeClassName;
        }

        /**
         * Parameterized Constructor.
         *
         * @param includeClassName
         */
        public ToString(boolean includeClassName) {
            this(false, includeClassName);
        }

        /**
         * Default Constructor.
         */
        public ToString() {
            this(false, false);
        }

        /**
         * Returns the string representation of the specified
         * <code>object</code> including all fields.
         *
         * @param object
         * @return
         */
        public String toString(Object object) {
            if (object == null) {
                return TextUtils.NULL;
            }

            Class<?> objectClass = object.getClass();
            if (objectClass == String.class) {
                return (String) object;
            } else if (objectClass.isArray()) {
                String classArrayType = (includeClassName ? objectClass.getComponentType().toString() : "") + "[]{";
                if (excludePackageName) {
                    int index = classArrayType.lastIndexOf(".");
                    if (index > 0 && index < classArrayType.length() - 1) {
                        classArrayType = classArrayType.substring(index + 1);
                    }
                }

                for (int i = 0; i < Array.getLength(object); i++) {
                    if (i > 0) {
                        classArrayType += ", ";
                    }

                    Object objectArray = Array.get(object, i);
                    if (objectClass.getComponentType().isPrimitive()) {
                        classArrayType += objectArray;
                    } else {
                        // recursion call.
                        classArrayType += toString(objectArray);
                    }
                }

                return classArrayType + "}";
            }

            String className = (excludePackageName ? objectClass.getSimpleName() : objectClass.getName());
            /*
             * Check this class fields as well as supper classes, if any.
             */
            do {
                if (includeClassName) {
                    className += "[";
                } else {
                    className = "[";
                }

                Field[] classFields = objectClass.getDeclaredFields();
                AccessibleObject.setAccessible(classFields, true);
                // get the names and values of all fields
                for (Field field : classFields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        if (!className.endsWith("[")) {
                            className += ", ";
                        }
                        className += field.getName() + "=";
                        try {
                            Class<?> classType = field.getType();
                            Object value = field.get(object);
                            if (classType.isPrimitive()) {
                                className += value;
                            } else {
                                // recursion call.
                                className += toString(value);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                className += "]";
                objectClass = objectClass.getSuperclass();
            } while (objectClass != null && !objectClass.isInstance(Object.class));

            return className;
        }
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static Integer asInteger(String text, String errorMessage) {
        Integer value = null;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new InvalidValueException(errorMessage);
        }

        return value;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static Double asDouble(String text, String errorMessage) {
        Double value = null;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(errorMessage);
        }

        return value;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static Double asDoubleLatitude(String text, String errorMessage) {
        Double latitude = asDouble(text, errorMessage);
        if ((latitude < -90) || (latitude > 90)) {
            throw new InvalidValueException(errorMessage);
        }

        return latitude;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static Double asDoubleLongitude(String text, String errorMessage) {
        Double longitude = asDouble(text, errorMessage);
        if ((longitude < -180) || (longitude > 180)) {
            throw new InvalidValueException(errorMessage);
        }

        return longitude;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static BigDecimal asBigDecimal(String text, String errorMessage) {
        BigDecimal value = null;
        try {
            value = new BigDecimal(text);
        } catch (NumberFormatException ex) {
            throw new InvalidValueException(errorMessage);
        }

        return value;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static BigDecimal asBigDecimalLatitude(String text, String errorMessage) {
        BigDecimal latitude = asBigDecimal(text, errorMessage);
        if (latitude != null && ((latitude.doubleValue() < -90) || (latitude.doubleValue() > 90))) {
            throw new InvalidValueException(errorMessage);
        }

        return latitude;
    }

    /**
     * @param text
     * @param errorMessage
     * @return
     * @throws InvalidValueException
     */
    public static BigDecimal asBigDecimalLongitude(String text, String errorMessage) {
        BigDecimal longitude = asBigDecimal(text, errorMessage);
        if (longitude != null && ((longitude.doubleValue() < -180) || (longitude.doubleValue() > 180))) {
            throw new InvalidValueException(errorMessage);
        }

        return longitude;
    }

}
