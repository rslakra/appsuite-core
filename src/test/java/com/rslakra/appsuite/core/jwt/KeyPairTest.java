package com.rslakra.appsuite.core.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Rohtash Lakra
 * @created 3/13/20 9:16 AM
 */
public class KeyPairTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyPairTest.class);

    public static void main(String[] args) {
        KeyPairTest keyPairTest = new KeyPairTest();
        try {
            String path = System.getProperty("user.home") + "/Downloads/Temp";
//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair generatedKeyPair = keyGen.genKeyPair();
            LOGGER.debug("Generated Key Pair");
            keyPairTest.dumpKeyPair(generatedKeyPair);
            keyPairTest.saveKeyPair(path, generatedKeyPair);

            KeyPair loadedKeyPair = keyPairTest.loadKeyPair(path, "RSA");
            LOGGER.debug("Loaded Key Pair");
            keyPairTest.dumpKeyPair(loadedKeyPair);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        LOGGER.debug("Public Key: " + toHexString(pub.getEncoded()));

        PrivateKey priv = keyPair.getPrivate();
        LOGGER.debug("Private Key: " + toHexString(priv.getEncoded()));
    }

    private String toHexString(byte[] rawBytes) {
        StringBuilder hexBuilder = new StringBuilder();
        for (int i = 0; i < rawBytes.length; i++) {
            hexBuilder.append(Integer.toString((rawBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hexBuilder.toString();
    }

    public void saveKeyPair(String path, KeyPair keyPair) throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path + "/public.key");
        byte[] encoded = Base64.getEncoder().encode(x509EncodedKeySpec.getEncoded());
        fos.write(encoded);
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
            privateKey.getEncoded());
        fos = new FileOutputStream(path + "/private.key");
        encoded = Base64.getEncoder().encode(pkcs8EncodedKeySpec.getEncoded());
        fos.write(encoded);
        fos.close();
    }

    public KeyPair loadKeyPair(String path, String algorithm)
        throws IOException, NoSuchAlgorithmException,
               InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();
        encodedPublicKey = Base64.getDecoder().decode(encodedPublicKey);

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();
        encodedPrivateKey = Base64.getDecoder().decode(encodedPrivateKey);

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
            encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
            encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}
