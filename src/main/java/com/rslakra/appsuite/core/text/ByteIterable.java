package com.rslakra.appsuite.core.text;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 19:15:15
 */
public class ByteIterable implements Iterable<Byte> {

    private final CharSequence textContent;

    /**
     * @param textContent
     */
    public ByteIterable(final CharSequence textContent) {
        this.textContent = textContent;
    }

    /**
     * @return
     */
    public Iterator<Byte> iterator() {
        return new ByteIterator(textContent);
    }
}
