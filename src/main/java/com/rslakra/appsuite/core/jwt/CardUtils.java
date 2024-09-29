package com.rslakra.appsuite.core.jwt;

import com.rslakra.appsuite.core.BeanUtils;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.PublicKey;
import java.util.Map;

/**
 * @author Rohtash Lakra
 * @created 4/15/20 10:58 AM
 */
public enum CardUtils {
    INSTANCE;

    public static final String JWKS_PUBLIC_KEY_FILENAME = "jwks.json";
    public static final String ATTR_CREDIT_CARD = "cc";
    public static final String ATTR_CVC = "cvc";
    public static final String ATTR_TOKEN_TYPE = "type";
    public static final String ATTR_IP = "ip";
    public static final String EXTERNAL_ID = "externalId";
    private static final String CONTENT_TYPE_APP_JSON = "application/json";

    public static final String API_PUBLIC_KEY = "apiPubKey";
    private PublicKey publicKey;
    private JwtConsumer jwtConsumer;

    /**
     * @param algorithm
     * @return
     */
    public final AlgorithmConstraints newWhitelistAlgorithmConstraints(final String algorithm) {
        return new AlgorithmConstraints(ConstraintType.WHITELIST, algorithm);
    }

    /**
     * @return
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     */
    public void setPublicKey(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @param jweAlgorithm
     * @param jweContentEncAlgorithm
     */
    public void setJwtConsumer(final String jweAlgorithm, final String jweContentEncAlgorithm) {
        jwtConsumer = new JwtConsumerBuilder()
            .setDecryptionKey(getPublicKey())
            .setJweAlgorithmConstraints(newWhitelistAlgorithmConstraints(jweAlgorithm))
            .setJweContentEncryptionAlgorithmConstraints(newWhitelistAlgorithmConstraints(jweContentEncAlgorithm))
            .setDisableRequireSignature()
            .build();
    }

    /**
     *
     */
    public void setJwtConsumer() {
        setJwtConsumer(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256,
                       ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
    }


    /**
     * @return
     */
    public JwtConsumer getJwtConsumer() {
        return jwtConsumer;
    }

    /**
     * @param jwtConsumer
     */
    public void setJwtConsumer(final JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
    }

    /**
     * @param type
     * @param cardNumber
     * @param cvc
     * @param ipAddress
     * @return
     */
    public JwtClaims newJwtClaim(final TokenType type, String cardNumber, String cvc, String ipAddress) {
        final JwtClaims jwtClaims = new JwtClaims();
        if (BeanUtils.isNotEmpty(cardNumber)) {
            jwtClaims.setClaim(ATTR_CREDIT_CARD, cardNumber);
        }

        if (BeanUtils.isNotEmpty(cvc)) {
            jwtClaims.setClaim(ATTR_CVC, cvc);
        }

        if (type != null) {
            jwtClaims.setClaim(ATTR_TOKEN_TYPE, type);
        } else {
            if (BeanUtils.isNotEmpty(cvc)) {
                jwtClaims.setClaim(ATTR_TOKEN_TYPE, TokenType.CREDIT_CARD);
            } else {
                jwtClaims.setClaim(ATTR_TOKEN_TYPE, TokenType.ACH);
            }
        }

        if (BeanUtils.isNotEmpty(ipAddress)) {
            jwtClaims.setClaim(ATTR_IP, ipAddress);
        }

        return jwtClaims;
    }

    /**
     * Gets an encrypted token for the claims
     *
     * @param type
     * @param cardNumber
     * @param cvc
     * @param ipAddress
     * @return
     * @throws JoseException
     */
    public static String newEncryptedToken(final TokenType type, String cardNumber, String cvc, String ipAddress)
        throws JoseException {
        final JwtClaims jwtClaims = INSTANCE.newJwtClaim(type, cardNumber, cvc, ipAddress);
        final JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        jwe.setKey(INSTANCE.getPublicKey());
        jwe.setKeyIdHeaderValue(API_PUBLIC_KEY);
        jwe.setContentTypeHeaderValue(CONTENT_TYPE_APP_JSON);
        jwe.setPayload(jwtClaims.toJson());

        return jwe.getCompactSerialization();
    }

    /**
     * Decrypts the token and gets claims
     *
     * @param cardToken
     * @return
     * @throws InvalidJwtException
     */
    private Map<String, Object> getClaimsFromToken(final String cardToken) throws InvalidJwtException {
        JwtClaims jwtClaims = jwtConsumer.processToClaims(cardToken);
        return jwtClaims.getClaimsMap();
    }

}
