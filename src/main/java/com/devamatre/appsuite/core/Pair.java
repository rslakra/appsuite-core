package com.devamatre.appsuite.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 4/7/21 5:17 PM
 */
public final class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {

    public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair[0];
    public static final Pair NULL = of((Object) null, (Object) null);
    private final L left;
    private final R right;

    /**
     * @param left
     * @param right
     */
    private Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return
     */
    public L getLeft() {
        return this.left;
    }

    /**
     * @return
     */
    public R getRight() {
        return this.right;
    }

    /**
     * @param value
     * @return
     */
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }

    public L getKey() {
        return this.getLeft();
    }

    public R getValue() {
        return this.getRight();
    }

    /**
     * @param other
     * @return
     */
    @Override
    public int compareTo(final Pair<L, R> other) {
        int result = ((Comparable) this.getLeft()).compareTo(other.getLeft());
        if (result == 0) {
            result = ((Comparable) this.getRight()).compareTo(other.getRight());
        }
// return (new CompareToBuilder()).append(this.getLeft(), other.getLeft())
//            .append(this.getRight(), other.getRight()).toComparison();
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
// return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }

    /**
     * Returns the <code>Pair</code> object formatted with the provided pattern.
     *
     * @param format
     * @return
     */
    public String toString(final String format) {
        return String.format(format, this.getLeft(), this.getRight());
    }

    /**
     * Returns the Pair object.
     *
     * @param left
     * @param right
     * @param <L>
     * @param <R>
     * @return
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair(left, right);
    }

    /**
     * Returns the Map.Entry pair.
     *
     * @param pair
     * @param <L>
     * @param <R>
     * @return
     */
    public static <L, R> Pair<L, R> of(Map.Entry<L, R> pair) {
        return (pair == null ? NULL : of(pair.getKey(), pair.getValue()));
    }

    /**
     * @param <L>
     * @param <R>
     * @return
     */
    public static <L, R> Pair<L, R>[] emptyArray() {
        return (Pair[]) EMPTY_ARRAY;
    }

}
