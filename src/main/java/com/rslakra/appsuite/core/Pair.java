package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 4/7/21 5:17 PM
 */
public final class Pair<K, V> implements Map.Entry<K, V>, Comparable<Pair<K, V>>, Serializable {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(Pair.class);
    public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair[0];
    public static final Pair NULL = of((Object) null, (Object) null);
    public static final String DELIMITER = "=";
    private final K key;
    private final V value;

    /**
     * Default constructor.
     *
     * @param key
     * @param value
     */
    private Pair(final K key, final V value) {
        LOGGER.debug("Pair({}, {})", key, value);
        this.key = key;
        this.value = value;
    }

    /**
     * Sets the value of this <code>Pair<K, V></code> object.
     *
     * @param value new value to be stored in this entry
     * @return
     * @throws UnsupportedOperationException if the <code>set</code> operation is not supported by this <code>Pair<K, V></code> object.
     */
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the key of this <code>Pair<K, V></code> object.
     *
     * @return
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Returns the value of this <code>Pair<K, V></code> object.
     *
     * @return
     */
    public V getValue() {
        return this.value;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     *
     * @param other the object to be compared.
     * @return
     */
    @Override
    public int compareTo(final Pair<K, V> other) {
        int result = ((Comparable) this.getKey()).compareTo(other.getKey());
        // check the value of the pair if the key is the same.
        if (result == 0) {
            result = ((Comparable) this.getValue()).compareTo(other.getValue());
        }

        return result;
    }

    /**
     * @param object
     * @return
     */
    @Override
    public boolean equals(final Object object) {
        if (Objects.isNull(object)) {
            return true;
        } else if (Objects.isNull(object) || !(object instanceof Map.Entry)) {
            return false;
        } else {
            Map.Entry<?, ?> other = (Map.Entry) object;
            return Objects.equals(getKey(), other.getKey()) && Objects.equals(getValue(), other.getValue());
        }
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        return (Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue()));
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return toString("(%s, %s)");
    }

    /**
     * Returns the <code>Pair</code> object formatted with the provided pattern.
     *
     * @param format
     * @return
     */
    public String toString(String format) {
        return String.format(format, this.getKey(), this.getValue());
    }

    /**
     * Returns the <code>Pair<K, V></code> pair.
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair(key, value);
    }

    /**
     * Returns the <code>Pair<K, V></code> pair.
     *
     * @param pair
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V> of(Map.Entry<K, V> pair) {
        return (pair == null ? NULL : of(pair.getKey(), pair.getValue()));
    }

    /**
     * Returns the empty <code>Pair<K, V></code> object.
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V>[] emptyArray() {
        return (Pair[]) EMPTY_ARRAY;
    }

    /**
     * Extracts the key and value from the specified <code>keyValuePair</code> based on the specified delimiter,
     * if <code>keyValuePair</code> not null or empty otherwise null.
     *
     * @param keyValuePair
     * @param delimiter
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V> ofString(String keyValuePair, String delimiter) {
        LOGGER.debug("+ofString({}, {})", keyValuePair, delimiter);
        Pair<K, V> pair = NULL;
        if (BeanUtils.isNotEmpty(keyValuePair)) {
            if (BeanUtils.isEmpty(delimiter)) {
                delimiter = DELIMITER;
            }

            int equalIndex = keyValuePair.indexOf(delimiter);
            LOGGER.debug("equalIndex={}", equalIndex);
            if (equalIndex != -1) {
                String key = keyValuePair.substring(0, equalIndex).trim();
                int lastIndex = keyValuePair.indexOf(";");
                LOGGER.debug("lastIndex={}", lastIndex);
                String value = null;
                if (lastIndex != -1 && lastIndex < keyValuePair.length()) {
                    value = keyValuePair.substring(equalIndex + 1, lastIndex).trim();
                } else {
                    value = keyValuePair.substring(equalIndex + 1).trim();
                }
                LOGGER.debug("value={}", value);
                if (NumberUtils.isBoolean(value)) {
                    pair = Pair.of((K) key, (V) Boolean.valueOf(value));
                } else if (NumberUtils.isNumber(value)) {
                    pair = Pair.of((K) key, (V) Integer.valueOf(value));
                } else {
                    if (value.charAt(0) == '[') {
                        String[] tokens = value.split(",");
                        pair = Pair.of((K) key, (V) Arrays.asList(tokens));
                    } else {
                        pair = Pair.of((K) key, (V) value);
                    }
                }
            }
        }

        LOGGER.debug("-ofString(), pair={}", pair);
        return pair;
    }

    /**
     * Extracts the key and value from the specified <code>keyValuePair</code> based on the specified delimiter,
     * if <code>keyValuePair</code> not null or empty otherwise null.
     *
     * @param keyValuePair
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V> ofString(String keyValuePair) {
        return ofString(keyValuePair, DELIMITER);
    }

}
