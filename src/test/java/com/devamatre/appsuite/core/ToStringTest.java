package com.devamatre.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created 12/13/21 5:41 PM
 */
public class ToStringTest {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(ToStringTest.class);
    private static final String CLASS_NAME = ToStringTest.class.getSimpleName();
    private static final String PACKAGE_NAME = ToStringTest.class.getPackageName();
    private static final String CLASS_NAME_WITH_PACKAGE = ToStringTest.class.getName();

    /**
     * ToString
     *
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(ToStringTest.class).toString();
    }

    @Test
    public void testOfWithDelimiterAndPrefixAndSuffix() {
        String strObject = ToString.of(ToStringTest.class, ";", "{", "}")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " {firstName=Rohtash;lastName=Lakra}", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithExcludePackageWithDelimiterAndPrefixAndSuffix() {
        String strObject = ToString.of(ToStringTest.class, true, ";", "{", "}")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME + " {firstName=Rohtash;lastName=Lakra}", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithCustomDelimiterAndPrefixAndSuffix() {
        String strObject = ToString.of(ToStringTest.class, ";", "{", "}")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " {firstName=Rohtash;lastName=Lakra}", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithIncludePackage() {
        String strObject = ToString.of(ToStringTest.class, false, ";")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " <firstName=Rohtash;lastName=Lakra>", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithExcludePackage() {
        String strObject = ToString.of(ToStringTest.class, true, ";")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME + " <firstName=Rohtash;lastName=Lakra>", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithPrefixAndSuffix() {
        String strObject = ToString.of(ToStringTest.class, "[", "]")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " [firstName=Rohtash, lastName=Lakra]", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfWithDelimiter() {
        String strObject = ToString.of(ToStringTest.class, ":")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " <firstName=Rohtash:lastName=Lakra>", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfClass() {
        String strObject = ToString.of(ToStringTest.class)
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " <firstName=Rohtash, lastName=Lakra>", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfNoClass() {
        String strObject = ToString.of()
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals("<firstName=Rohtash, lastName=Lakra>", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testOfNoClassWithDelimiterAndPrefixAndSuffix() {
        String strObject = ToString.of(",", "{", "}")
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals("{firstName=Rohtash,lastName=Lakra}", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));

        strObject = ToString.of("&", ToString.EMPTY_STR, ToString.EMPTY_STR)
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals("firstName=Rohtash&lastName=Lakra", strObject);
        assertTrue(strObject.contains("firstName"));
        assertTrue(strObject.contains("lastName"));
    }

    @Test
    public void testAdd() {
        // test null value
        String strObject = ToString.of().add(null).toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertTrue(strObject.contains("<null>"));

        //test  value
        strObject = ToString.of().add("name").toString();
        LOGGER.debug(strObject);
        assertNotNull(strObject);
        assertTrue(strObject.contains("name"));
    }

    @Test
    public void testAddKeyValue() {
        // test null key
        String strObject = ToString.of()
            .add(null, "Rohtash Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertTrue(strObject.contains("<Rohtash Lakra>"));

        // test null value
        strObject = ToString.of()
            .add("name", null)
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertTrue(strObject.contains("<name>"));

        //test key/value
        strObject = ToString.of()
            .add("firstName", "Rohtash")
            .add("lastName", "Lakra")
            .toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertTrue(strObject.contains("firstName="));
        assertTrue(strObject.contains("lastName="));
    }

    @Test
    public void testToString() {
        String strObject = new ToStringTest().toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " <>", strObject);
    }

    @Test
    public void testToStringTestIncludePackageName() {
        String strObject = ToString.of(ToStringTest.class, false).toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME_WITH_PACKAGE + " <>", strObject);
    }

    @Test
    public void testToStringTestExcludePackageName() {
        String strObject = ToString.of(ToStringTest.class, true).toString();
        LOGGER.debug("strObject:{}", strObject);
        assertNotNull(strObject);
        assertEquals(CLASS_NAME + " <>", strObject);
    }
}
