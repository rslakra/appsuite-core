/******************************************************************************
 * Copyright (C) Devamatre 2009 - 2022. All rights reserved.
 *
 * This code is licensed to Devamatre under one or more contributor license 
 * agreements. The reproduction, transmission or use of this code, in source 
 * and binary forms, with or without modification, are permitted provided 
 * that the following conditions are met:
 * <pre>
 *  1. Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * </pre>
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Devamatre reserves the right to modify the technical specifications and or 
 * features without any prior notice.
 *****************************************************************************/
package com.devamatre.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * @author Rohtash Lakra
 * @version Apr 11, 2006
 */

public enum NetUtils {
    INSTANCE;

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);
    public static final String OCTET_ZERO = "0";
    public static final String OCTET_255 = "255";
    public static final String LOCAL_HOST = "localhost";
    private static final Random RND_IP = new Random();

    /**
     * @param ipRange
     * @return
     */
    public static String randomIPAddress(int ipRange) {
        return (RND_IP.nextInt(ipRange) + "." + RND_IP.nextInt(ipRange) + "." + RND_IP.nextInt(ipRange) + "."
                + RND_IP.nextInt(ipRange));
    }

    /**
     * @return
     */
    public static String randomIPAddress() {
        return randomIPAddress(256);
    }


    /**
     * @param ipNumber
     * @return
     */
    public static String toIPAddress(int ipNumber) {
        return (ipNumber & 0xFF) + "." + ((ipNumber >> 8) & 0xFF) + "." + ((ipNumber >> 16) & 0xFF) + "." + (
            (ipNumber >> 24) & 0xFF);
    }

    /**
     * Returns the long IP address of the provided <code>ipAddress</code> text.
     *
     * @param ipAddress
     * @return
     */
    public static long toIPAddress(String ipAddress) {
        String[] ipTokens = ipAddress.split("\\.");
        long ipNumber = 0;
        for (int i = 0; i < ipTokens.length; i++) {
            // IP address range is 0-255, so implicit casting it to the range.
            ipNumber += ((Integer.parseInt(ipTokens[i]) % 256 * Math.pow(256, i)));
        }

        return ipNumber;
    }

    /**
     * Returns the integer equivalent.
     *
     * @param numString
     * @return
     */
    public static int toInteger(final String numString) {
        return Integer.parseInt(numString);
    }

    /**
     * @param ipOctet
     * @param subNetMaskOctet
     * @return
     */
    public static String toBroadcastAddressOctet(String ipOctet, String subNetMaskOctet) {
        LOGGER.debug("+toBroadcastAddressOctet({}, {})", ipOctet, subNetMaskOctet);
        String broadCastAddressOctet = null;
        if (OCTET_ZERO.equals(subNetMaskOctet)) {
            broadCastAddressOctet = OCTET_255;
        } else if (OCTET_255.equals(subNetMaskOctet)) {
            broadCastAddressOctet = ipOctet;
        } else {
            int ipOctetValue = toInteger(ipOctet);
            int subNetMaskOctetValue = toInteger(subNetMaskOctet);
            LOGGER.debug("ipOctetValue:{}, subNetMaskOctetValue:{})", ipOctetValue, subNetMaskOctetValue);
            int magicMultiplier = 256 - subNetMaskOctetValue;
            int result = (magicMultiplier * ((ipOctetValue / magicMultiplier) + 1)) - 1;
            LOGGER.debug("magicMultiplier:{}, result:{})", magicMultiplier, result);
            broadCastAddressOctet = String.valueOf(result);
        }

        LOGGER.debug("-toBroadcastAddressOctet(), broadCastAddressOctet:{}", broadCastAddressOctet);
        return broadCastAddressOctet;
    }

    /**
     * Returns the broadcast address of the given IP address and subnet mask.
     *
     * @param ipAddress
     * @param subNetMask
     * @return
     */
    public static String findBroadcastAddress(String ipAddress, String subNetMask) {
        LOGGER.debug("+findBroadcastAddress({}, {})", ipAddress, subNetMask);
        final StringBuilder broadCastAddress = new StringBuilder();
        if (BeanUtils.isNotEmpty(ipAddress) && BeanUtils.isNotEmpty(subNetMask)) {
            String[] ipTokens = ipAddress.split("[.]");
            String[] subNetMaskTokens = subNetMask.split("[.]");
            for (int i = 0; i < ipTokens.length; i++) {
                broadCastAddress.append(toBroadcastAddressOctet(ipTokens[i], subNetMaskTokens[i]));
                if (i < ipTokens.length - 1) {
                    broadCastAddress.append(".");
                }
            }
        }

        LOGGER.debug("-findBroadcastAddress(), broadCastAddress:{}", broadCastAddress);
        return broadCastAddress.toString();
    }


    /**
     * Make a BufferedReader to get incoming data.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public static BufferedReader getReader(Socket socket) throws IOException {
        return (new BufferedReader(new InputStreamReader(socket.getInputStream())));
    }

    /**
     * Make a PrintWriter to send outgoing data. This PrintWriter will automatically flush stream when
     * <code>println(...)</code> is called.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public static PrintWriter getWriter(Socket socket) throws IOException {
        /* autoFlush set to be true */
        return (new PrintWriter(socket.getOutputStream(), true));
    }

}
