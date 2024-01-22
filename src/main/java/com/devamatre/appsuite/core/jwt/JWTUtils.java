package com.devamatre.appsuite.core.jwt;

import com.devamatre.appsuite.core.BeanUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.devamatre.appsuite.core.Payload;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 3/10/20 10:57 AM
 */
public enum JWTUtils {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);
    public static final String ALGO_RSA = "RSA";
    public static final String UTF_8 = "UTF-8";
    public static final String NEW_LINE = "\n";
    public static final String PRIVATE_KEY = "_private.key";
    public static final String PUBLIC_KEY = "_public.key";
    public static final String JWKS_FILE_SUFFIX = ".well-known/jwks.json";
    public static final String JWS_HEADER_TYPE = "typ";
    public static final String JWS_HEADER_TYPE_VALUE = "JWT";
    private KeyFactory rsaKeyFactory;
    private SignedJWT signedJWT;
    private RSAKey rsaKey;
    private String keyFolderPath;
    private String service;

    // payloadBuilder
    private final Payload<String, HttpsJwks> payload = new Payload();

    /**
     * @return
     */
    public String getKeyFolderPath() {
        return keyFolderPath;
    }

    /**
     * @param keyFolderPath
     */
    public void setKeyFolderPath(String keyFolderPath) {
        this.keyFolderPath = keyFolderPath;
    }

    /**
     * @param line
     * @return
     */
    public boolean endsWithKey(final String line) {
        return line.endsWith(" KEY-----");
    }

    /**
     * Returns true if the line is certificates begin line otherwise false.
     *
     * @param line
     * @return
     */
    public boolean startsWithBegin(final String line) {
        return (line.startsWith("-----BEGIN ") && endsWithKey(line));
    }

    /**
     * Returns true if the line is certificates end line otherwise false.
     *
     * @param line
     * @return
     */
    public boolean startsWithEnd(final String line) {
        return (line.startsWith("-----END ") && endsWithKey(line));
    }

    /**
     * @return
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return
     */
    public String getPrivateKeyFile() {
        return (getService() + PRIVATE_KEY);
    }

    /**
     * @return
     */
    public String getPublicKeyFile() {
        return (getService() + PUBLIC_KEY);
    }

    /**
     * Returns the <code>KeyFactory</code> for the <code>algorithm</code> algorithm.
     *
     * @param algorithm
     * @return
     */
    public KeyFactory getKeyFactory(final String algorithm) {
        if (rsaKeyFactory == null) {
            try {
                rsaKeyFactory = KeyFactory.getInstance(algorithm);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }

        return rsaKeyFactory;
    }

    /**
     * @param rawBytes
     * @return
     */
    public String toUTF8String(final byte[] rawBytes) {
        try {
            return new String(rawBytes, UTF_8);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * @param pathString
     * @return
     */
    public Path getPath(final String pathString) {
        if (BeanUtils.isEmpty(getKeyFolderPath())) {
            final String pkgString = BeanUtils.getClassPath(JWTUtils.class, pathString);
            return Paths.get("src/main/java", pkgString);
        } else {
            return Paths.get(getKeyFolderPath(), pathString);
        }
    }

    /**
     * Reads the <code>pathString</code> file bytes.
     *
     * @param pathString
     * @return
     * @throws IOException
     */
    public byte[] readAllBytes(final String pathString) throws IOException {
        return Files.readAllBytes(getPath(pathString));
    }

    /**
     * @param keyLines
     * @param removeKeyLines
     * @return
     */
    public String toString(final List<String> keyLines, final boolean removeKeyLines) {
        final StringBuilder sBuilder = new StringBuilder();
        if (keyLines != null) {
            keyLines.forEach(line -> {
                if (removeKeyLines) {
                    if (!(startsWithBegin(line) || startsWithEnd(line))) {
                        sBuilder.append(line);
                    }
                } else {
                    sBuilder.append(line);
                }
            });
        }

        return sBuilder.toString();
    }

    /**
     * @param pathString
     * @param removeKeyLines
     * @return
     * @throws IOException
     */
    public String readKeyContents(final String pathString, final boolean removeKeyLines) throws IOException {
        return toString(Files.readAllLines(getPath(pathString)), removeKeyLines);
    }

    /**
     * @param inputStream
     * @param addNewLines
     * @return
     * @throws IOException
     */
    public String readContents(final InputStream inputStream, final boolean addNewLines)
        throws IOException {
        final StringBuilder sBuilder = new StringBuilder();
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = bReader.readLine()) != null) {
                if (startsWithBegin(line) || startsWithEnd(line)) {
                    continue;
                }
                sBuilder.append(line);
                if (addNewLines) {
                    sBuilder.append(NEW_LINE);
                }
            }
        }

        return sBuilder.toString();
    }

