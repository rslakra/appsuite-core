package com.rslakra.appsuite.core.jwt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

/**
 * @author Rohtash Lakra
 * @created 3/13/20 8:15 AM
 */
public class PEMReader {

    // Begin markers for all supported PEM files
    public static final String PRIVATE_PKCS1_MARKER = "-----BEGIN RSA PRIVATE KEY-----";
    public static final String PRIVATE_PKCS8_MARKER = "-----BEGIN PRIVATE KEY-----";
    public static final String CERTIFICATE_X509_MARKER = "-----BEGIN CERTIFICATE-----";
    public static final String PUBLIC_X509_MARKER = "-----BEGIN PUBLIC KEY-----";
    private static final String BEGIN_MARKER = "-----BEGIN ";

    private InputStream stream;
    private byte[] dataBytes;
    private String beginMarker;

    public PEMReader(InputStream inStream) throws IOException {
        stream = inStream;
        readFile();
    }

    public PEMReader(byte[] buffer) throws IOException {
        this(new ByteArrayInputStream(buffer));
    }

    public PEMReader(String fileName) throws IOException {
        this(new FileInputStream(fileName));
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public String getBeginMarker() {
        return beginMarker;
    }

    /**
     * Read the PEM file and save the DER encoded octet stream and begin marker.
     *
     * @throws IOException
     */
    protected void readFile() throws IOException {
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(BEGIN_MARKER) != -1) {
                    beginMarker = line.trim();
                    String endMarker = beginMarker.replace("BEGIN", "END");
                    dataBytes = readBytes(reader, endMarker);
                    return;
                }
            }
            throw new IOException("Invalid PEM file: no begin marker");
        } finally {
            reader.close();
        }
    }


    /**
     * Read the lines between BEGIN and END marker and convert the Base64 encoded content into binary byte array.
     *
     * @return DER encoded octet stream
     * @throws IOException
     */
    private byte[] readBytes(final BufferedReader reader, String endMarker) throws IOException {
        String line = null;
        StringBuilder sBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.indexOf(endMarker) != -1) {
                return Base64.getDecoder().decode(sBuilder.toString());
            }

            sBuilder.append(line.trim());
        }

        throw new IOException("Invalid PEM file: No end marker");
    }
}
