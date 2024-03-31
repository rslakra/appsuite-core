package com.devamatre.appsuite.core;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created 12/13/21 5:41 PM
 */
public final class ToString {

    public static final String SPACE = " ";
    public static final String EMPTY_STR = "";
    public static final String DELIMITER = ", ";
    public static final String PREFIX = "<";
    public static final String SUFFIX = ">";
    public static final String SEPARATOR = "=";

    private final String delimiter;
    private final String prefix;
    private final String suffix;

    /**
     * Contains all the string component added so far.
     */
    private String[] elements;

    /**
     * The number of string component added so far.
     */
    private int size;

    /**
     * Total length in chars so far, excluding prefix and suffix.
     */
    private int length;

    /**
     * When overridden by the user to be non-null via {setEmptyValue}, the string returned by toString() when no
     * elements have yet been added. When null, prefix + suffix is used as the empty value.
     */
    private String emptyValue;

    /**
     * Constructs a {@code ToString} with no characters in it using copies of the supplied {@code prefix},
     * {@code delimiter} and {@code suffix}. If no characters are added to the {@code ToString} and methods accessing
     * the string value of it are invoked, it will return the {@code prefix + suffix} (or properties thereof) in the
     * result, unless {@code setEmptyValue} has first been called.
     *
     * @param delimiter the sequence of characters to be used between each element added to the {@code ToString}
     * @param prefix    the sequence of characters to be used at the beginning
     * @param suffix    the sequence of characters to be used at the end
     */
    public ToString(final CharSequence delimiter, final CharSequence prefix, final CharSequence suffix) {
        // make defensive copies of arguments
        Objects.requireNonNull(delimiter, "The delimiter must not be null!");
        Objects.requireNonNull(prefix, "The prefix must not be null!");
        Objects.requireNonNull(suffix, "The suffix must not be null!");
        this.delimiter = delimiter.toString();
        this.prefix = prefix.toString();
        this.suffix = suffix.toString();
    }

    /**
     * ToString Constructor.
     *
     * @param delimiter
     */
    public ToString(final CharSequence delimiter) {
        this(delimiter, EMPTY_STR, EMPTY_STR);
    }

    /**
     * Copies characters from <code>self</code> string into the destination <code>chars</code> array.
     *
     * @param self
     * @param chars
     * @param startOffset
     * @return
     */
    private int fillChars(String self, char[] chars, int startOffset) {
        int length = 0;
        if (Objects.nonNull(self)) {
            length = self.length();
            self.getChars(0, length, chars, startOffset);
        }

        return length;
    }

    /**
     * @param classType
     * @param excludeClassName
     * @param excludePackageName
     * @param delimiter
     * @param prefix
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, boolean excludeClassName, boolean excludePackageName,
                                  CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        if (Objects.isNull(classType)) {
            return new ToString(delimiter, prefix, suffix);
        } else {
            if (excludeClassName) {
                return new ToString(delimiter, prefix, suffix);
            } else if (excludePackageName) {
                return new ToString(delimiter, classType.getSimpleName() + SPACE + prefix, suffix);
            } else {
                return new ToString(delimiter, classType.getName() + SPACE + prefix, suffix);
            }
        }
    }

    /**
     * @param classType
     * @param excludePackageName
     * @param delimiter
     * @param prefix
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, boolean excludePackageName, CharSequence delimiter,
                                  CharSequence prefix, CharSequence suffix) {
        return of(classType, Objects.isNull(classType), excludePackageName, delimiter, prefix, suffix);
    }

    /**
     * @param classType
     * @param delimiter
     * @param prefix
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, CharSequence delimiter, CharSequence prefix,
                                  CharSequence suffix) {
        return of(classType, classType == null, delimiter, prefix, suffix);
    }

    /**
     * @param classType
     * @param excludePackageName
     * @param delimiter
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, boolean excludePackageName, CharSequence delimiter) {
        return of(classType, excludePackageName, delimiter, PREFIX, SUFFIX);
    }

    /**
     * @param classType
     * @param excludePackageName
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, boolean excludePackageName) {
        return of(classType, excludePackageName, DELIMITER, PREFIX, SUFFIX);
    }

    /**
     * @param classType
     * @param prefix
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, CharSequence prefix, CharSequence suffix) {
        return of(classType, DELIMITER, prefix, suffix);
    }

    /**
     * @param classType
     * @param delimiter
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType, CharSequence delimiter) {
        return of(classType, delimiter, PREFIX, SUFFIX);
    }

    /**
     * @param delimiter
     * @param prefix
     * @param suffix
     * @param <T>
     * @return
     */
    public static <T> ToString of(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return of(null, delimiter, prefix, suffix);
    }

