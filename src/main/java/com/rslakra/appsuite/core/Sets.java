package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Rohtash Lakra
 * @created 3/19/20 1:35 PM
 */
public enum Sets {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(Sets.class);

    /**
     * Returns the set of
     *
     * @param values
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... values) {
        return INSTANCE.newArraySet(values);
    }

    /**
     * @param values
     * @param <T>
     * @return
     */
    public final <T> Set<T> newArraySet(T... values) {
        return new ArraySet(values);
    }

    /**
     * @param <E>
     */
    private final class ArraySet<E> extends AbstractSet<E> implements Serializable {

        // unique set of values.
        private final Set<E> values;

        /**
         * @param values
         */
        private ArraySet(final E[] values) {
            LOGGER.debug("ArraySet({})", Arrays.toString(values));
            this.values = new HashSet<>(Arrays.asList(values));
        }

        /**
         * @return
         */
        @Override
        public Iterator iterator() {
            return values.iterator();
        }

        /**
         * Returns the size of this array.
         *
         * @return
         */
        @Override
        public int size() {
            return this.values.size();
        }
    }

    /**
     * @param items
     * @param <T>
     * @return
     */
    public static <T> Set<T> asSet(Collection<T> items) {
        return new HashSet<>(items);
    }

}
