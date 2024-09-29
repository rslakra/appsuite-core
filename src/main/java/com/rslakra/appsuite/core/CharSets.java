package com.rslakra.appsuite.core;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Standard charsets
 *
 * @author Rohtash Lakra
 * @created 7/21/22 4:08 PM
 */
public enum CharSets {
    /**
     * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     */
    ASCII(Charset.forName("ASCII")),
    /**
     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     */
    ISO_8859_1(Charset.forName("ISO-8859-1")),
    /**
     * Eight-bit UCS Transformation Format
     */
    UTF_8(Charset.forName("UTF-8")),
    /**
     * Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
     */
    UTF_16(Charset.forName("UTF-16"));

    private final Charset charset;

    /**
     * @param charset
     */
    private CharSets(final Charset charset) {
        this.charset = charset;
    }

    /**
     * @return
     */
    public String toCharset() {
        return charset.name();
    }

    /**
     * @param charset
     * @return
     */
    public static CharSets ofString(final String charset) {
        CharSets charSets = UTF_8;
        if (BeanUtils.isNotEmpty(charset)) {
            charSets =
                Arrays.stream(values())
                    .filter(charSet -> charSet.toCharset().equalsIgnoreCase(charset))
                    .findAny()
                    .orElse(UTF_8);
        }

        return charSets;
    }
}
