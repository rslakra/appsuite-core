package com.rslakra.appsuite.core;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Rohtash Lakra
 * @created 4/13/20 6:00 PM
 */
public final class ArrayIterator<T> implements Iterator {

    private T[] data;
    private int startIndex = 0;
    private int index = 0;
    private int endIndex = 0;

    /**
     * @param data
     */
    public ArrayIterator(final T[] data) {
        this(data, 0);
    }

    /**
     * @param data
     * @param startIndex
     */
    public ArrayIterator(final T[] data, final int startIndex) {
        this(data, startIndex, Array.getLength(data));
    }

    /**
     * @param data
     * @param startIndex
     * @param endIndex
     */
    public ArrayIterator(final T[] data, final int startIndex, final int endIndex) {
        this.setData(data);
        checkStartIndex(startIndex);
        checkEndIndex(endIndex);
        if (endIndex < startIndex) {
            throw new IllegalArgumentException("End index must not be less than start index.");
        } else {
            this.startIndex = startIndex;
            this.index = startIndex;
            this.endIndex = endIndex;
        }
    }

    /**
     * @param index
     */
    private void checkStartIndex(final int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Invalid ArrayIterator index:" + index);
        }
    }

    /**
     * @param index
     */
    private void checkEndIndex(final int index) {
        if (index > this.endIndex) {
            throw new ArrayIndexOutOfBoundsException("Invalid ArrayIterator index:" + index);
        }
    }

    /**
     * @return
     */
    public boolean hasNext() {
        return (this.index < this.endIndex);
    }

    /**
     * @return
     */
    public Object next() {
        if (this.hasNext()) {
            return Array.get(this.data, this.index++);
        }

        throw new NoSuchElementException();
    }

    /**
     *
     */
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }

    /**
     * @return
     */
    public T[] getData() {
        return this.data;
    }

    /**
     * @param data
     */
    public void setData(final T[] data) {
        this.data = data;
        this.endIndex = Array.getLength(data);
        this.startIndex = 0;
        this.index = 0;
    }

    /**
     *
     */
    public void reset() {
        this.index = this.startIndex;
    }

    /**
     * ToString
     *
     * @return
     */
    @Override
    public String toString() {
        final ToString strBuilder = ToString.of(ToString.DELIMITER, "[", "]");
        while (hasNext()) {
            strBuilder.add(next().toString());
        }
        return strBuilder.toString();
    }
}
