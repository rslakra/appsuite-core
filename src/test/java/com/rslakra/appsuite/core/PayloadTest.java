package com.rslakra.appsuite.core;

import static com.rslakra.appsuite.core.BeanUtils.assertNonNull;
import static com.rslakra.appsuite.core.BeanUtils.isNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rohtash Lakra
 * @created 6/16/21 4:56 PM
 */
public class PayloadTest {

    /**
     * @param expected
     * @param actual
     * @param <K>
     * @param <V>
     */
    private <K, V> void assertPayload(Map<K, V> expected, Payload<K, V> actual) {
        if (isNotNull(expected) && isNotNull(actual)) {
            expected.keySet().forEach(key -> {
                assertEquals(expected.get(key), actual.get(key));
            });
        }
    }

    /**
     * Tests the newBuilder with parameters.
     */
    @Test
    public void testNewBuilderWithParams() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", "Rohtash");
        objectMap.put("age", 26);
        Payload<String, Object> payload = Payload.newBuilder(objectMap);
        assertNotNull(payload);
        assertPayload(objectMap, payload);
// assertEquals(objectMap.get("name"), payload.get("name"));
// assertEquals(objectMap.get("age"), payload.get("age"));
    }

    /**
     * Tests the newBuilder without parameters.
     */
    @Test
    public void testNewBuilder() {
        Payload<String, String> payload = Payload.newBuilder();
        assertNotNull(payload);
    }

    @Test
    public void testOfPair() {
        Payload<String, String> payload = Payload
            .newBuilder()
            .ofPair("name", "rohtash");
        assertEquals("rohtash", payload.get("name"));
    }

    @Test
    public void testWithObjects() {
        Payload<String, Object> payload = Payload
            .newBuilder()
            .withMessage("Rohtash")
            .withStatus("active")
            .withDeleted(false)
            .withCause(new Throwable("Generic payload!"));
        assertNonNull(payload);
        assertEquals("Rohtash", payload.get(Payload.MESSAGE));
        assertEquals("active", payload.get(Payload.STATUS));
        assertEquals(false, payload.get(Payload.DELETED));
        assertEquals("Generic payload!", payload.get(Payload.ERROR));
    }

    @Test
    public void testWithPayload() {

        Payload<String, Object> imagePayload = Payload
            .newBuilder()
            .ofPair("name", "trulli")
            .ofPair("url", "https://www.w3schools.com/html/pic_trulli.jpg")
            .ofPair("width", 825)
            .ofPair("height", 668);

        Payload<String, Payload> payload = Payload
            .newBuilder()
            .ofPair("imagePayload", imagePayload);

        assertNonNull(payload);
        assertTrue(payload.contains("imagePayload"));
        Payload<String, Object> tempImagePayload = payload.getPayload("imagePayload");
        assertNonNull(tempImagePayload);
        assertEquals("trulli", tempImagePayload.get("name"));
        assertEquals("https://www.w3schools.com/html/pic_trulli.jpg", tempImagePayload.get("url"));
        assertEquals(825, tempImagePayload.get("width"));
        assertEquals(668, tempImagePayload.get("height"));
    }

}
