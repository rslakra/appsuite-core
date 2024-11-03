package com.rslakra.appsuite.core;

import static com.rslakra.appsuite.core.BeanUtils.asType;
import static com.rslakra.appsuite.core.BeanUtils.copyProperties;
import static com.rslakra.appsuite.core.BeanUtils.deepCopyProperties;
import static com.rslakra.appsuite.core.BeanUtils.findEnumByClass;
import static com.rslakra.appsuite.core.BeanUtils.findMethodByName;
import static com.rslakra.appsuite.core.BeanUtils.getAllFields;
import static com.rslakra.appsuite.core.BeanUtils.getClassPath;
import static com.rslakra.appsuite.core.BeanUtils.getLength;
import static com.rslakra.appsuite.core.BeanUtils.hasBlanks;
import static com.rslakra.appsuite.core.BeanUtils.hasSetter;
import static com.rslakra.appsuite.core.BeanUtils.isArray;
import static com.rslakra.appsuite.core.BeanUtils.isAssignable;
import static com.rslakra.appsuite.core.BeanUtils.isBlank;
import static com.rslakra.appsuite.core.BeanUtils.isEmpty;
import static com.rslakra.appsuite.core.BeanUtils.isEnumType;
import static com.rslakra.appsuite.core.BeanUtils.isKindOf;
import static com.rslakra.appsuite.core.BeanUtils.isNotBlank;
import static com.rslakra.appsuite.core.BeanUtils.isNotEmpty;
import static com.rslakra.appsuite.core.BeanUtils.isNotNull;
import static com.rslakra.appsuite.core.BeanUtils.isNull;
import static com.rslakra.appsuite.core.BeanUtils.isPositive;
import static com.rslakra.appsuite.core.BeanUtils.isPrimitive;
import static com.rslakra.appsuite.core.BeanUtils.isSameType;
import static com.rslakra.appsuite.core.BeanUtils.isSimpleType;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOf;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfBigDecimal;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfCharSequence;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfCollection;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfDate;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfList;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfMap;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfSet;
import static com.rslakra.appsuite.core.BeanUtils.isTypeOfString;
import static com.rslakra.appsuite.core.BeanUtils.isZero;
import static com.rslakra.appsuite.core.BeanUtils.nextUuid;
import static com.rslakra.appsuite.core.BeanUtils.notEquals;
import static com.rslakra.appsuite.core.BeanUtils.partitionBySize;
import static com.rslakra.appsuite.core.BeanUtils.pathSegments;
import static com.rslakra.appsuite.core.BeanUtils.replaceUnderscoresWithDashes;
import static com.rslakra.appsuite.core.BeanUtils.separateCamelCase;
import static com.rslakra.appsuite.core.BeanUtils.toBytes;
import static com.rslakra.appsuite.core.BeanUtils.toSentenceCase;
import static com.rslakra.appsuite.core.BeanUtils.toTitleCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rslakra.appsuite.core.entity.Color;
import com.rslakra.appsuite.core.entity.Instance;
import com.rslakra.appsuite.core.enums.EntityStatus;
import com.rslakra.appsuite.core.enums.RoleType;
import com.rslakra.appsuite.core.text.TextUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Rohtash Lakra
 * @created 5/27/20 2:40 PM
 */
public class BeanUtilsTest {

    // LOGGER
    private final Logger LOGGER = LoggerFactory.getLogger(BeanUtilsTest.class);

    /**
     *
     */
    @Test
    public void testIsNull() {
        assertTrue(isNull(null));
        assertFalse(isNull(new Object()));
    }

    /**
     *
     */
    @Test
    public void testIsNotNull() {
        assertFalse(isNotNull(null));
        assertTrue(isNotNull(new Object()));
    }

    /**
     *
     */
    @Test
    public void testHasBlanks() {
        assertFalse(hasBlanks(null));
        assertFalse(hasBlanks(""));
        assertTrue(hasBlanks(" "));
        assertTrue(hasBlanks(" rsl"));
    }

