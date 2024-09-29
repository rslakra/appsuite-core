package com.rslakra.appsuite.core.text;

import java.util.NoSuchElementException;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 19:21:36
 */
public final class CharIterator extends AbstractIterator<Character> {

    /**
     * @param text
     */
    public CharIterator(final CharSequence text) {
        super(text);
    }

    /**
     * @return
     */
    @Override
    public Character next() {
        if (this.hasNext()) {
            return Character.valueOf((char) nextValue());
        } else {
            throw new NoSuchElementException();
        }
    }
}
