package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Rohtash Lakra
 * @created 5/11/22 6:10 PM
 */
public class StopWatch {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(StopWatch.class);
    private Instant startTime;
    private Instant stopTime;
    private Duration duration;

    public StopWatch() {
    }

    /**
     *
     */
    public final void startTimer() {
        startTime = Instant.now();
        LOGGER.debug("startTimer()");
    }

    /**
     *
     */
    public final void stopTimer() {
        stopTime = Instant.now();
        duration = Duration.between(startTime, stopTime);
        LOGGER.debug("stopTimer()");
    }

    /**
     * @return
     */
    private String timeTaken() {
        return String.format("%dD, %02d:%02d:%02d.%04d", duration.toDays(), duration.toHours(), duration.toMinutes(),
                             duration.getSeconds(), duration.toMillis());
    }

    /**
     * @return
     */
    public final String took() {
        return timeTaken();
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(StopWatch.class)
            .add("startTime", startTime)
            .add("endTime", stopTime)
            .add("duration", timeTaken())
            .toString();
    }

}
