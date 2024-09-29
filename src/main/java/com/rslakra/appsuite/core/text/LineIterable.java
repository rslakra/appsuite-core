package com.rslakra.appsuite.core.text;

import java.util.Iterator;

/**
 * @author Rohtash Lakra
 * @created 1/21/21 10:51 AM
 */
public class LineIterable implements Iterable<Character> {

    private final CharSequence textContent;

    public LineIterable(final CharSequence textContent) {
        this.textContent = textContent;
    }

    /**
     * @return
     */
    public Iterator<Character> iterator() {
        return new CharIterator(textContent);
    }
}