    /**
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> ToString of(Class<T> classType) {
        return of(classType, DELIMITER);
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ToString of() {
        return of(null);
    }

    /**
     * Sets the sequence of characters to be used when determining the string representation of this {@code ToString}
     * and no elements have been added yet, that is, when it is empty.  A copy of the {@code emptyValue} parameter is
     * made for this purpose. Note that once an add method has been called, the {@code ToString} is no longer considered
     * empty, even if the element(s) added correspond to the empty {@code String}.
     *
     * @param emptyValue the characters to return as the value of an empty {@code ToString}
     * @return this {@code ToString} itself so the calls may be chained
     * @throws NullPointerException when the {@code emptyValue} parameter is {@code null}
     */
    public ToString setEmptyValue(final CharSequence emptyValue) {
        this.emptyValue = Objects.requireNonNull(emptyValue, "The empty value must not be null").toString();
        return this;
    }

    /**
     * Returns the current value, consisting of the {@code prefix}, the values added so far separated by the
     * {@code delimiter}, and the {@code suffix}, unless no elements have been added in which case, the
     * {@code prefix + suffix} or the {@code emptyValue} characters are returned.
     *
     * @return the string representation of this {@code ToString}
     */
    @Override
    public String toString() {
        final String[] elements = this.elements;
        if (elements == null && emptyValue != null) {
            return emptyValue;
        }
        final int size = this.size;
        final int addLength = prefix.length() + suffix.length();
        if (addLength == 0) {
            compactElements();
            return size == 0 ? EMPTY_STR : elements[0];
        }

        final String delimiter = this.delimiter;
        final char[] allElements = new char[length + addLength];
        int charIndex = fillChars(prefix, allElements, 0);
        if (size > 0) {
            charIndex += fillChars(elements[0], allElements, charIndex);
            for (int i = 1; i < size; i++) {
                charIndex += fillChars(delimiter, allElements, charIndex);
                charIndex += fillChars(elements[i], allElements, charIndex);
            }
        }

        charIndex += fillChars(suffix, allElements, charIndex);
        return new String(allElements);
    }

    /**
     * Adds the <code>newValue</code> into the string object.
     *
     * @param newValue
     * @return
     */
    public ToString add(CharSequence newValue) {
        final String element = String.valueOf(newValue);
        if (this.elements == null) {
            this.elements = new String[8];
        } else {
            if (size == this.elements.length) {
                this.elements = Arrays.copyOf(this.elements, 2 * size);
            }
            length += this.delimiter.length();
        }
        length += element.length();
        this.elements[size++] = element;
        return this;
    }

    /**
     * Adds the <code>key</code> and <code>value</code> to the string object.
     *
     * @param key
     * @return
     */
    public ToString add(CharSequence key, Object value) {
        if (Objects.nonNull(key) && Objects.nonNull(value)) {
            return add(key + SEPARATOR + value);
        } else if (Objects.isNull(key) && Objects.nonNull(value)) {
            return add(Objects.toString(value));
        } else {
            return add(key);
        }
    }

    /**
     * Adds the contents of the given {@code ToString} without prefix and suffix as the next element if it is non-empty.
     * If the given {@code ToString} is empty, the call has no effect.
     *
     * <p>A {@code ToString} is empty if {@link #add(CharSequence) add()}
     * has never been called, and if {@code merge()} has never been called with a non-empty {@code ToString} argument.
     *
     * <p>If the other {@code ToString} is using a different delimiter,
     * then elements from the other {@code ToString} are concatenated with that delimiter and the result is appended to
     * this {@code ToString} as a single element.
     *
     * @param other The {@code ToString} whose contents should be merged into this one
     * @return This {@code ToString}
     * @throws NullPointerException if the other {@code ToString} is null
     */
    public ToString merge(final ToString other) {
        Objects.requireNonNull(other);
        if (Objects.isNull(other.elements)) {
            return this;
        }
        other.compactElements();
        return add(other.elements[0]);
    }

    /**
     * Compacts elements.
     */
    private void compactElements() {
        if (size > 1) {
            final char[] chars = new char[length];
            int index = 0;
            int startIndex = 0;
            while (index < size) {
                if (startIndex > 0) {
                    startIndex += fillChars(delimiter, chars, startIndex);
                }
                startIndex += fillChars(elements[index], chars, startIndex);
                elements[index] = null;
                index++;
            }

            size = 1;
            elements[0] = new String(chars);
        }
    }

    /**
     * Returns the length of the {@code String} representation of this {@code ToString}. Note that if no add methods
     * have been called, then the length of the {@code String} representation (either {@code prefix + suffix} or
     * {@code emptyValue}) will be returned. The value should be equivalent to {@code toString().length()}.
     *
     * @return the length of the current value of {@code ToString}
     */
    public int length() {
        return (size == 0 && Objects.nonNull(emptyValue)) ? emptyValue.length()
                                                          : length + prefix.length() + suffix.length();
    }
}
