package com.rslakra.appsuite.core.monitoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rslakra.appsuite.core.BeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Rohtash Lakra
 * @created 5/12/21 5:35 PM
 */
public class TimedListTest {

    private static Logger LOGGER = LoggerFactory.getLogger(TimedListTest.class);

    private long referenceTime;
    private TimedList<ListItem> listItems;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        LOGGER.debug("setUp()");
        listItems = new TimedList<>();
    }

    /**
     *
     */
    private class ListItem implements Delayed {

        private final long time;

        ListItem(long time) {
            this.time = time;
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Delayed that) {
            return (int) (getDelay(TimeUnit.MILLISECONDS) - that.getDelay(TimeUnit.MILLISECONDS));
        }

        /**
         * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
         */
        @Override
        public long getDelay(TimeUnit unit) {
            long value = time - referenceTime;
            return unit.convert(value, TimeUnit.MILLISECONDS);
        }

    }

    @Test
    public void testQueueItemExpires() throws InterruptedException {
        LOGGER.debug("+testQueueItemExpires(), listItems: {}");
        ListItem item = new ListItem(10);
        if (BeanUtils.isNull(listItems)) {
            listItems = new TimedList<>();
        }
        assertTrue(listItems.add(item));

        assertEquals(1, listItems.size());

        // increase the time
        referenceTime = 50;

        // Allow a thread switch if necessary
        TimeUnit.MILLISECONDS.sleep(100);

        // The queue should now be empty
        assertEquals(0, listItems.size());
        LOGGER.debug("-testQueueItemExpires()");
    }

    @Test
    public void testQueueOneItemExpires() throws InterruptedException {
        LOGGER.debug("+testQueueOneItemExpires(), listItems: {}");
        ListItem item = new ListItem(10);
        if (BeanUtils.isNull(listItems)) {
            listItems = new TimedList<>();
        }
        boolean result = listItems.add(item);
        assertTrue(result);

        item = new ListItem(100);
        result = listItems.add(item);
        assertTrue(result);

        assertEquals(2, listItems.size());

        // increase the time
        referenceTime = 50;

        // Allow a thread switch if necessary
        TimeUnit.MILLISECONDS.sleep(100);

        // The queue should now contain one item
        assertEquals(1, listItems.size());
        LOGGER.debug("-testQueueOneItemExpires()");
    }
}
