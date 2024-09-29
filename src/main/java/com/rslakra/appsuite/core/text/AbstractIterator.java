package com.rslakra.appsuite.core.text;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 20:17:50
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

    private final CharSequence text;
    private int index;

    /**
     * @param text
     */
    public AbstractIterator(final CharSequence text) {
        this.text = text;
        index = 0;
    }

    /**
     * @return
     */
    protected final Character nextValue() {
        return Character.valueOf(text.charAt(index++));
    }

    /**
     * @return
     */
    @Override
    public boolean hasNext() {
        return (index < text.length());
    }

    /**
     *
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }

}
