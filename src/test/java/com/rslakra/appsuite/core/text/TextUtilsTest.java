package com.rslakra.appsuite.core.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @Created Jul 22, 2022 21:20:40
 */
public class TextUtilsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(TextUtilsTest.class);

    @Test
    public void testTrims() {
        String value = "Rohtash    Singh    Lakra  ";
        String result = TextUtils.trims(value);
        LOGGER.debug("result:{}", result);
        assertNotNull(result);
        assertEquals(result, "Rohtash Singh Lakra");
    }

    @Test
    public void testToStringWithString() {
        String[] values = new String[]{"Rohtash", "Singh", "Lakra"};
        String result = TextUtils.toString(values);
        LOGGER.debug("result:{}", result);
        assertNotNull(result);
        assertEquals(result, "[]{Rohtash, Singh, Lakra}");
    }

    @Test
    public void testToStringWithList() {
        List<String> valueList = Arrays.asList("Rohtash", "Singh", "Lakra");
        LOGGER.debug("valueList:{}", valueList);
// String result = TextUtils.toString(valueList);
// LOGGER.debug("result:{}", result);
// assertNotNull(result);
// assertEquals(result, "[]{Rohtash, Singh, Lakra}");
    }

    @Test
    public void testToStringWithMap() {
        Map<String, Object> valueMap = new HashMap();
        valueMap.put("firstName", "Rohtash");
        valueMap.put("middleName", "Singh");
        valueMap.put("lastName", "Lakra");
        LOGGER.debug("valueMap:{}", valueMap);
// String result = TextUtils.toString(valueMap);
// LOGGER.debug("result:{}", result);
// assertNotNull(result);
// assertEquals(result, "[keySet=null, values=null]");
    }
}
