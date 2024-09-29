package com.rslakra.appsuite.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rohtash Lakra
 * @created 3/25/20 8:12 PM
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public final class Range<T> {

    private final T from;
    private final T to;
    private final boolean reverse;

    /**
     * Uses custom builder class
     */
    public static <T> RangeBuilder builder() {
        return new CustomRangeBuilder<T>();
    }

    /**
     * Customer builder to override <code>build</code> method.
     */
    private static class CustomRangeBuilder<T> extends RangeBuilder<T> {

        /**
         * Overrides the build method.
         *
         * @return
         */
        @Override
        public Range build() {
            Range range = super.build();
            if (range.isReverse()) {
                return new Range<T>((T) range.getTo(), (T) range.getFrom(), range.isReverse());
            } else {
                return new Range<T>((T) range.getFrom(), (T) range.getTo(), range.isReverse());
            }
        }
    }

    /**
     * ToString
     *
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(Range.class, "[", "]")
            .add("from", from)
            .add("to", to)
            .add("reverse", reverse)
            .toString();
    }
}
