package com.devamatre.appsuite.core;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rohtash Lakra (rslakra.work@gmail.com)
 * @version 1.0.0
 * @since 11/23/2023 1:58 PM
 */
public class NetUtilsTest {

    private static Logger LOGGER = LoggerFactory.getLogger(NetUtilsTest.class);

    /**
     * Tests the <code>randomIPAddress</code> method.
     */
    @Test
    public void testRandomIPAddressWithRange() {
        String ipAddress = NetUtils.randomIPAddress(10);
        LOGGER.debug("ipAddress:{}", ipAddress);
        assertNotNull(ipAddress);
    }

    /**
     * Tests the <code>randomIPAddress</code> method.
     */
    @Test
    public void testRandomIPAddress() {
        String ipAddress = NetUtils.randomIPAddress();
        LOGGER.debug("ipAddress:{}", ipAddress);
        assertNotNull(ipAddress);
    }

    /**
     * Tests the <code>toIPAddress</code> method.
     */
    public void testToIPAddress() {
        int ipNumber = 351617594;
        String ipAddress = NetUtils.LOCAL_HOST;
        LOGGER.debug("ipNumber:{}, ipAddress:{}", ipNumber, ipAddress);
        String ipAddressFromIPNumber = NetUtils.toIPAddress(ipNumber);
        long ipToNum = NetUtils.toIPAddress(ipAddressFromIPNumber);
        LOGGER.debug("ipAddressFromIPNumber:{}, ipToNum:{}", ipAddressFromIPNumber, ipToNum);
    }
}
