package com.rslakra.appsuite.core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Rohtash Lakra
 */
public class HeapMonitor extends SingleThreadExecutor {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(HeapMonitor.class);

    private final Runtime runtime = Runtime.getRuntime();
    private long interval;
    private TimeUnit timeUnit;
    private ExecutorService executor;

    /**
     *
     */
    public HeapMonitor() {
        super("HeapMonitor");
    }

    /**
     * @return
     */
    public final Runtime getRuntime() {
        return runtime;
    }

    /**
     * @return
     */
    public final long getInterval() {
        return interval;
    }

    /**
     * @return
     */
    public final TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * @return
     */
    public final ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Called on shutdown
     */
    public final void destroy() {
        getExecutor().shutdown();
    }

    /**
     * MemoryAccumulator
     */
    private final class MemoryAccumulator implements Runnable {

        // memoryStates
        private final List<MemoryState> memoryStates = new ArrayList<>();

        /**
         *
         */
        @Override
        public void run() {
            while (true) {
                addMemoryStates(MemoryState.of(runtime.freeMemory(), runtime.totalMemory(), runtime.maxMemory()));
                timeToSleep();
            }
        }

        /**
         * Creates a sparse list to add only those entries that are needed.
         *
         * @param memoryState
         */
        private void addMemoryStates(final MemoryState memoryState) {
            memoryStates.add(memoryState);
        }

        /**
         *
         */
        private void timeToSleep() {
            try {
                getTimeUnit().sleep(getInterval());
            } catch (InterruptedException ex) {
                LOGGER.warn("InterruptedException:{}", ex.getMessage(), ex);
            }
        }

        /**
         * @return
         */
        public final List<MemoryState> getMemoryStates() {
            return memoryStates;
        }
    }

    /**
     * @return
     */
    @Override
    protected Runnable getRunnable() {
        return new MemoryAccumulator();
    }

}
