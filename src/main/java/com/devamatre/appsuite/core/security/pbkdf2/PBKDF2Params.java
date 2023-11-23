package com.devamatre.appsuite.core.security.pbkdf2;

import com.devamatre.appsuite.core.security.GuardUtils;

/**
 * The parameters for the <code>PBKDF2</code> generation.
 *
 * @author Rohtash Lakra (rslakra.work@gmail.com)
 * @version 1.0.0
 * @since 11/21/2016 04:04:53 PM
 */
public class PBKDF2Params {

    private String algorithm;
    private byte[] salt;
    private int iterations;

    /**
     * @param algorithm
     * @param salt
     * @param iterations
     */
    public PBKDF2Params(final String algorithm, final byte[] salt, final int iterations) {
        this.algorithm = algorithm;
        this.salt = salt;
        this.iterations = iterations;
    }

    /**
     * @param algorithm
     * @param salt
     */
    public PBKDF2Params(final String algorithm, final byte[] salt) {
        this(algorithm, salt, PBKDF2Generator.ITERATIONS);
    }

    /**
     * @param salt
     */
    public PBKDF2Params(final byte[] salt) {
        this(PBKDF2Generator.PBKDF2_WITH_HMAC_SHA1, salt, PBKDF2Generator.ITERATIONS);
    }

    /**
     * Returns the algorithm.
     *
     * @return
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns the salt.
     *
     * @return
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * Returns the iterations.
     *
     * @return
     */
    public int getIterations() {
        return iterations;
    }
}
