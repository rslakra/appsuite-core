package com.rslakra.appsuite.core.text;

import java.util.NoSuchElementException;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 19:23:46
 */
public final class ByteIterator extends AbstractIterator<Byte> {

    /**
     * @param text
     */
    public ByteIterator(final CharSequence text) {
        super(text);
    }

    /**
     * @return
     */
    @Override
    public Byte next() {
        if (this.hasNext()) {
            return Integer.valueOf(nextValue()).byteValue();
        } else {
            throw new NoSuchElementException();
        }
    }

}