    /**
     *
     */
    @Test
    public void testIsBlank() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank(" "));
        assertFalse(isBlank(" rsl"));
    }

    /**
     *
     */
    @Test
    public void testIsNotBlank() {
        assertFalse(isNotBlank(null));
        assertFalse(isNotBlank(""));
        assertFalse(isNotBlank(" "));
        assertTrue(isNotBlank(" rsl"));
    }

    /**
     *
     */
    @Test
    public void testIsArray() {
        assertFalse(isArray(null));
        assertTrue(isArray(new String[1]));
        assertTrue(isArray(new Integer[]{1, 2}));
        assertTrue(isArray(new Class[1]));
        assertTrue(isArray(Class[].class));
        assertFalse(isArray(String.class));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOf() {
        assertFalse(isTypeOf(null, Object.class));
        assertFalse(isTypeOf(null, Class.class));
        assertFalse(isTypeOf(null, String.class));
        assertFalse(isTypeOf(null, Integer.class));
        assertFalse(isTypeOf(null, List.class));
        assertFalse(isTypeOf(null, Map.class));

        assertTrue(isTypeOf(new Object(), Object.class));
        assertTrue(isTypeOf(new Object().getClass(), Class.class));
        assertTrue(isTypeOf(new String(), String.class));
        assertTrue(isTypeOf(Integer.valueOf("10"), Integer.class));
        assertTrue(isTypeOf(new ArrayList<>(), List.class));
        assertTrue(isTypeOf(new HashMap<>(), Map.class));
    }

    /**
     *
     */
    @Test
    public void testIsAssignableFrom() {
        assertFalse(isAssignable(null, Object.class));
        assertFalse(isAssignable(null, Class.class));
        assertFalse(isAssignable(null, String.class));
        assertFalse(isAssignable(null, Integer.class));
        assertFalse(isAssignable(null, List.class));
        assertFalse(isAssignable(null, Map.class));
        assertFalse(isAssignable(new Object(), Object.class));
        assertTrue(isAssignable(new Object().getClass(), Object.class));
        assertTrue(isAssignable(Object.class, Object.class));
        assertTrue(isAssignable(Class.class, Class.class));
        assertTrue(isAssignable(String.class, CharSequence.class));
        assertTrue(isAssignable(Integer.class, Number.class));
        assertTrue(isAssignable(ArrayList.class, List.class));
        assertTrue(isAssignable(Vector.class, List.class));
        assertTrue(isAssignable(AbstractList.class, List.class));
        assertTrue(isAssignable(HashMap.class, Map.class));
        assertTrue(isAssignable(ConcurrentHashMap.class, Map.class));
        assertTrue(isAssignable(Dictionary.class, Dictionary.class));
        assertTrue(isAssignable(Hashtable.class, Dictionary.class));
    }

    /**
     *
     */
    @Test
    public void testIsKindOf() {
        assertFalse(isKindOf(null, Object.class));
        assertFalse(isKindOf(null, Class.class));
        assertFalse(isKindOf(null, String.class));
        assertFalse(isKindOf(null, Integer.class));
        assertFalse(isKindOf(null, List.class));
        assertFalse(isKindOf(null, Map.class));
        assertTrue(isKindOf(new Object(), Object.class));
        assertTrue(isKindOf(new Object().getClass(), Object.class));
        assertTrue(isKindOf(Object.class, Object.class));
        assertTrue(isKindOf(Class.class, Class.class));
        assertTrue(isKindOf(String.class, CharSequence.class));
        assertTrue(isKindOf(Integer.class, Number.class));

        assertTrue(isKindOf(ArrayList.class, List.class));
        assertTrue(isKindOf(Vector.class, List.class));
        assertTrue(isKindOf(AbstractList.class, List.class));

        assertTrue(isKindOf(AbstractMap.class, Map.class));
        assertTrue(isKindOf(Hashtable.class, Map.class));
        assertTrue(isKindOf(HashMap.class, Map.class));
        assertTrue(isKindOf(ConcurrentHashMap.class, Map.class));
        assertTrue(isKindOf(Dictionary.class, Dictionary.class));
        assertTrue(isKindOf(Hashtable.class, Dictionary.class));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOfMap() {
        assertFalse(isTypeOfMap(null));
        assertFalse(isTypeOfMap(Dictionary.class));
        assertTrue(isTypeOfMap(AbstractMap.class));
        assertTrue(isTypeOfMap(Map.class));
        assertFalse(isTypeOfMap(new Object()));
        assertTrue(isTypeOfMap(new HashMap<>()));
        assertTrue(isTypeOfMap(new Hashtable<>()));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOfList() {
        assertFalse(isTypeOfList(null));
        assertTrue(isTypeOfList(AbstractList.class));
        assertTrue(isTypeOfList(List.class));
        assertFalse(isTypeOfList(new Object()));
        assertTrue(isTypeOfList(new ArrayList<>()));
        assertTrue(isTypeOfList(new Vector<>()));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOfSet() {
        assertFalse(isTypeOfSet(null));
        assertTrue(isTypeOfSet(AbstractSet.class));
        assertTrue(isTypeOfSet(Set.class));
        assertFalse(isTypeOfSet(new Object()));
        assertTrue(isTypeOfSet(new HashSet<>()));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOfCollection() {
        assertFalse(isTypeOfCollection(null));
        assertFalse(isTypeOfCollection(new Object()));

        // list
        assertTrue(isTypeOfCollection(AbstractList.class));
        assertTrue(isTypeOfCollection(List.class));
        assertTrue(isTypeOfCollection(new ArrayList<>()));
        assertTrue(isTypeOfCollection(new Vector<>()));
        // set
        assertTrue(isTypeOfCollection(AbstractSet.class));
        assertTrue(isTypeOfCollection(Set.class));
        assertTrue(isTypeOfCollection(new HashSet<>()));
        //collection
        assertTrue(isTypeOfCollection(AbstractCollection.class));
        assertTrue(isTypeOfCollection(Collection.class));
    }

    /**
     *
     */
    @Test
    public void testIsTypeOfCharSequence() {
        assertFalse(isTypeOfCharSequence(null));
        assertFalse(isTypeOfCharSequence(new Object()));
        assertTrue(isTypeOfCharSequence(""));
        assertTrue(isTypeOfCharSequence(new String()));
        assertTrue(isTypeOfCharSequence(CharSequence.class));
        assertTrue(isTypeOfCharSequence(String.class));
        assertTrue(isTypeOfCharSequence(new StringBuilder()));
        assertTrue(isTypeOfCharSequence(new StringBuffer()));
    }

    /**
     * Tests <code>isTypeOfString</code>() method.
     */
    @Test
    public void testIsTypeOfString() {
        assertFalse(isTypeOfString(null));
        assertFalse(isTypeOfString(new Object()));
        assertTrue(isTypeOfString(""));
        assertTrue(isTypeOfString(new String()));
        assertTrue(isTypeOfString(String.class));
        assertFalse(isTypeOfString(new StringBuilder()));
        assertFalse(isTypeOfString(new StringBuffer()));
    }

    /**
     * Tests <code>isTypeOfBigDecimal</code>() method.
     */
    @Test
    public void testIsTypeOfBigDecimal() {
        assertFalse(isTypeOfBigDecimal(null));
        assertFalse(isTypeOfBigDecimal(new Object()));
        assertFalse(isTypeOfBigDecimal(""));
        assertTrue(isTypeOfBigDecimal(BigDecimal.ZERO));
        assertTrue(isTypeOfBigDecimal(BigDecimal.class));
        assertFalse(isTypeOfBigDecimal(new Integer(0)));
    }

    /**
     * Tests <code>isTypeOfDate</code>() method.
     */
    @Test
    public void testIsTypeOfDate() {
        assertFalse(isTypeOfDate(null));
        assertFalse(isTypeOfDate(new Object()));
        assertFalse(isTypeOfDate(""));
        assertTrue(isTypeOfDate(new Date()));
        assertFalse(isTypeOfDate(new Integer(0)));
    }

    /**
     * Tests <code>asType</code>() method.
     */
    @Test
    public void testAsType() {
        // compare number
        String text = "1601";
        assertEquals(new BigDecimal(text), asType(text, BigDecimal.class));
        assertEquals(new BigInteger(text), asType(text, BigInteger.class));
        assertEquals(Double.valueOf(text), asType(text, Double.class));
        assertEquals(Float.valueOf(text), asType(text, Float.class));
        assertEquals(Long.valueOf(text), asType(text, Long.class));
        assertEquals(Integer.valueOf(text), asType(text, Integer.class));

        // compare date
        text = "2023/01/16";
        assertEquals(new Date(text), asType(text, Date.class));

        // compare small number
        text = "16";
        assertEquals(Short.valueOf(text), asType(text, Short.class));
        assertEquals(Integer.valueOf(text), asType(text, Integer.class));
        assertEquals(Long.valueOf(text), asType(text, Long.class));

        // compare boolean
        text = "true";
        assertTrue(asType(text, Boolean.class));
        assertFalse(asType(null, Boolean.class));
    }

    /**
     *
     */
    @Test
    public void testGetLength() {
        // Object
        assertEquals(0, getLength(null));
        assertEquals(0, getLength(new Object()));
        assertEquals(0, getLength(new BeanUtilsTest()));

        // String
        assertEquals(0, getLength(""));
        assertEquals(1, getLength(" "));
        assertEquals(3, getLength("rsl"));

        // Array
        assertEquals(0, getLength(new Class[0]));
        assertEquals(0, getLength(new Object[0]));
        assertEquals(0, getLength(new String[0]));
        assertEquals(0, getLength(new Map[0]));
        assertEquals(0, getLength(new List[0]));
        assertEquals(0, getLength(new Set[0]));
        assertEquals(0, getLength(new Collection[0]));

        // class types
        assertEquals(1, getLength(new Class[1]));
        assertEquals(2, getLength(new Object[2]));
        assertEquals(1, getLength(new String[1]));
        assertEquals(2, getLength(new String[]{"Rohtash", "Lakra"}));
        assertEquals(2, getLength(new Integer[]{1, 2}));
        assertEquals(2, getLength(Arrays.asList("Rohtash", "Singh")));

        // primitive types
        assertEquals(2, getLength(new byte[]{1, 2}));
        assertEquals(2, getLength(new short[]{1, 2}));
        assertEquals(2, getLength(new boolean[]{Boolean.TRUE, Boolean.FALSE}));
    }

    /**
     *
     */
    @Test
    public void testIsEmpty() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty(" "));
        assertFalse(isEmpty(" rsl"));
        assertFalse(isEmpty(new Object()));
        assertTrue(isEmpty(new Class[0]));
        assertTrue(isEmpty(new Object[0]));
        assertTrue(isEmpty(new String[0]));
        assertTrue(isEmpty(new Map[0]));
        assertTrue(isEmpty(new List[0]));
        assertTrue(isEmpty(new Set[0]));
    }

    /**
     *
     */
    @Test
    public void testIsNotEmpty() {
        assertFalse(isNotEmpty(null));
        assertFalse(isNotEmpty(""));
        assertTrue(isNotEmpty(" "));
        assertTrue(isNotEmpty(" rsl"));
        assertTrue(isNotEmpty(new String("Lakra")));
        assertTrue(isNotEmpty(new Object()));
        assertTrue(isNotEmpty(new Object(), new String("RS")));
        assertTrue(isNotEmpty(new Class[]{BeanUtils.class}));
        assertTrue(isNotEmpty(new Class[1], new Class[1]));
        assertFalse(isNotEmpty(new Object[1]));
        assertFalse(isNotEmpty(new String[1]));
        assertFalse(isNotEmpty(new Map[1]));
        assertFalse(isNotEmpty(new HashMap<>()));
        assertFalse(isNotEmpty(new Hashtable<>()));
        assertFalse(isNotEmpty(new List[1]));
        assertFalse(isNotEmpty(new Set[1]));
    }

    /**
     *
     */
    @Test
    public void testAssertNonNull() {
        try {
            BeanUtils.assertNonNull(null, "Object should not be null!");
            assertTrue(false);
        } catch (NullPointerException ex) {
            assertTrue(true);
        }

        BeanUtils.assertNonNull(new Object());
        assertTrue(true);
    }

    @Test
    public void testIsPrimitive() {
        assertTrue(isPrimitive(Boolean.class));
        assertTrue(isPrimitive(Byte.class));
        assertTrue(isPrimitive(Character.class));
        assertTrue(isPrimitive(Short.class));
        assertTrue(isPrimitive(Integer.class));
        assertTrue(isPrimitive(Long.class));
        assertTrue(isPrimitive(Float.class));
        assertTrue(isPrimitive(Double.class));
        assertTrue(isPrimitive(Void.class));
        assertFalse(isPrimitive(Enum.class));
    }

    @Test
    public void testIsEnumType() {
        assertTrue(isEnumType(BeanUtils.class));
        assertFalse(isEnumType(Void.class));
    }

    @Test
    public void testIsSimpleType() {
        assertTrue(isSimpleType(CharSequence.class));
        assertTrue(isSimpleType(Number.class));
        assertTrue(isSimpleType(Date.class));
        assertFalse(isSimpleType(Void.class));
    }

    @Test
    public void testIsSameType() {
        assertTrue(isSameType(Class.class));
        assertTrue(isSameType(Locale.class));
        assertTrue(isSameType(URI.class));
        assertTrue(isSameType(URL.class));
        assertFalse(isSameType(Void.class));
    }

    @Test
    public void testIsSimpleValueTyp() {
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(URL.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(BeanUtils.class));
        assertFalse(BeanUtils.INSTANCE.isSimpleValueType(List.class));
    }

    @Test
    public void testIsSimpleProperty() {
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(CharSequence.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(Number.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(Date.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(URI.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(URL.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(Locale.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(Void.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(Class.class));
        assertFalse(BeanUtils.INSTANCE.isSimpleProperty(Objects.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleProperty(BeanUtils.class));
    }

    /**
     *
     */
    @Test
    public void testToTitleCase() {
        assertEquals(BeanUtils.EMPTY_STR, toTitleCase(null));
        assertEquals("Rohtash", toTitleCase("rohtash"));
        assertEquals("Rohtash Lakra", toTitleCase("rohtash Lakra"));
    }

    /**
     *
     */
    @Test
    public void testPathSegments() {
        assertEquals(BeanUtils.EMPTY_STR, pathSegments(BeanUtils.EMPTY_STR));
        assertEquals("rohtash", pathSegments("rohtash"));
        assertEquals("rohtash/lakra", pathSegments("rohtash", "lakra"));
    }

    /**
     *
     */
    @Test
    public void testNextUuid() {
        String nextUuid = nextUuid();
        LOGGER.debug("nextUuid:{}", nextUuid);
        assertNotNull(nextUuid);
    }

    /**
     *
     */
    @Test
    public void testEquals() {
        assertTrue(BeanUtils.equals(null, null));
        Object leftObject = new Object();
        Object rightObject = new Object();
        Object lastObject = rightObject;
        assertFalse(BeanUtils.equals(leftObject, null));
        assertFalse(BeanUtils.equals(null, rightObject));
        assertFalse(BeanUtils.equals(leftObject, rightObject));
        assertTrue(BeanUtils.equals(lastObject, rightObject));
        assertFalse(BeanUtils.equals(lastObject, leftObject));
        String leftString = new String();
        String rightString = new String();
        String lastString = rightString;
        assertFalse(BeanUtils.equals(leftString, null));
        assertFalse(BeanUtils.equals(null, rightString));
        assertTrue(BeanUtils.equals(leftString, rightString));
        assertTrue(BeanUtils.equals(lastString, rightString));

        assertTrue(BeanUtils.equals("", ""));
        assertTrue(BeanUtils.equals(Integer.valueOf(10), Integer.valueOf(10)));
        assertTrue(BeanUtils.equals("rohtash", String.valueOf("rohtash")));
    }

    /**
     *
     */
    @Test
    public void testNotEquals() {
        assertFalse(notEquals(null, null));
        Object leftObject = new Object();
        Object rightObject = new Object();
        Object lastObject = rightObject;
        assertTrue(notEquals(leftObject, null));
        assertTrue(notEquals(null, rightObject));
        assertTrue(notEquals(leftObject, rightObject));
        assertFalse(notEquals(lastObject, rightObject));
        assertTrue(notEquals(lastObject, leftObject));
        String leftString = new String();
        String rightString = new String();
        String lastString = rightString;
        assertTrue(notEquals(leftString, null));
        assertTrue(notEquals(null, rightString));
        assertFalse(notEquals(leftString, rightString));
        assertFalse(notEquals(lastString, rightString));

        assertFalse(notEquals("", ""));
        assertFalse(notEquals(Integer.valueOf(10), Integer.valueOf(10)));
        assertFalse(notEquals("rohtash", String.valueOf("rohtash")));
    }

    /**
     *
     */
    @Test
    public void testToBytes() {
        assertNotNull(toBytes(TextUtils.NULL));
    }

    /**
     * @return
     */
    private static Stream<Arguments> classPathWithClassNameData() {
        return Stream.of(
            Arguments.of(null, false, BeanUtils.EMPTY_STR),
            Arguments.of(null, true, BeanUtils.EMPTY_STR),
            Arguments.of(BeanUtilsTest.class, false, getClassPath(BeanUtilsTest.class)),
            Arguments.of(BeanUtilsTest.class, true, getClassPath(BeanUtilsTest.class, true))
        );
    }

    /**
     * @param classType
     * @param withClassName
     * @param expectedPath
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("classPathWithClassNameData")
    public <T> void testGetClassPathWithClassName(Class<T> classType, boolean withClassName, String expectedPath) {
        String classPath = getClassPath(classType, withClassName);
        assertEquals(expectedPath, classPath);
    }

    /**
     * @return
     */
    private static Stream<Arguments> classPathWithClassNameAndPathsData() {
        return Stream.of(
            Arguments.of(null, false, null, BeanUtils.EMPTY_STR),
            Arguments.of(null, false, new String[]{}, BeanUtils.EMPTY_STR),
            Arguments.of(null, false, new String[]{"data"}, "data"),

            Arguments.of(null, true, null, BeanUtils.EMPTY_STR),
            Arguments.of(null, true, new String[]{}, BeanUtils.EMPTY_STR),
            Arguments.of(null, true, new String[]{"data"}, "data"),

            Arguments.of(BeanUtilsTest.class, false, null, getClassPath(BeanUtilsTest.class)),
            Arguments.of(BeanUtilsTest.class, false, new String[]{}, getClassPath(BeanUtilsTest.class)),
            Arguments.of(BeanUtilsTest.class, false, new String[]{"data"},
                         getClassPath(BeanUtilsTest.class) + "/data"),

            Arguments.of(BeanUtilsTest.class, true, null, getClassPath(BeanUtilsTest.class, true)),
            Arguments.of(BeanUtilsTest.class, true, new String[]{}, getClassPath(BeanUtilsTest.class, true)),
            Arguments.of(BeanUtilsTest.class, true, new String[]{"data"},
                         getClassPath(BeanUtilsTest.class, true) + "/data")
        );
    }

    /**
     * @param classType
     * @param withClassName
     * @param pathComponents
     * @param expectedPath
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("classPathWithClassNameAndPathsData")
    public <T> void testGetClassPathWithClassNameAndPaths(Class<T> classType, boolean withClassName,
                                                          String[] pathComponents, String expectedPath) {
        String classPath = getClassPath(classType, withClassName, pathComponents);
        assertEquals(expectedPath, classPath);
    }

    /**
     * @return
     */
    private static Stream<Arguments> classPathWithPathComponentsData() {
        return Stream.of(
            Arguments.of(null, (String[]) null, BeanUtils.EMPTY_STR),
            Arguments.of(null, new String[]{}, BeanUtils.EMPTY_STR),
            Arguments.of(null, new String[]{"data"}, "data"),

            Arguments.of(BeanUtilsTest.class, null, getClassPath(BeanUtilsTest.class)),
            Arguments.of(BeanUtilsTest.class, new String[]{}, getClassPath(BeanUtilsTest.class)),
            Arguments.of(BeanUtilsTest.class, new String[]{"data"},
                         getClassPath(BeanUtilsTest.class) + "/data"),
            Arguments.of(BeanUtilsTest.class, new String[]{"first", "last"},
                         getClassPath(BeanUtilsTest.class) + "/first/last")
        );
    }

    /**
     * @param classType
     * @param pathComponents
     * @param expectedPath
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("classPathWithPathComponentsData")
    public <T> void testGetClassPathWithPathComponents(Class<T> classType, String[] pathComponents,
                                                       String expectedPath) {
        String classPath = getClassPath(classType, pathComponents);
        assertEquals(expectedPath, classPath);
    }

    /**
     * @return
     */
    private static Stream<Arguments> partitionListBySizeData() {
        return Stream.of(
            Arguments.of(null, 4, 0),
            Arguments.of(Arrays.asList(), 4, 0),
            Arguments.of(Arrays.asList(1, 2, 3), 3, 1),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 0, 0),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 1, 9),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 2, 5),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 3, 3),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 4, 3),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 5, 2),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 6, 2),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 9, 1),
            Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 10, 1)
        );
    }

    /**
     * @param listIntegers
     * @param partitionSize
     * @param expectedPartitionSize
     */
    @ParameterizedTest
    @MethodSource("partitionListBySizeData")
    public void testPartitionListBySizeData(List<Integer> listIntegers, int partitionSize, int expectedPartitionSize) {
        List<List<Integer>> partitionList = partitionBySize(listIntegers, partitionSize);
        LOGGER.debug("partitionList size:{}, elements:{}", partitionList.size(), partitionList);
        assertNotNull(partitionList);
        assertEquals(partitionList.size(), expectedPartitionSize);
        if (expectedPartitionSize == 0) {
            assertTrue(partitionList.isEmpty());
        }
    }

    /**
     * Data Set for partition testing.
     *
     * @return
     */
    private static Stream<Arguments> partitionSetBySizeData() {
        return Stream.of(
            Arguments.of(null, 4, 0),
            Arguments.of(Sets.asSet(), 4, 0),
            Arguments.of(Sets.asSet(1, 2, 3), 3, 1),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 0, 0),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 1, 9),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 2, 5),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 3, 3),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 4, 3),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 5, 2),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 6, 2),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 9, 1),
            Arguments.of(Sets.asSet(1, 2, 3, 4, 5, 6, 7, 8, 9), 10, 1)
        );
    }

    /**
     * @param setInts
     * @param partitionSize
     * @param expectedPartitionSize
     */
    @ParameterizedTest
    @MethodSource("partitionSetBySizeData")
    public void testPartitionSetBySize(Set<Integer> setInts, int partitionSize, int expectedPartitionSize) {
        Set<Set<Integer>> partitionSet = partitionBySize(setInts, partitionSize);
        LOGGER.debug("partitionSet size:{}, elements:{}", partitionSet.size(), partitionSet);
        assertNotNull(partitionSet);
        assertEquals(partitionSet.size(), expectedPartitionSize);
        if (expectedPartitionSize == 0) {
            assertTrue(partitionSet.isEmpty());
        }
    }

    /**
     *
     */
    @Test
    public void testFindEnumByClass() {
        assertNotNull(findEnumByClass(EntityStatus.class, "active"));
        assertNotNull(findEnumByClass(EntityStatus.class, "inactive"));
        assertNotNull(findEnumByClass(EntityStatus.class, "deleted"));
        assertNull(findEnumByClass(EntityStatus.class, "fake"));
        assertNotNull(findEnumByClass(RoleType.class, "admin"));
        assertNotNull(findEnumByClass(RoleType.class, "user"));
    }

    /**
     *
     */
    @Test
    public void testIsZero() {
        assertEquals(true, isZero(BigDecimal.ZERO));
        assertEquals(false, isZero(BigDecimal.TEN));
        assertEquals(false, isZero(BigDecimal.TEN.negate()));
    }

    /**
     *
     */
    @Test
    public void testIsPositive() {
        assertEquals(true, isPositive(BigDecimal.TEN));
        assertEquals(false, isPositive(BigDecimal.TEN.negate()));
        assertEquals(false, isPositive(BigDecimal.ZERO));
    }

    /**
     * @throws NoSuchMethodException
     */
    @Test
    public void testIsGetter() throws NoSuchMethodException {
        BeanUtils beanUtils = BeanUtils.INSTANCE;
        assertNotNull(beanUtils);
        Method[] methods = beanUtils.getClass().getMethods();
        assertNotNull(methods);
        // Method methodGetter = beanUtils.getClass().getMethod("isClassPropertyDescriptor");
        // assertNotNull(methodGetter);
        // assertTrue(BeanUtils.isGetter(methodGetter));
        // assertEquals("isClassPropertyDescriptor", methodGetter.getName());
        // Method methodIsActive = beanUtils.getClass().getMethod("isActive");
        // assertTrue(BeanUtils.isGetter(methodIsActive));
    }

    /**
     * @throws NoSuchMethodException
     */
    @Test
    public void testIsSetter() throws NoSuchMethodException {
        BeanUtils beanUtils = BeanUtils.INSTANCE;
        assertNotNull(beanUtils);
        Method[] methods = beanUtils.getClass().getMethods();
        assertNotNull(methods);
        Method
            methodSetter =
            beanUtils.getClass().getMethod("setBeanProperty", PropertyDescriptor.class, Object.class, Object.class);
        assertNotNull(methodSetter);
        // assertTrue(BeanUtils.isSetter(methodSetter));
        // assertEquals("setBeanProperty", methodSetter.getName());
        // Method methodSetActive = beanUtils.getClass().getMethod("setActive", boolean.class);
        // assertTrue(BeanUtils.isSetter(methodSetActive));
    }

    //------------

    @Test
    public void testFindMethodByName() {
        Method getByMethodName = findMethodByName(BeanUtils.class, "isCopyOnlyNonNullValues");
        assertNotNull(getByMethodName);
        assertEquals("isCopyOnlyNonNullValues", getByMethodName.getName());
    }

    @Test
    public void testHasSetter() {
        assertEquals(true, hasSetter(BeanUtils.class, "setCopyOnlyNonNullValues", boolean.class));
    }

    @Test
    public void testGetReturnType() {
        PropertyDescriptor propertyDescriptor = null;
        assertEquals(null, BeanUtils.INSTANCE.getReturnType(propertyDescriptor));
        propertyDescriptor = BeanUtils.INSTANCE.findByPropertyName(BeanUtils.class, "copyOnlyNonNullValues");
        assertNotNull(propertyDescriptor);
        assertEquals("copyOnlyNonNullValues", propertyDescriptor.getName());
        assertEquals(boolean.class, BeanUtils.INSTANCE.getReturnType(propertyDescriptor));
    }

    @Test
    public void testEnsurePublic() {
        Method getByMethodName = findMethodByName(BeanUtils.class, "isCopyOnlyNonNullValues");
        assertNotNull(getByMethodName);
        BeanUtils.INSTANCE.ensurePublic(getByMethodName);
    }

    @Test
    public void testFindByPropertyName() {
        PropertyDescriptor
            propertyDescriptor =
            BeanUtils.INSTANCE.findByPropertyName(BeanUtils.class, "copyOnlyNonNullValues");
        assertNotNull(propertyDescriptor);
        assertEquals("copyOnlyNonNullValues", propertyDescriptor.getName());
    }

    @Test
    public void testGetProperties() {
        Collection<PropertyDescriptor> beanProperties = BeanUtils.INSTANCE.getProperties(BeanUtils.class);
        assertNotNull(beanProperties);
        assertEquals(2, beanProperties.size());
    }

    @Test
    public void testGetAllFields() {
        List<Field> allFields = getAllFields(BeanUtils.class);
        assertNotNull(allFields);
        assertEquals(23, allFields.size());
    }

    @Test
    public void testReadObjectProperty() {
        PropertyDescriptor
            propertyDescriptor =
            BeanUtils.INSTANCE.findByPropertyName(BeanUtils.class, "copyOnlyNonNullValues");
        assertNotNull(propertyDescriptor);
        assertEquals("copyOnlyNonNullValues", propertyDescriptor.getName());
        BeanUtils beanUtils = BeanUtils.INSTANCE;
        Object propertyDescriptorObject = BeanUtils.INSTANCE.readObjectProperty(propertyDescriptor, beanUtils);
        assertNotNull(propertyDescriptorObject);
    }

    @Test
    public void testSetBeanProperty() {
        PropertyDescriptor
            copyOnlyNonNullValues =
            BeanUtils.INSTANCE.findByPropertyName(BeanUtils.class, "copyOnlyNonNullValues");
        assertNotNull(copyOnlyNonNullValues);
        assertEquals("copyOnlyNonNullValues", copyOnlyNonNullValues.getName());
        BeanUtils.INSTANCE.setBeanProperty(copyOnlyNonNullValues, BeanUtils.INSTANCE, true);
    }

    @Test
    public void testIsSimpleValueType() {
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(CharSequence.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(Number.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(Date.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(URI.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(URL.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(Locale.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(Class.class));
        assertTrue(BeanUtils.INSTANCE.isSimpleValueType(BeanUtils.class));
    }

    @Test
    public void testNewInstance() throws ReflectiveOperationException {
        BeanUtilsTest beanUtilsTest = BeanUtils.newInstance(BeanUtilsTest.class);
        assertNotNull(beanUtilsTest);
    }

    /**
     * @param expected
     * @param target
     */
    private void assertInstance(Instance expected, Instance target) {
        LOGGER.debug("+assertInstance({}, {})", expected, target);
        assertNotNull(expected);
        assertNotNull(target);
        assertEquals(expected.getFamily(), target.getFamily());
        assertEquals(expected.getGeneration(), target.getGeneration());
        assertEquals(expected.getProperties(), target.getProperties());
        assertEquals(expected.getSize(), target.getSize());
        LOGGER.debug("-assertInstance()");
    }

    /**
     * Copy Properties
     */
    @Test
    public void testCopyProperties() {
        Instance sourceInstance = new Instance();
        sourceInstance.setFamily("iPhone");
        sourceInstance.setGeneration("4th");
        sourceInstance.setProperties("name");
        sourceInstance.setSize("10");
        LOGGER.debug("sourceInstance: {}", sourceInstance);

        Instance targetInstance = new Instance();
        copyProperties(sourceInstance, targetInstance);
        LOGGER.debug("targetInstance: {}", targetInstance);
        assertNotNull(targetInstance);
        assertEquals(sourceInstance.getFamily(), targetInstance.getFamily());
        assertEquals(sourceInstance.getGeneration(), targetInstance.getGeneration());
        assertEquals(sourceInstance.getProperties(), targetInstance.getProperties());
        assertEquals(sourceInstance.getSize(), targetInstance.getSize());
    }

    /**
     * Testing Bean Class
     */
    @Data
    private class TestBean {

        private String name;
        private Instance instance;
        private List<Color> colors;
    }

    /**
     * Copy Properties
     */
    @Test
    public void testDeepCopyProperties() {
        Instance instance = new Instance();
        instance.setFamily("iPhone");
        instance.setGeneration("4th");
        instance.setProperties("name");
        instance.setSize("10");
        LOGGER.debug("instance: {}", instance);

        TestBean sourceInstance = new TestBean();
        assertNotNull(sourceInstance);
        sourceInstance.setName("sourceInstance");
        sourceInstance.setInstance(instance);
        Color redColor = new Color(1601L, "Red");
        Color whiteColor = new Color(1602L, "White");
        sourceInstance.setColors(Arrays.asList(redColor, whiteColor));
        LOGGER.debug("sourceInstance: {}", sourceInstance);

        TestBean targetInstance = new TestBean();
        deepCopyProperties(sourceInstance, targetInstance);
        LOGGER.debug("targetInstance: {}", targetInstance);
        assertNotNull(targetInstance);
        assertEquals(sourceInstance.getName(), targetInstance.getName());
        assertInstance(targetInstance.getInstance(), sourceInstance.getInstance());
        assertNotNull(targetInstance.getColors());
        assertEquals(targetInstance.getColors().size(), targetInstance.getColors().size());
    }

    /**
     * CamelCase Separator Testing Data.
     *
     * @return
     */
    private static Stream<Arguments> separateCamelCaseData() {
        return Stream.of(
            Arguments.of(null, ",", null),
            Arguments.of("FirstName", null, "FirstName"),
            Arguments.of("FirstName", "-", "First-Name"),
            Arguments.of("FirstName", ":", "First:Name"),
            Arguments.of("FirstName", "#", "First#Name"),
            Arguments.of("FirstName", "$", "First$Name"),
            Arguments.of("FirstName", ";", "First;Name")
        );
    }

    /**
     * @param charSequence
     * @param separator
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("separateCamelCaseData")
    public void testSeparateCamelCase(final CharSequence charSequence, final String separator, CharSequence expected) {
        LOGGER.info("+testSeparateCamelCase({}, {}, {})", charSequence, separator, expected);
        String separatedCamelCase = separateCamelCase(charSequence, separator);
        LOGGER.info("separatedCamelCase: {}", separatedCamelCase);
        assertEquals(expected, separatedCamelCase);
        LOGGER.info("-testSeparateCamelCase()");
    }

    /**
     * SentenceCase Testing Data.
     *
     * @return
     */
    private static Stream<Arguments> sentenceCaseData() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("FirstName", "FirstName"),
            Arguments.of("lastName", "LastName"),
            Arguments.of("Lakra", "Lakra"),
            Arguments.of("lakra", "Lakra"),
            Arguments.of("LAKRA", "LAKRA"),
            Arguments.of("lAKRA", "LAKRA"),
            Arguments.of("LakRA", "LakRA"),
            Arguments.of("16 $ firstName", "16 $ FirstName")
        );
    }

    /**
     * @param text
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("sentenceCaseData")
    public void testToSentenceCase(CharSequence text, CharSequence expected) {
        LOGGER.info("+testToSentenceCase({}, {}, {})", text, expected);
        String sentenceCase = toSentenceCase(text);
        LOGGER.info("sentenceCase: {}", sentenceCase);
        assertEquals(expected, sentenceCase);
        LOGGER.info("-testToSentenceCase()");
    }

    /**
     * SentenceCase Testing Data.
     *
     * @return
     */
    private static Stream<Arguments> toStringWithThrowableData() {
        return Stream.of(
            Arguments.of(null, BeanUtils.EMPTY_STR),
            Arguments.of(new NullPointerException(), "java.lang.NullPointerException"),
            Arguments.of(new RuntimeException(), "java.lang.RuntimeException"),
            Arguments.of(new NumberFormatException(), "java.lang.NumberFormatException")
        );
    }

    /**
     * @param throwable
     * @param expected
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("toStringWithThrowableData")
    public <T> void testToStringWithThrowable(Throwable throwable, String expected) {
        String str = BeanUtils.toString(throwable);
        LOGGER.info("str: {}", str);
        assertTrue(str.contains(expected));
    }

    /**
     *
     */
    @Test
    public void testToString() {
        Object object = null;
        String str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);

        object = new RuntimeException();
        str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);

        object = "Lakra";
        str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);
        assertEquals("Lakra", str);

        object = new BigDecimal("16.01");
        str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);
        assertEquals("16.01", str);

        object = Double.valueOf("16.01");
        str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);
        assertEquals("16.01", str);

        object = Arrays.asList("Rohtash", "Lakra");
        str = BeanUtils.toString(object);
        LOGGER.info("str: {}", str);
        assertNotNull(str);
        assertEquals("[Rohtash, Lakra]", str);
    }

    //    @Test
    //    public void testCopyProperties() {
    // Payload payload = Payload.builder()
    //            .of("firstName", "Rohtash")
    //            .of("lastName", "Lakra");
    // LOGGER.debug("payload:{}", payload);
    // Payload tempPayload = Payload.builder();
    // BeanUtils.copyProperties(payload, tempPayload);
    // LOGGER.debug("tempPayload:{}", tempPayload);
    // assertNotNull(tempPayload);
    //
    // Payload copyPayload = Payload.builder();
    // BeanUtils.copyProperties(tempPayload, copyPayload);
    // LOGGER.debug("copyPayload:{}", copyPayload);
    // assertNotNull(copyPayload);
    // assertEquals(payload, copyPayload);
    //    }

