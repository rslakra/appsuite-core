package com.rslakra.appsuite.core.monitoring;

import java.util.StringJoiner;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * The memory state holding at the particular point of time.
 *
 * @author Rohtash Lakra
 */
public class MemoryState implements Delayed {

    /**
     * The time for the current state of the memory.
     */
    private final long time;

    /**
     * The current free heap space
     */
    private final long freeMemory;

    /**
     * The current heap size
     */
    private final long totalMemory;

    /**
     * The total amount of heap size
     */
    private final long maxMemory;

    /**
     * Returns the object of the <code>MemoryState</code>.
     *
     * @param freeMemory
     * @param totalMemory
     * @param maxMemory
     * @return
     */
    public static MemoryState of(final long freeMemory, final long totalMemory, final long maxMemory) {
        return new MemoryState(freeMemory, totalMemory, maxMemory);
    }

    /**
     * @return
     */
    public static MemoryState ofZero() {
        return of(0, 0, 0);
    }

    /**
     * @param freeMemory
     * @param totalMemory
     * @param maxMemory
     */
    public MemoryState(final long freeMemory, final long totalMemory, final long maxMemory) {
        this.time = System.currentTimeMillis();
        this.freeMemory = freeMemory;
        this.totalMemory = totalMemory;
        this.maxMemory = maxMemory;
    }

    /**
     * @return
     */
    public final long getTime() {
        return time;
    }

    /**
     * @return
     */
    public final long getFreeMemory() {
        return freeMemory;
    }

    /**
     * @return
     */
    public final long getTotalMemory() {
        return totalMemory;
    }

    /**
     * @return
     */
    public final long getMaxMemory() {
        return maxMemory;
    }

    /**
     * @return
     */
    public final long getFreeMemoryPercentage() {
        return ((getFreeMemory() * 100) / getTotalMemory());
    }

    /**
     * @param other
     * @return
     */
    @Override
    public int compareTo(final Delayed other) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
    }

    /**
     * Returns the remaining time.
     *
     * @return
     */
    private long getRemainingTime() {
        return (getTime() - System.currentTimeMillis());
    }

    /**
     * @param timeUnit
     * @return
     */
    @Override
    public final long getDelay(final TimeUnit timeUnit) {
        return timeUnit.convert(getRemainingTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", MemoryState.class.getSimpleName() + "[", "]")
            .add("time=" + time)
            .add("freeMemory=" + freeMemory)
            .add("totalMemory=" + totalMemory)
            .add("maxMemory=" + maxMemory)
            .toString();
    }
}
