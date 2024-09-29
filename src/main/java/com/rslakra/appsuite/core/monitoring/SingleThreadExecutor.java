package com.rslakra.appsuite.core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * The <code>SingleThreadExecutor</code> class that handles the running of a single thread via an executor service.
 *
 * @author Rohtash Lakra
 */
public abstract class SingleThreadExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(SingleThreadExecutor.class);
    private final ExecutorService executor;

    /**
     * @param threadName
     */
    public SingleThreadExecutor(final String threadName) {
        this.executor = createExecutor(threadName);
    }

    /**
     * @return A new executor service.
     */
    protected ExecutorService createExecutor(final String threadName) {

        final UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {

            /**
             *
             * @param thread
             * @param throwable
             */
            @Override
            public void uncaughtException(final Thread thread, Throwable throwable) {
                LOGGER.error("Exception in timed list: " + throwable.getMessage(), throwable);
                startRunnable();
            }
        };

        final ThreadFactory threadFactory = new ThreadFactory() {

            /**
             *
             * @param runnable
             * @return
             */
            @Override
            public Thread newThread(Runnable runnable) {
                final Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                thread.setName(threadName);
                thread.setUncaughtExceptionHandler(exceptionHandler);
                return thread;
            }
        };

        return Executors.newSingleThreadExecutor(threadFactory);
    }

    /**
     * Start the single background thread.
     */
    protected void startRunnable() {
        executor.execute(getRunnable());
    }

    /**
     * @return
     */
    protected abstract Runnable getRunnable();

}