//    @Test
//    public void testDeepCopyProperties() {
// Payload phones = Payload.builder()
//            .of("home", "4085108759")
//            .of("mobile", "4085108759")
//            .of("office", "4085108759");
//
// Payload payload = Payload.builder()
//            .of("firstName", "Rohtash")
//            .of("lastName", "Lakra")
//            .of("phones", phones);
//
// LOGGER.debug("payload:{}", payload);
// Payload tempPayload = Payload.builder();
// BeanUtils.deepCopyProperties(payload, tempPayload);
// LOGGER.debug("tempPayload:{}", tempPayload);
// assertNotNull(tempPayload);
//
// Payload copyPayload = Payload.builder();
// BeanUtils.deepCopyProperties(tempPayload, copyPayload);
// LOGGER.debug("copyPayload:{}", copyPayload);
// assertNotNull(copyPayload);
// assertEquals(payload, copyPayload);
//    }

    /**
     * @return
     */
    private static Stream<Arguments> replaceUnderscoresWithDashesData() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("Rohtash_Lakra", "Rohtash-Lakra"),
            Arguments.of("Rohtash_Singh_Lakra", "Rohtash-Singh-Lakra"),
            Arguments.of("Rohtash_16_Lakra", "Rohtash-16-Lakra")
        );
    }

    /**
     * @param input
     * @param expected
     */
    @ParameterizedTest
    @MethodSource("replaceUnderscoresWithDashesData")
    public void testReplaceUnderscoresWithDashes(String input, String expected) {
        String result = replaceUnderscoresWithDashes(input);
        LOGGER.debug("input: {}, result: {}, expected: {}", input, result, expected);
        assertEquals(expected, result);
    }
}
