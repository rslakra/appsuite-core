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
package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author Rohtash Lakra
 */
public enum SSLUtils {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(SSLUtils.class);

    /**
     * Converts the exception into IOException.
     *
     * @param exception
     * @return
     */
    public static IOException newIOException(final Exception exception) {
        return new IOException(exception);
    }

    /**
     * @param filePath
     * @return
     */
    public static InputStream readStream(final String filePath) {
        return SSLUtils.class.getResourceAsStream(filePath);
    }

    /**
     * Creates and returns the <code>TrustManagerFactory</code> that trusts the CAs in the given
     * <code>KeyStore</code>.
     *
     * @param loadedKeyStore
     * @return
     * @throws IOException
     */
    public static TrustManagerFactory newTrustManagerFactory(final KeyStore loadedKeyStore) throws Exception {
        LOGGER.debug("+newTrustManagerFactory({})", loadedKeyStore);
        TrustManagerFactory trustManagerFactory = null;
        if (BeanUtils.isNotNull(loadedKeyStore)) {
            // Create a TrustManager that trusts the CAs in our KeyStore
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init(loadedKeyStore);
        }

        LOGGER.debug("newTrustManagerFactory(), trustManagerFactory:{}", trustManagerFactory);
        return trustManagerFactory;
    }

    /**
     * Creates an SSLSocketFactory for HTTPS. Pass a loaded KeyStore and an array of loaded KeyManagers. These objects
     * must properly loaded/initialized by the caller.
     *
     * @param loadedKeyStore
     * @param keyManagers
     * @return
     * @throws IOException
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore loadedKeyStore, KeyManager[] keyManagers)
        throws IOException {
        SSLServerSocketFactory serverSocketFactory;
        try {
            // Create a TrustManager that trusts the CAs in our KeyStore
            TrustManagerFactory trustManagerFactory = newTrustManagerFactory(loadedKeyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
            serverSocketFactory = sslContext.getServerSocketFactory();
        } catch (Exception ex) {
            throw newIOException(ex);
        }

        return serverSocketFactory;
    }

    /**
     * Creates an SSLSocketFactory for HTTPS. Pass a loaded KeyStore and a loaded KeyManagerFactory. These objects must
     * properly load/initialized by the caller.
     *
     * @param loadedKeyStore
     * @param loadedKeyFactory
     * @return
     * @throws IOException
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore loadedKeyStore,
                                                              KeyManagerFactory loadedKeyFactory) throws IOException {
        try {
            return makeSSLSocketFactory(loadedKeyStore, loadedKeyFactory.getKeyManagers());
        } catch (Exception ex) {
            throw newIOException(ex);
        }
    }

    /**
     * Creates an SSLSocketFactory for HTTPS. Pass a KeyStore resource with your certificate and passphrase
     *
     * @param keyAndTrustStoreFilePath
     * @param passphrase
     * @return
     * @throws IOException
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(String keyAndTrustStoreFilePath, char[] passphrase)
        throws IOException {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keyStoreStream = readStream(keyAndTrustStoreFilePath);
            if (BeanUtils.isNotNull(keyStoreStream)) {
                throw new IOException("Unable to load keystore from classpath:" + keyAndTrustStoreFilePath);
            }

            keystore.load(keyStoreStream, passphrase);
            KeyManagerFactory
                keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, passphrase);
            return makeSSLSocketFactory(keystore, keyManagerFactory);
        } catch (Exception ex) {
            throw newIOException(ex);
        }
    }

    /**
     * Creates a SSLSocketFactory for HTTPS connection with the given '.crt' (certificate) file.
     *
     * @param certInputStream
     * @return
     * @throws IOException
     */
    public static SSLServerSocketFactory makeSSLSocketFactory(final InputStream certInputStream) throws IOException {
        try {
            // Load CAs from an InputStream
            CertificateFactory mCertFactory = CertificateFactory.getInstance("X.509");
            InputStream caInputStream = new BufferedInputStream(certInputStream);
            java.security.cert.Certificate mCertificate;
            try {
                mCertificate = mCertFactory.generateCertificate(caInputStream);
                IOUtils.debug("CA=" + ((X509Certificate) mCertificate).getSubjectDN());
            } finally {
                IOUtils.closeSilently(caInputStream);
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", mCertificate);

            // Create a TrustManager that trusts the CAs in our KeyStore
            TrustManagerFactory trustManagerFactory = newTrustManagerFactory(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getServerSocketFactory();
        } catch (Exception ex) {
            throw newIOException(ex);
        }
    }

    /**
     * The 'javax.net.ssl.trustStore' to be set with the 'JKS' file path.
     *
     * @param mFile
     */
    public static void setSSLTrustStore(final File mFile) {
        System.setProperty("javax.net.ssl.trustStore", mFile.getAbsolutePath());
    }

    /**
     * The 'javax.net.ssl.trustStore' to be set with the 'JKS' file path.
     *
     * @param jksFilePath
     */
    public static void setSSLTrustStore(final String jksFilePath) {
        setSSLTrustStore(new File(jksFilePath));
    }

    /**
     * Returns true if the <cod>hostName</cod> contains any of the
     * <code>allowedHostNames</code>
     * otherwise false.
     *
     * @param hostName
     * @param allowedHostNames
     * @return
     */
    public static boolean isAllowedHostname(final String hostName, final String... allowedHostNames) {
        if (hostName != null && allowedHostNames != null) {
            for (String host : allowedHostNames) {
                if (hostName.contains(host)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * It must be called before setting the SSL factory. The
     * <code>hostName</code> is real host
     * name, which handles the SSL requests. If the <code>hostName</code> is null, by default 'localhost' is used.
     *
     * @param hostName
     * @param allowedHostNames
     */
    public static void setDefaultHostnameVerifier(final String hostName, final String... allowedHostNames) {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            /**
             * Verify the SSL host name.
             *
             * @param hostname
             * @param sslSession
             * @return
             */
            public boolean verify(String hostname, SSLSession sslSession) {
                if (hostname == null || hostname.trim().length() == 0) {
                    hostname = hostName;
                }

                // HostnameVerifier hostNameVerifier =
                // HttpsURLConnection.getDefaultHostnameVerifier();
                // if(hostNameVerifier != null) {
                // return hostNameVerifier.verify(hostname, sslSession);
                // }

                if (hostname.equals("localhost")) {
                    return true;
                } else if (hostname.equals(hostName)) {
                    return true;
                } else if (isAllowedHostname(hostname, allowedHostNames)) {
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * @param certFilePath
     * @param keyStorePassword
     * @param keyPassword
     * @param port
     * @return
     * @throws Exception
     */
    public static SSLServerSocket makeSSLServerSocket(final String certFilePath, final char[] keyStorePassword,
                                                      final char[] keyPassword, final int port) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(certFilePath), keyStorePassword);
        System.out.println(keyStore.getProvider());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, keyPassword);
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
        return serverSocket;
    }

}
