package com.devamatre.appsuite.core.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.devamatre.appsuite.core.entity.Recovery;
import com.devamatre.appsuite.core.Payload;
import com.google.gson.FieldNamingPolicy;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Rohtash Lakra
 * @created 5/7/20 3:10 PM
 */
public class JSONUtilsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(JSONUtilsTest.class);

    @Test
    public void testToJson() {
        Payload<String, String> payload = Payload.newBuilder()
            .ofPair("name", "Rohtash Lakra");
        String json = null;
        try {
            json = JSONUtils.toJson(payload);
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        LOGGER.info("json: {}", json);
        assertNotNull(json);
        assertTrue(json.contains("name"));
    }

    @Test
    public void testFromJson() {
        Payload<String, String> payload = Payload.newBuilder()
            .ofPair("firstName", "Rohtash")
            .ofPair("lastName", "Lakra");
        LOGGER.info("payload: {}", payload);
        String json;
        Payload<String, String> jsonPayload;
        try {
            json = JSONUtils.toJson(payload);
            LOGGER.info("json: {}", json);
            jsonPayload = JSONUtils.fromJson(json, Payload.class);
        } catch (IOException ex) {
            jsonPayload = null;
            LOGGER.error(ex.getLocalizedMessage(), ex);
            assertFalse(true);
        }
        assertNotNull(jsonPayload);
        LOGGER.info("jsonPayload: {}", jsonPayload);
        assertEquals(payload.get("firstName"), jsonPayload.get("firstName"));
        assertEquals(payload.get("lastName"), jsonPayload.get("lastName"));
    }

    @Test
    public void testToJSONString() {
        Payload<String, String> payload = Payload.newBuilder()
            .ofPair("firstName", "Rohtash")
            .ofPair("lastName", "Lakra");
        String json = JSONUtils.toJSONString(payload);
        LOGGER.info("json: {}", json);
        assertNotNull(json);
        assertTrue(json.contains("firstName"));
        assertTrue(json.contains("lastName"));
    }

    @Test
    public void testFromJSONString() {
        Payload<String, String> payload = Payload.newBuilder()
            .ofPair("firstName", "Rohtash")
            .ofPair("lastName", "Lakra");
        String json = JSONUtils.toJSONString(payload);
        LOGGER.info("json: {}", json);
        Payload<String, String> jsonPayload = JSONUtils.fromJSONString(json, Payload.class);
        LOGGER.info("jsonPayload: {}", jsonPayload);
        LOGGER.info("jsonPayload: {}", jsonPayload);
        assertEquals(payload.get("firstName"), jsonPayload.get("firstName"));
        assertEquals(payload.get("lastName"), jsonPayload.get("lastName"));
    }

    @Test
    public void testJson() {
        Payload<String, Payload> payload = Payload.newBuilder()
            .ofPair("name", Payload.newBuilder()
                .ofPair("firstName", "Rohtash Singh")
                .ofPair("lastName", "Lakra"));

        String json;
        Payload tempPayload = null;
        try {
            json = JSONUtils.toJson(payload);
            LOGGER.info("json: {}", json);
            tempPayload = JSONUtils.fromJson(json, Payload.class);
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        LOGGER.info("tempDictionary: {}", tempPayload);
        assertNotNull(tempPayload);
        Payload pairs = tempPayload.getPayload("name");
        LOGGER.info("pairs: {}", pairs);
        assertEquals(2, pairs.size());
        assertEquals("Rohtash Singh", pairs.get("firstName"));
        assertEquals("Lakra", pairs.get("lastName"));
    }

    @Test
    public void testSetFieldNamingPolicy() {
        Recovery recovery = new Recovery();
        recovery.setPointObjective("pointObjective");
        recovery.setTimeObjective("timeObjective");

        JSONUtils.INSTANCE.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        // FieldNamingPolicy.IDENTITY
        String jsonRecovery = JSONUtils.toJSONString(recovery);
        LOGGER.info("jsonRecovery: {}", jsonRecovery);
        assertNotNull(jsonRecovery);
        assertTrue(jsonRecovery.contains("pointObjective"));
        assertTrue(jsonRecovery.contains("timeObjective"));

        // FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
        JSONUtils.INSTANCE.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        jsonRecovery = JSONUtils.toJSONString(recovery);
        LOGGER.info("jsonRecovery: {}", jsonRecovery);
        assertNotNull(jsonRecovery);
        assertTrue(jsonRecovery.contains("point_objective"));
        assertTrue(jsonRecovery.contains("time_objective"));
    }

    @Test
    public void testSerializeNulls() {
        Recovery recovery = new Recovery();
        recovery.setPointObjective("pointObjective");
        recovery.setTimeObjective("timeObjective");

        JSONUtils.INSTANCE.serializeNulls();
        // FieldNamingPolicy.IDENTITY
        String jsonRecovery = JSONUtils.toJSONString(recovery);
        LOGGER.info("jsonRecovery: {}", jsonRecovery);
        assertNotNull(jsonRecovery);
        assertTrue(jsonRecovery.contains("pointObjective"));
        assertTrue(jsonRecovery.contains("timeObjective"));
    }

}