// /**
//     * Returns the <code>PrivateKey</code> for the <code>pathString</code>.
//     *
//     * @param pathString
//     * @return
//     * @throws Exception
//     */
// public PrivateKey loadPrivateKey(final String pathString) throws Exception {
// PrivateKey privateKey = null;
// final KeyFactory keyFactory = getKeyFactory(ALGO_RSA);
// final List<String> keyLines = Files.readAllLines(getPath(pathString));
// final String keyContents = toString(keyLines, true);
// byte[] keyBytes = Base64.getDecoder().decode(keyContents);
// /*
//         * PKCS#! has the 'BEGIN RSA PRIVATE KEY' header, and
//         * PKCS#8 has the 'BEGIN PRIVATE KEY' header.
//         */
// if (keyLines.get(0).contains("BEGIN PRIVATE KEY")) {
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//            privateKey = keyFactory.generatePrivate(keySpec);
// } else if (keyLines.get(0).contains(ALGO_RSA)) {
//            final DerInputStream derReader = new DerInputStream(keyBytes);
//            final DerValue[] seq = derReader.getSequence(0);
//            // skip version seq[0];
//            final BigInteger modulus = seq[1].getBigInteger();
//            final BigInteger publicExponent = seq[2].getBigInteger();
//            final BigInteger privateExponent = seq[3].getBigInteger();
//            final BigInteger primeP = seq[4].getBigInteger();
//            final BigInteger primeQ = seq[5].getBigInteger();
//            final BigInteger primeExponentP = seq[6].getBigInteger();
//            final BigInteger primeExponentQ = seq[7].getBigInteger();
//            final BigInteger crtCoefficient = seq[8].getBigInteger();
//            RSAPrivateCrtKeySpec
//                keySpec =
//                new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ,
//                                         primeExponentP,
//                                         primeExponentQ, crtCoefficient);
//            privateKey = keyFactory.generatePrivate(keySpec);
// } else {
//            throw new RuntimeException("Unsupported PEM Key!");
// }
//
// return privateKey;
//    }

    /**
     * @return
     * @throws Exception
     */
    public PrivateKey loadPrivateKey() throws Exception {
// return loadPrivateKey(getPrivateKeyFile());
        return null;
    }

    /**
     * Returns the <code>PublicKey</code> for the <code>pathString</code>.
     *
     * @param pathString
     * @param keyAlgo
     * @return
     * @throws Exception
     */
    public PublicKey loadPublicKey(final String pathString, final String keyAlgo) throws Exception {
        PublicKey publicKey = null;
        final KeyFactory keyFactory = getKeyFactory(keyAlgo);
        final List<String> keyLines = Files.readAllLines(getPath(pathString));
        final String keyContents = toString(keyLines, true);
// keyContents = keyContents.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
// keyContents = keyContents.replace("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(keyContents);
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    /**
     * @return
     * @throws Exception
     */
    public PublicKey loadPublicKey(final String keyAlgo) throws Exception {
        return loadPublicKey(getPublicKeyFile(), keyAlgo);
    }

    /**
     * @return
     * @throws Exception
     */
    public static PublicKey loadRSAPublicKey() throws Exception {
        return INSTANCE.loadPublicKey(ALGO_RSA);
    }

    /**
     * Adds/Subtracts the number of days in the given date.
     *
     * @param date
     * @param days
     */
    public static Date addDays(final Date date, final int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * @return
     */
    public SignedJWT getSignedJWT() {
        return signedJWT;
    }

    /**
     * @return
     */
    private RSAKey getRSAKey() {
        return rsaKey;
    }

    /**
     * @param size
     * @param keyId
     * @throws JOSEException
     */
    public void setRSAKey(final int size, final String keyId) throws JOSEException {
        if (Objects.isNull(getRSAKey())) {
            rsaKey = new RSAKeyGenerator(size)
                .keyID(keyId)
                .generate();
        }
    }

//    /**
//     * @param priKeyBytes
//     * @param pubKeyBytes
//     * @return
//     * @throws InvalidKeySpecException
//     */
//    public KeyPair toKeyPair(final byte[] priKeyBytes, final byte[] pubKeyBytes)
// throws InvalidKeySpecException {
// final KeyFactory keyFactory = getKeyFactory(ALGO_RSA);
// PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(priKeyBytes));
// PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubKeyBytes));
// return new KeyPair(pubKey, priKey);
//    }

    /**
     * The rsaKey to be set.
     *
     * @throws JOSEException
     */
    private void setRSAKey() throws JOSEException {
        if (Objects.isNull(getRSAKey())) {
            try {
//                PrivateKey privateKey = loadPrivateKey(getPrivateKeyFile());
                PrivateKey privateKey = null;
                PublicKey publicKey = loadPublicKey(getPublicKeyFile());
                RSAKey.Builder rsaKeyBuilder = new RSAKey.Builder((RSAPublicKey) publicKey).privateKey(privateKey);
                rsaKey = rsaKeyBuilder.build();
            } catch (Exception ex) {
                throw new JOSEException(ex.getLocalizedMessage(), ex);
            }
        }
    }

    /**
     * @return
     */
    public RSAKey getRSAPublicKey() {
        return getRSAKey().toPublicJWK();
    }

    /**
     * JWT Header
     * <pre>
     * {
     * "alg": "HS256",
     * "typ": "JWT"
     * }
     * </pre>
     */
    private JWSHeader jwtHeader(final JWSAlgorithm algorithm, final JOSEObjectType type) {
        return new JWSHeader.Builder(algorithm).type(type).build();
    }

    /**
     * JWT Claims
     * <pre>
     * {
     * "aud": "{protocol}://{b2b.host}/identity/oauth2/access_token?realm=<your-realm>",
     * "iss": "{client_id}",
     * "sub": "{client_id}",
     * "exp": “{expiry time in seconds}”,
     * "iat": “{issued time in seconds}”
     * }
     * </pre>
     *
     * @param keyId
     * @param audience
     * @param issuer
     * @param subject
     * @param issuedAt
     * @param expiredOn
     * @return
     */
    public static JWTClaimsSet jwtClaimsSet(final String keyId, final String audience, final String issuer,
                                            final String subject, final Date issuedAt, final Date expiredOn) {
        // Prepare JWT with claims set
        final JWTClaimsSet.Builder jwtClaimsBuilder = new JWTClaimsSet.Builder();
        if (audience != null) {
            jwtClaimsBuilder.audience(audience);
        }
        jwtClaimsBuilder.issuer(issuer);
        jwtClaimsBuilder.subject(subject);
        if (keyId != null) {
            jwtClaimsBuilder.claim("cli", keyId);
        }
// Date issueTime = Date.from(Instant.ofEpochSecond(issuedAt.getTime()));
// LOGGER.debug("issueTime:" + issueTime);
// Date expiryTime = Date.from(Instant.ofEpochMilli(Instant.now().plusMillis(60 * 1000).toEpochMilli()));
// LOGGER.debug("expiryTime:" + expiryTime);
        jwtClaimsBuilder.issueTime(issuedAt);
        jwtClaimsBuilder.expirationTime(new Date(expiredOn.getTime() + 60 * 1000));
        return jwtClaimsBuilder.build();
    }

    /**
     * JWT Claims
     * <pre>
     * {
     * "aud": "{protocol}://{b2b.host}/identity/oauth2/access_token?realm=<your-realm>",
     * "iss": "{client_id}",
     * "sub": "{client_id}",
     * "exp": “{expiry time in seconds}”,
     * "iat": “{issued time in seconds}”
     * }
     * </pre>
     *
     * @param audience
     * @param issuer
     * @param subject
     * @return
     */
    public static JWTClaimsSet jwtClaimsSet(final String audience, final String issuer, final String subject) {
        return jwtClaimsSet(null, audience, issuer, subject, new Date(), addDays(new Date(), 1));
    }

    /**
     * JWT Header
     * <pre>
     * {
     * "alg": "HS256",
     * "typ": "JWT"
     * }
     * </pre>
     * <p>
     * JWT Claims
     * <pre>
     * {
     * "aud": "{protocol}://{b2b.host}/identity/oauth2/access_token?realm=<your-realm>",
     * "iss": "{client_id}",
     * "sub": "{client_id}",
     * "exp": “{expiry time in seconds}”,
     * "iat": “{issued time in seconds}”
     * }
     * </pre>
     *
     * @param audience
     * @param issuer
     * @param subject
     * @param clientSecret
     * @throws JOSEException
     */
    public static String jwtMACSignedToken(final String audience, final String issuer, final String subject,
                                           final String clientSecret) throws JOSEException {
        final JWSHeader jwsHeader = INSTANCE.jwtHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT);
        // Prepare JWT with claims set
        final JWTClaimsSet jwtClaimsSet = jwtClaimsSet(audience, issuer, subject);
        // Create the signed JWT.
        final SignedJWT signedJwt = jwtMACSigned(jwsHeader, jwtClaimsSet, clientSecret);
        // To serialize to compact form
        return signedJwt.serialize();
    }

    /**
     * JWT Header
     * <pre>
     * {
     * "alg": "HS256",
     * "typ": "JWT"
     * }
     * </pre>
     * <p>
     * JWT Claims
     * <pre>
     * {
     * "aud": "{protocol}://{b2b.host}/identity/oauth2/access_token?realm=<your-realm>",
     * "iss": "{client_id}",
     * "sub": "{client_id}",
     * "exp": “{expiry time in seconds}”,
     * "iat": “{issued time in seconds}”
     * }
     * </pre>
     *
     * @param audience
     * @param clientId
     * @param clientSecret
     * @throws JOSEException
     */
    public static String jwtMACSignedToken(final String audience, final String clientId, final String clientSecret)
        throws JOSEException {
        return jwtMACSignedToken(audience, clientId, clientId, clientSecret);
    }

    /**
     * @param jwtHeaders
     * @param jwtClaims
     * @param jwtSigner
     * @return
     */
    private SignedJWT jwtSigned(final JWSHeader jwtHeaders, final JWTClaimsSet jwtClaims, final JWSSigner jwtSigner)
        throws JOSEException {
        // create signed JWT with jwsHeader and jwtClaims
        SignedJWT jwtSigned = new SignedJWT(jwtHeaders, jwtClaims);
        // Compute the signature
        jwtSigned.sign(jwtSigner);

        return jwtSigned;
    }

    /**
     * @param jwtHeaders
     * @param jwtClaims
     * @param rsaKey
     * @return
     * @throws JOSEException
     */
    public static SignedJWT jwtRSASigned(final JWSHeader jwtHeaders, final JWTClaimsSet jwtClaims, final RSAKey rsaKey)
        throws JOSEException {
        // create signed JWT with jwsHeader and jwtClaims and compute signature
        return INSTANCE.jwtSigned(jwtHeaders, jwtClaims, new RSASSASigner(rsaKey));
    }

    /**
     * @param jwtHeaders
     * @param jwtClaims
     * @param clientSecret
     * @return
     * @throws JOSEException
     */
    public static SignedJWT jwtMACSigned(final JWSHeader jwtHeaders, final JWTClaimsSet jwtClaims,
                                         final String clientSecret)
        throws JOSEException {
        // create signed JWT with jwsHeader and jwtClaims and compute signature
        return INSTANCE.jwtSigned(jwtHeaders, jwtClaims, new MACSigner(clientSecret));
    }


    /**
     * Returns the parsed <code>jwtToken</code>.
     *
     * @param jwtToken
     * @return
     * @throws ParseException
     */
    private SignedJWT jwtSigned(final String jwtToken) throws ParseException {
        // parse the jwtToken
        return SignedJWT.parse(jwtToken);
    }

    /**
     * @param jwtToken
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public JWSHeader jwtHeaders(final String jwtToken) throws ParseException {
        // parse the JWS and verify its signature
        return jwtSigned(jwtToken).getHeader();
    }

    /**
     * Parse the JWT and verify with client secret.
     *
     * @param jwtToken
     * @param jwsVerifier
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    private JWTClaimsSet jwtVerifiedClaims(final String jwtToken, final JWSVerifier jwsVerifier)
        throws ParseException, JOSEException {
        // parse the JWS and verify its signature
        SignedJWT signedJwt = jwtSigned(jwtToken);
        return signedJwt.verify(jwsVerifier) ? signedJwt.getJWTClaimsSet() : null;
    }

    /**
     * Parse the JWT and verify with client secret.
     *
     * @param jwtToken
     * @param clientSecret
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static JWTClaimsSet jwtMACVerifiedClaims(final String jwtToken, final String clientSecret)
        throws ParseException, JOSEException {
        // parse the JWT Token and verify its signature
        return INSTANCE.jwtVerifiedClaims(jwtToken, new MACVerifier(clientSecret));
    }

    /**
     * On the consumer side, parse the JWS and verify its RSA signature
     *
     * @param jwtToken
     * @param rsaPublicKey
     * @return
     * @throws JOSEException
     * @throws ParseException
     */
    public static JWTClaimsSet jwtRSASSAVerifiedClaims(final String jwtToken, final RSAKey rsaPublicKey)
        throws JOSEException, ParseException {
        return INSTANCE.jwtVerifiedClaims(jwtToken, new RSASSAVerifier(rsaPublicKey));
    }

    /**
     * Parse the JWT and verify with client secret.
     *
     * @param jwtToken
     * @param jwsVerifier
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    private boolean jwtVerified(final String jwtToken, final JWSVerifier jwsVerifier)
        throws ParseException, JOSEException {
        // parse the JWS and verify its signature
        return jwtSigned(jwtToken).verify(jwsVerifier);
    }

    /**
     * Parse the JWT and verify with client secret.
     *
     * @param jwtToken
     * @param clientSecret
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static boolean jwtMACVerified(final String jwtToken, final String clientSecret)
        throws ParseException, JOSEException {
        // parse the JWT Token and verify its signature
        return INSTANCE.jwtVerified(jwtToken, new MACVerifier(clientSecret));
    }

    /**
     * On the consumer side, parse the JWS and verify its RSA signature
     *
     * @param jwtToken
     * @param rsaPublicKey
     * @return
     * @throws JOSEException
     * @throws ParseException
     */
    public static boolean jwtRSASSAVerified(final String jwtToken, final RSAKey rsaPublicKey)
        throws JOSEException, ParseException {
        // parse the JWT Token and verify its signature
        return INSTANCE.jwtVerified(jwtToken, new RSASSAVerifier(rsaPublicKey));
    }

    /**
     * JWT Header
     * <pre>
     * {
     * "alg": "HS256",
     * "typ": "JWT"
     * }
     * </pre>
     * <p>
     * JWT Claims
     * <pre>
     * {
     * "aud": "{protocol}://{b2b.host}/identity/oauth2/access_token?realm=<your-realm>",
     * "iss": "{client_id}",
     * "sub": "{client_id}",
     * "exp": “{expiry time in seconds}”,
     * "iat": “{issued time in seconds}”
     * }
     * </pre>
     *
     * @param keyId
     * @param subject
     * @param issuer
     * @throws JOSEException
     */
    public static String jwtRSASignedToken(final String keyId, final String audience, final String issuer,
                                           final String subject, final Date issuedAt, final Date expiredOn)
        throws JOSEException {
        //RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures
// setRSAKey(keySize, keyId);
        INSTANCE.setRSAKey();

        // prepare header
        final JWSHeader jwsHeader = INSTANCE.jwtHeader(JWSAlgorithm.RS256, JOSEObjectType.JWT);
        // Prepare JWT with claims set
        final JWTClaimsSet jwtClaimsSet = INSTANCE.jwtClaimsSet(keyId, audience, issuer, subject, issuedAt, expiredOn);
        // Create RSA-signer with the private key, compute the signature
        INSTANCE.signedJWT = jwtRSASigned(jwsHeader, jwtClaimsSet, INSTANCE.getRSAKey());

        /**
         * To serialize to compact form, produces something like
         *
         * eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2LmZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3QdmaXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7-jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
         */
        return INSTANCE.signedJWT.serialize();
    }

    /**
     * Fetches the public key from the <code>urlJWKSFilePath</code>.
     * <p>
     * The time period to cache the JWKs from the endpoint, if the cache directive headers of the response are not
     * present or indicate that the content should not be cached. This is useful because the content of a JWKS endpoint
     * should be cached in the vast majority of situations and cache directive headers that indicate otherwise are
     * likely a mistake or misconfiguration.
     * <p>
     * The default value, used when this method is not called, of the default cache duration is 3600 seconds (1 hour).
     *
     * @param urlJWKSFilePath
     * @param defaultCacheDuration the length in seconds of the default cache duration
     * @return
     */
    public List<JsonWebKey> fetchJsonWebKeys(final String urlJWKSFilePath, final long defaultCacheDuration)
        throws JoseException, IOException {
        if (BeanUtils.isEmpty(urlJWKSFilePath)) {
            throw new IllegalArgumentException("Invalid JWKS file url!");
        }

        HttpsJwks jwksRequest = payload.get(urlJWKSFilePath);
        if (BeanUtils.isEmpty(jwksRequest)) {
            jwksRequest = new HttpsJwks(urlJWKSFilePath);
            payload.add(urlJWKSFilePath, jwksRequest);
            jwksRequest.setDefaultCacheDuration(defaultCacheDuration);
        }

        return jwksRequest.getJsonWebKeys();
    }

    /**
     * @param urlJWKSFilePath
     * @param defaultCacheDuration
     * @return
     */
    public Key fetchPublicKey(final String urlJWKSFilePath, final long defaultCacheDuration)
        throws JoseException, IOException {
        final List<JsonWebKey> jsonWebKeys = fetchJsonWebKeys(urlJWKSFilePath, defaultCacheDuration);
        if (BeanUtils.isEmpty(jsonWebKeys)) {
            return null;
        }

        JsonWebKey jsonWebKey = jsonWebKeys.get(0);
        return jsonWebKey.getKey();
    }

    /**
     * Encodes Client ID and Client Secret
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    public static String encodeClientIdAndSecret(final String clientId, final String clientSecret) {
        LOGGER.debug("encodeClientIdAndSecret({}, {})", clientId, clientId);
        final Charset defaultCharset = Charset.defaultCharset();
        final byte[] dataBytes = (clientId.trim() + ":" + clientSecret.trim()).getBytes(defaultCharset);
        return new String(Base64.getEncoder().encode(dataBytes), defaultCharset);
    }

    /**
     * @param jwtRequest
     * @return
     */
    public static String getAudienceUrl(final JwtRequest jwtRequest) {
        return (jwtRequest.getServerBaseUrl() + jwtRequest.getPathSegment() + jwtRequest.getRealm());
    }

    /**
     * Generates the new JWT (Json Web Token).
     *
     * @param clientId
     * @param clientSecret
     * @param audience
     * @param expirationTimeInMinutes
     * @param withJwtId
     * @return
     */
    public static String jwtToken(final String clientId, final String clientSecret, String audience,
                                  int expirationTimeInMinutes, boolean withJwtId) {
        LOGGER.debug("+jwtToken({}, {}, {}, {})", clientId, audience, expirationTimeInMinutes, withJwtId);
        String jwtString;
        final JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setIssuedAt(NumericDate.now());
        jwtClaims.setExpirationTimeMinutesInTheFuture(expirationTimeInMinutes);
        jwtClaims.setSubject(clientId);
        jwtClaims.setIssuer(clientId);
        jwtClaims.setAudience(audience);
        if (withJwtId) {
            jwtClaims.setGeneratedJwtId();
        }

        try {
            Key key = new HmacKey(clientSecret.getBytes(JWTUtils.UTF_8));
            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(jwtClaims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setHeader(JWTUtils.JWS_HEADER_TYPE, JWTUtils.JWS_HEADER_TYPE_VALUE);
            jws.setKey(key);
            jws.setDoKeyValidation(false);
            jwtString = jws.getCompactSerialization();
        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            throw new RuntimeException("JWT Generation is failed", ex);
        }

        LOGGER.debug("-jwtToken(), jwtString={}", jwtString);
        return jwtString;
    }

    /**
     * @param jwtRequest
     * @return
     */
    public static String jwtToken(final JwtRequest jwtRequest) {
        return jwtToken(jwtRequest.getClientId(), jwtRequest.getClientSecret(), getAudienceUrl(jwtRequest),
                        jwtRequest.getExpirationTimeInMinutes(), jwtRequest.isWithJwtId());
    }

    /**
     * @param jwtToken
     * @return
     */
    public static boolean isTokenExpired(final String jwtToken) {
        try {
            // parse the JWS and
            final JWTClaimsSet jwtClaims = INSTANCE.jwtSigned(jwtToken).getJWTClaimsSet();
            return !jwtClaims.getExpirationTime().before(new Date());
        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }
}
