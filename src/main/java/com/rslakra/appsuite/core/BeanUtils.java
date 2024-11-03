/******************************************************************************
 * Copyright (C) Devamatre 2009 - 2022. All rights reserved.
 * <p/>
 * This code is licensed to Devamatre under one or more contributor license
 * agreements. The reproduction, transmission or use of this code, in source
 * and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 * <pre>
 *  1. Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * </pre>
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * <p/>
 * Devamatre reserves the right to modify the technical specifications and or
 * features without any prior notice.
 *****************************************************************************/
package com.rslakra.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Rohtash Lakra
 * @created 5/27/20 2:13 PM
 */
public enum BeanUtils {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);
    public static final String REQUEST_TRACER = "requestTracer";
    public static final String EMPTY_STR = "";
    public static final String DOT = ".";
    public static final String UNKNOWN = "unknown";
    public static final String CLASS = "class";
    public static final String IS = "is";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String ID = "id";
    // IMMUTABLE_PROPERTIES
    public static final Set<String>
        IMMUTABLE_PROPERTIES =
        Sets.asSet("createdOn", "createdAt", "createdBy", "updatedOn", "updatedAt", "updatedBy");
    // PRIMITIVE_WRAPPER_TYPES
    private final Map<Class<?>, Class<?>> primitiveTypes = new HashMap<>(16);
    private final Set<Class<?>> simpleTypes = new HashSet<>(8);
    private final Set<Class<?>> sameTypes = new HashSet<>(8);
    // IMMUTABLE_ATTRIBUTES
    private final Set<String> immutableAttributes = new HashSet<>(Arrays.asList(ID));
    // CLASS_PROPERTY_DESCRIPTORS
    private final Map<Class<?>, Map<String, PropertyDescriptor>> classPropertyDescriptors = new ConcurrentHashMap<>();
    // CLASS_PROPERTIES
    private final ConcurrentMap<Class<?>, ClassProperties> classProperties = new ConcurrentHashMap<>(256);
    private boolean copyOnlyNonNullValues = false;

    BeanUtils() {
        // primitive or wrapper types
        primitiveTypes.put(Boolean.class, Boolean.TYPE);
        primitiveTypes.put(Byte.class, Byte.TYPE);
        primitiveTypes.put(Character.class, Character.TYPE);
        primitiveTypes.put(Short.class, Short.TYPE);
        primitiveTypes.put(Integer.class, Integer.TYPE);
        primitiveTypes.put(Long.class, Long.TYPE);
        primitiveTypes.put(Float.class, Float.TYPE);
        primitiveTypes.put(Double.class, Double.TYPE);
        primitiveTypes.put(Void.class, Void.TYPE);

        // simple type
        simpleTypes.add(CharSequence.class);
        simpleTypes.add(Number.class);
        simpleTypes.add(Date.class);

        // equal types
        sameTypes.add(Class.class);
        sameTypes.add(Locale.class);
        sameTypes.add(URI.class);
        sameTypes.add(URL.class);
    }

    /**
     * @return
     */
    public boolean isCopyOnlyNonNullValues() {
        return copyOnlyNonNullValues;
    }

    /**
     * @param copyOnlyNonNullValues
     */
    public void setCopyOnlyNonNullValues(boolean copyOnlyNonNullValues) {
        this.copyOnlyNonNullValues = copyOnlyNonNullValues;
    }

    /**
     * Forces to copy only non-null objects when the <code>copyOnlyNonNullValues</code> property set to be true.
     *
     * @return
     */
    public static boolean allowsObjectCopying(Object object) {
        return (INSTANCE.isCopyOnlyNonNullValues() ? isNotNull(object) : true);
    }

    /**
     * Returns true if the provided reference is null otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isNull(final Object object) {
        return Objects.isNull(object);
    }

    /**
     * Returns true if the provided reference is not null otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isNotNull(final Object object) {
        return !isNull(object);
    }

    /**
     * Returns true if the provided references are not null otherwise false.
     *
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> boolean isNotNull(final Object object, final Class<T> classType) {
        return (isNotNull(object) && isNotNull(classType));
    }

    /**
     * Returns true if the readable sequence of char values has white-space otherwise false.
     *
     * @param input
     * @return
     */
    public static boolean hasBlanks(final CharSequence input) {
        if (isNotNull(input)) {
            for (int index = 0; index < input.length(); index++) {
                if (Character.isWhitespace(input.charAt(index))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if the readable sequence of char values  are not a white-space otherwise false.
     *
     * @param input
     * @return
     */
    public static boolean isBlank(final CharSequence input) {
        if (isNotNull(input)) {
            for (int index = 0; index < input.length(); index++) {
                if (!Character.isWhitespace(input.charAt(index))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @param input
     * @return
     */
    public static boolean isNotBlank(final CharSequence input) {
        return !isBlank(input);
    }

    /**
     * Returns true if the object is an array otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isArray(final Object object) {
        if (isNotNull(object)) {
            if (object instanceof Object[]) {
                return true;
            } else if (object.getClass().isArray()) {
                return true;
            } else if (isTypeOf(object, Class.class)) {
                LOGGER.debug("object:{}", object);
                final Class<?> classType = (Class<?>) object;
                return (classType.isArray() || Array.class.isAssignableFrom(classType));
            }
        }

        return false;
    }

    /**
     * Returns true if the provided <code>object</code> is an instance of the provided <code>classType<T></code>
     * otherwise false.
     *
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> boolean isTypeOf(Object object, Class<T> classType) {
        return (isNotNull(classType) && classType.isInstance(object));
    }

    /**
     * Returns true if the class or interface represented by the <code>Class<T></code> classObject is either the same
     * as, or is a superclass or superinterface of, the class or interface represented by the specified Class parameter.
     * It returns true if so; otherwise it returns false. If this Class classObject represents a primitive type, this
     * method returns true if the specified Class parameter is exactly this Class classObject; otherwise it returns
     * false.
     *
     * @param classObject
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> boolean isAssignable(Object classObject, Class<T> classType) {
        return (isTypeOf(classObject, Class.class) && classType.isAssignableFrom((Class<?>) classObject));
    }

    /**
     * Returns true if the <code>object</code> is type of the <code>Class<T></code> or is assignable of the
     * <code>Class<T></code> otherwise false.
     *
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> boolean isKindOf(Object object, Class<T> classType) {
        return (isTypeOf(object, classType) || isAssignable(object, classType));
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Map</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfMap(final Object object) {
        return isKindOf(object, Map.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>List</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfList(final Object object) {
        return isKindOf(object, List.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Set</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfSet(final Object object) {
        return isKindOf(object, Set.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Collection</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfCollection(final Object object) {
        return isKindOf(object, Collection.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Iterable</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfIterable(final Object object) {
        return isKindOf(object, Iterable.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Iterator</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfIterator(final Object object) {
        return isKindOf(object, Iterator.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Enumeration</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfEnumeration(final Object object) {
        return isKindOf(object, Enumeration.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>CharSequence</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfCharSequence(final Object object) {
        return isKindOf(object, CharSequence.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>String</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfString(final Object object) {
        return isKindOf(object, String.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>BigDecimal</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfBigDecimal(final Object object) {
        return isKindOf(object, BigDecimal.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Date</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfDate(final Object object) {
        return isKindOf(object, Date.class);
    }

    /**
     * Returns true if the <code>object</code> is a type of <code>Throwable</code> otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isTypeOfThrowable(final Object object) {
        return isKindOf(object, Throwable.class);
    }

    /**
     * Converts the provided <code>object</code> into <code>classType</code> object.
     *
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T asType(Object object, Class<T> classType) {
        if (isNotNull(object) && isNotNull(classType)) {
            if (isAssignable(classType, BigDecimal.class)) {
                return (T) new BigDecimal(Objects.toString(object));
            } else if (isAssignable(classType, BigInteger.class)) {
                return (T) new BigInteger(Objects.toString(object));
            } else if (isAssignable(classType, Double.class)) {
                return (T) Double.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Float.class)) {
                return (T) Float.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Long.class)) {
                return (T) Long.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Integer.class)) {
                return (T) Integer.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Short.class)) {
                return (T) Short.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Byte.class)) {
                return (T) Byte.valueOf(Objects.toString(object));
            } else if (isAssignable(classType, Date.class)) {
                return (T) new Date(Objects.toString(object));
            } else if (isAssignable(classType, Boolean.class)) {
                return (T) Boolean.valueOf(Objects.toString(object));
            } else {
                try {
                    return (T) object;
                } catch (ClassCastException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        } else if (isNotNull(classType)) {
            if (isAssignable(classType, Boolean.class)) {
                return (T) Boolean.valueOf(Objects.toString(object));
            }
        }

        return null;
    }

    /**
     * Converts the <code>charSequence</code> into masked object.
     *
     * @param charSequence
     * @return
     */
    public static CharSequence maskObject(CharSequence charSequence) {
        return charSequence;
    }

    /**
     * Returns the size of the <code>Iterator</code> iterator.
     *
     * @param iterator
     * @return
     */
    public static long sizeIterator(final Iterator<?> iterator) {
        return sizeIterable(() -> (Iterator<Object>) iterator);
    }

    /**
     * Returns the size of the <code>Iterable</code> iterable.
     *
     * @param iterable
     * @return
     */
    public static long sizeIterable(final Iterable<?> iterable) {
        return (isNotNull(iterable) ? sizeIterator(iterable.iterator()) : 0);
    }

    /**
     * @param enumeration
     * @return
     */
    public static int sizeEnumeration(final Enumeration<?> enumeration) {
        return isNotNull(enumeration) ? Collections.list((Enumeration<?>) enumeration).size() : 0;
    }

    /**
     * Returns the length of the object.
     *
     * @param object
     * @return
     */
    public static long getLength(final Object object) {
        if (isNotNull(object)) {
            if (isArray(object)) {
                return Array.getLength(object);
            } else if (isTypeOfMap(object)) {
                return ((Map<?, ?>) object).size();
            } else if (isTypeOfCollection(object)) {
                return ((Collection<?>) object).size();
            } else if (isTypeOfIterator(object)) {
                return sizeIterator((Iterator<?>) object);
            } else if (isTypeOfIterable(object)) {
                return sizeIterable((Iterable<?>) object);
            } else if (isTypeOfEnumeration(object)) {
                return sizeEnumeration((Enumeration<?>) object);
            } else if (isTypeOfCharSequence(object)) {
                return ((CharSequence) object).length();
            }
        }

        return 0;
    }

    /**
     * Returns true if the <code>object</code> is null or empty otherwise false.
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(final Object object) {
        LOGGER.trace("+isEmpty({})", object);
        boolean result = false;
        if (isNull(object)) {
            result = true;
        } else if (isArray(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfMap(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfCollection(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfIterator(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfIterable(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfEnumeration(object) && getLength(object) == 0) {
            result = true;
        } else if (isTypeOfCharSequence(object) && getLength(object) == 0) {
            result = true;
        }

        LOGGER.trace("-isEmpty(), result:{}", result);
        return result;
    }

    /**
     * Returns true if the <code>object</code> is null or empty otherwise false.
     *
     * @param objects
     * @return
     */
    public static boolean isNotEmpty(final Object... objects) {
        if (isNotNull(objects) && getLength(objects) > 0) {
            for (int index = 0; index < objects.length; index++) {
                if (isEmpty(objects[index])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * @param object
     * @param message
     */
    public static void assertNonNull(final Object object, final String message) {
        if (isNull(object)) {
            throw new NullPointerException(message);
        }
    }

    /**
     * @param object
     */
    public static void assertNonNull(final Object object) {
        assertNonNull(object, "object should not be null!");
    }

    /**
     * Capitalize the string.
     *
     * @param self
     * @return
     */
    public static String toTitleCase(final CharSequence self) {
        return isEmpty(self) ? EMPTY_STR
                             : EMPTY_STR + Character.toUpperCase(self.charAt(0)) + self.subSequence(1, self.length());
    }

    /**
     * @param pathSegments
     * @return
     */
    public static String pathSegments(final String... pathSegments) {
        assertNonNull(pathSegments, "pathSegments should not be null!");
        return String.join(File.separator, pathSegments);
    }

    /**
     * Returns the unique random <code>UUID</code> string.
     *
     * @return
     */
    public static String nextUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns true if the <code>source</code> and <code>target</code> objects are same otherwise false.
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean equals(final Object source, final Object target) {
        if (source == target) {
            return true;
        } else if (isNotNull(source) && isNotNull(target)) {
            if (isTypeOfBigDecimal(source.getClass())) {
                return ((BigDecimal) source).compareTo((BigDecimal) target) == 0;
            } else if (isTypeOfDate(source.getClass())) {
                return ((Date) source).getTime() == ((Date) target).getTime();
            }

            return source.equals(target);
        }

        return false;
    }


    /**
     * Returns true if the <code>source</code> and <code>target</code> objects are not same otherwise false.
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean notEquals(final Object source, final Object target) {
        return !equals(source, target);
    }

    /**
     * Returns the positive hashCode of <code>objects</code>.
     *
     * @param objects
     * @return
     */
    public static int hashCode(final Object... objects) {
        /* always get the positive hashCode */
        return (Objects.hash(objects) & 0x7fffffff);
    }

    /**
     * @param charSequence
     */
    public static byte[] toBytes(final CharSequence charSequence) {
        byte[] dataBytes = null;
        if (isNotEmpty(charSequence)) {
            dataBytes = new byte[charSequence.length()];
            for (int i = 0; i < charSequence.length(); i++) {
                char cChar = charSequence.charAt(i);
                if (cChar > 0xff) {
                    throw new IllegalArgumentException(
                        "Invalid Character: " + (cChar) + " at index:" + (i + 1) + " in string: " + charSequence);
                }
                dataBytes[i] = (byte) cChar;
            }
        }

        return dataBytes;
    }

    /**
     * Returns the filePath of the <code>Package</code> name.
     *
     * @param <T>
     * @param classType
     * @return
     */
    public static <T> String getPackagePath(final Class<T> classType) {
        return (isNull(classType) ? EMPTY_STR : classType.getPackage().getName().replace(".", File.separator));
    }

    /**
     * Returns the package path of the given class.
     *
     * @param classType
     * @param withClassName
     * @param <T>
     * @return
     */
    public static <T> String getClassPath(final Class<T> classType, final boolean withClassName) {
        LOGGER.debug("+getClassPath({}, {})", classType, withClassName);
        String pkgPath = getPackagePath(classType);
        LOGGER.debug("pkgPath: {}", pkgPath);
        if (isNotNull(classType) && withClassName) {
            pkgPath += (isNotEmpty(pkgPath) ? File.separator : EMPTY_STR) + classType.getSimpleName();
            LOGGER.debug("withClassName pkgPath: {}", pkgPath);
        }

        LOGGER.debug("-getClassPath(), pkgPath: {}", pkgPath);
        return pkgPath;
    }

    /**
     * @param pathString
     * @return
     */
    public static boolean startsWithFileSeparator(final String pathString) {
        return (isNotEmpty(pathString) && pathString.startsWith(File.separator));
    }

    /**
     * @param classType
     * @param withClassName
     * @param pathComponents
     * @return
     */
    public static <T> String getClassPath(final Class<T> classType, final boolean withClassName,
                                          final String... pathComponents) {
        LOGGER.debug("+getClassPath({}, {}, {})", classType, withClassName, pathComponents);
        final StringBuilder pathBuilder = new StringBuilder(getClassPath(classType, withClassName));
        if (isNotEmpty(pathComponents)) {
            for (String pathString : pathComponents) {
                LOGGER.debug("pathString: {}", pathString);
                if (isNotEmpty(pathString)) {
                    if (!startsWithFileSeparator(pathString) && pathBuilder.length() > 0) {
                        pathBuilder.append(File.separator);
                    }
                    pathBuilder.append(pathString);
                }
            }
        }

        LOGGER.debug("-getClassPath(), pathBuilder: {}", pathBuilder);
        return pathBuilder.toString();
    }

    /**
     * @param classType
     * @param pathComponents
     * @return
     */
    public static String getClassPath(final Class<?> classType, final String... pathComponents) {
        return getClassPath(classType, false, pathComponents);
    }

    /**
     * @param pathComponents
     * @return
     */
    public static String getClassPath(final String... pathComponents) {
        return getClassPath(null, false, pathComponents);
    }

    /**
     * Returns true if the provided <code>classType</code> is either the wrapper or the primitive type otherwise false.
     *
     * @param classType
     * @return
     */
    public static boolean isPrimitive(final Class<?> classType) {
        assertNonNull(classType, "Class must not be null!");
        return (classType.isPrimitive() || INSTANCE.primitiveTypes.containsKey(classType));
    }

    /**
     * Returns true if the provided <code>classType</code> is equal to the <code>sameTypes</code> entry value otherwise
     * false.
     *
     * @param classType
     * @return
     */
    public static boolean isEnumType(final Class<?> classType) {
        assertNonNull(classType, "Class must not be null!");
        return classType.isEnum();
    }

    /**
     * Returns true if the provided <code>classType</code> exists in the <code>simpleTypes</code> map otherwise false.
     *
     * @param classType
     * @return
     */
    public static boolean isSimpleType(final Class<?> classType) {
        assertNonNull(classType, "Class must not be null!");
        return (INSTANCE.simpleTypes.stream().anyMatch(simpleType -> simpleType.isAssignableFrom(classType)));
    }

    /**
     * Returns true if the provided <code>classType</code> is equal to the <code>sameTypes</code> entry value otherwise
     * false.
     *
     * @param classType
     * @return
     */
    public static boolean isSameType(final Class<?> classType) {
        assertNonNull(classType, "Class must not be null!");
        return INSTANCE.sameTypes.stream().anyMatch(sameType -> sameType.equals(classType));
    }

    /**
     * @param classType
     * @return
     */
    public boolean isSimpleValueType(final Class<?> classType) {
        return (isPrimitive(classType) || isEnumType(classType) || isSimpleType(classType) || isSameType(classType));
    }

    /**
     * @param classType
     * @return
     */
    public boolean isSimpleProperty(final Class<?> classType) {
        Objects.requireNonNull(classType, "Class must not be null!");
        return isSimpleValueType(classType) || (classType.isArray() && isSimpleValueType(classType.getComponentType()));
    }

    /**
     * Partition the provided <code>inputValues</code> by the size of <code>partitionSize</code>.
     *
     * @param inputValues
     * @param partitionSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partitionBySize(final List<T> inputValues, final int partitionSize) {
        if (isEmpty(inputValues) || partitionSize <= 0) {
            return Collections.emptyList();
        } else if (inputValues.size() <= partitionSize) {
            return Collections.singletonList(inputValues);
        } else {
            final AtomicInteger counter = new AtomicInteger(0);
            return new ArrayList<>(
                inputValues.stream().collect(Collectors.groupingBy(item -> counter.getAndIncrement() / partitionSize))
                    .values());
        }
    }

    /**
     * Partition the provided <code>inputValues</code> by the size of <code>partitionSize</code>.
     *
     * @param inputValues
     * @param partitionSize
     * @param <T>
     * @return
     */
    public static <T> Set<Set<T>> partitionBySize(final Set<T> inputValues, final int partitionSize) {
        if (isEmpty(inputValues) || partitionSize <= 0) {
            return Collections.emptySet();
        } else if (inputValues.size() <= partitionSize) {
            return Collections.singleton(inputValues);
        } else {
            final AtomicInteger counter = new AtomicInteger(0);
            final Collection<Set<T>>
                partitions =
                inputValues.stream().collect(
                        Collectors.groupingBy(item -> counter.getAndIncrement() / partitionSize, Collectors.toSet()))
                    .values();
            return new HashSet<>(partitions);
        }
    }

    /**
     * Returns the enum type of the provided <code>fieldName</code>.
     *
     * @param classType
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> T findEnumByClass(final Class<T> classType, final String fieldName) {
        return Arrays.stream(classType.getEnumConstants())
            .filter(e -> ((Enum<?>) e).name().equalsIgnoreCase(fieldName))
            .findAny()
            .orElse(null);
    }


    /**
     * Returns true if the value is not null and equal to zero otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean isZero(final BigDecimal value) {
        return (isNotNull(value) && value.signum() == 0);
    }

    /**
     * Returns true if the value is not null and greater than zero otherwise false.
     *
     * @param value
     * @return
     */
    public static boolean isPositive(final BigDecimal value) {
        return (isNotNull(value) && value.signum() == 1);
    }


    /**
     * Returns true if the value is not null and less than zero otherwise false.
     *
     * @param value
     * @return
     */
    public boolean isNegative(final BigDecimal value) {
        return (isNotNull(value) && value.signum() == -1);
    }

    /**
     *
     */
    public static void logCallerMethodDetails() {
        // avoid unwanted thead-stack processing
        if (LOGGER.isInfoEnabled()) {
            final StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
            String callerClass = UNKNOWN;
            String callerMethod = UNKNOWN;
            for (int index = 0; index < 6; index++) {
                StackTraceElement element = stackTraces[index];
                LOGGER.debug(String.format("index=%d, lineNumber=%d, className=%s, methodName=%s", index,
                                           element.getLineNumber(), element.getClassName(), element.getMethodName()));
            }

            // check stack-traces size
            if (getLength(stackTraces) > 2) {
                callerClass = stackTraces[2].getClassName();
                callerMethod = stackTraces[2].getMethodName();
            }
            LOGGER.info("logCallerMethodDetails", "caller class=%s, method=%s", callerClass, callerMethod);
        }
    }

    /**
     * @param source
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> T arrayFromObject(final Object source, Class<T> responseType) {
        LOGGER.debug("+arrayFromObject({}, {})", source, responseType);
        final int length = Long.valueOf(getLength(source)).intValue();
        LOGGER.debug("length:{}", length);
        Object[] toObject = null;
        if (responseType.isAssignableFrom(String[].class)) {
            toObject = new String[length];
        } else if (responseType.isAssignableFrom(Character[].class)) {
            toObject = new Character[length];
        } else if (responseType.isAssignableFrom(Byte[].class)) {
            toObject = new Byte[length];
        } else if (responseType.isAssignableFrom(Short[].class)) {
            toObject = new Short[length];
        } else if (responseType.isAssignableFrom(Integer[].class)) {
            toObject = new Integer[length];
        } else if (responseType.isAssignableFrom(Long[].class)) {
            toObject = new Long[length];
        } else if (responseType.isAssignableFrom(Float[].class)) {
            toObject = new Float[length];
        } else if (responseType.isAssignableFrom(Double[].class)) {
            toObject = new Double[length];
        } else if (responseType.isAssignableFrom(BigDecimal[].class)) {
            toObject = new BigDecimal[length];
        } else {
            toObject = new Object[length];
        }

        System.arraycopy(source, 0, toObject, 0, length);
        LOGGER.debug("-arrayFromObject(), toObject:{}", (T) toObject);
        return (T) toObject;
    }

    /**
     * Converts the collection <code>response</code> to an array.
     *
     * @param response
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> T arrayFromCollection(final Collection<T> response, Class<T> responseType) {
        LOGGER.debug("+arrayFromCollection({}, {})", response, responseType);
        if (isArray(responseType)) {
            if (responseType.isAssignableFrom(String[].class)) {
                return (T) response.toArray(new String[0]);
            } else if (responseType.isAssignableFrom(Character[].class)) {
                return (T) response.toArray(new Character[0]);
            } else if (responseType.isAssignableFrom(Byte[].class)) {
                return (T) response.toArray(new Byte[0]);
            } else if (responseType.isAssignableFrom(Short[].class)) {
                return (T) response.toArray(new Short[0]);
            } else if (responseType.isAssignableFrom(Integer[].class)) {
                return (T) response.toArray(new Integer[0]);
            } else if (responseType.isAssignableFrom(Float[].class)) {
                return (T) response.toArray(new Float[0]);
            } else if (responseType.isAssignableFrom(Double[].class)) {
                return (T) response.toArray(new Double[0]);
            } else {
                return (T) response.toArray(new Object[0]);
            }
        } else if (isTypeOfSet(responseType)) {
            if (isArray(response)) {
                return (T) Arrays.asList(response).stream().collect(Collectors.toSet());
            } else if (isTypeOfCollection(response)) {
                return (T) response.stream().collect(Collectors.toSet());
            }
        } else if (isTypeOfList(responseType)) {
            if (isArray(response)) {
                return (T) Arrays.asList(response);
            } else if (isTypeOfCollection(response)) {
                return (T) response.stream().collect(Collectors.toList());
            }
        }

        LOGGER.debug("-arrayFromCollection()", response, responseType);
        return (T) response;
    }

    /**
     * Converts the collection <code>response</code> to an array.
     *
     * @param response
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> T arrayFromMap(final Map response, Class<T> responseType, final boolean keysArray) {
        if (keysArray) {
            return (T) arrayFromCollection(response.keySet(), responseType);
        } else {
            return (T) arrayFromCollection(response.values(), responseType);
        }
    }

    /**
     * Converts the <code>response</code> to an array.
     *
     * @param response
     * @param responseType
     * @param <T>
     * @return
     */
    public static <T> T toType(final Object response, Class<T> responseType) {
        if (isArray(responseType)) {
            if (isArray(response)) {
                return INSTANCE.arrayFromObject(response, responseType);
            } else if (isTypeOfMap(response)) {
                return INSTANCE.arrayFromMap((Map) response, responseType, true);
            } else if (isTypeOfCollection(response)) {
                return INSTANCE.arrayFromCollection((Collection<T>) response, responseType);
            }
        } else if (isTypeOfMap(responseType)) {
        } else if (isTypeOfCollection(responseType)) {
            if (isTypeOfMap(response)) {
                return INSTANCE.arrayFromMap((Map) response, responseType, true);
            } else if (isTypeOfCollection(response)) {
                return INSTANCE.arrayFromCollection((Collection<T>) response, responseType);
            }
        }

        return (T) response;
    }

    /**
     * Capitalize the string.
     *
     * @param self
     * @return
     */
    public static String capitalize(final CharSequence self) {
        return isEmpty(self) ? EMPTY_STR
                             : EMPTY_STR + Character.toUpperCase(self.charAt(0)) + self.subSequence(1, self.length());
    }

    /**
     * @param name
     * @return
     */
    private boolean isModifiable(final String name) {
        return !immutableAttributes.contains(name);
    }

    /**
     * Returns the <code>PropertyDescriptor[]</code> for the given class.
     * <p>
     * CVE-2010-1622
     * <code>http://blog.o0o.nu/2010/06/cve-2010-1622.html</code>
     *
     * @param classType
     * @return
     * @throws IntrospectionException
     */
    public static PropertyDescriptor[] getBeanInfo(final Class<?> classType) throws IntrospectionException {
        return Introspector.getBeanInfo(classType, Object.class).getPropertyDescriptors();
    }

    /**
     * @param classType
     * @param propertyDescriptor
     * @throws IntrospectionException
     */
    private void findWriteMethod(final Class<?> classType, final PropertyDescriptor propertyDescriptor)
        throws IntrospectionException {
        if (!isClassPropertyDescriptor(propertyDescriptor) && propertyDescriptor.getReadMethod() != null) {
            final String setterMethod = getSetterMethod(propertyDescriptor);
            final Class<?> propType = getReturnType(propertyDescriptor);
            for (Method method : classType.getMethods()) {
                if (setterMethod.equals(method.getName()) && method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].isAssignableFrom(propType)) {
                    propertyDescriptor.setWriteMethod(method);
                    return;
                }
            }
        }
    }

    /**
     * @param classType
     * @return
     */
    public final Map<String, PropertyDescriptor> findByClass(final Class<?> classType) {
        try {
            Map<String, PropertyDescriptor> typePropertyDescriptor = classPropertyDescriptors.get(classType);
            if (isNull(typePropertyDescriptor)) {
                typePropertyDescriptor = new LinkedHashMap<>();
                final PropertyDescriptor[] propertyDescriptors = getBeanInfo(classType);
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (isNull(propertyDescriptor.getWriteMethod())) {
                        findWriteMethod(classType, propertyDescriptor);
                    }

                    typePropertyDescriptor.put(propertyDescriptor.getName(), propertyDescriptor);
                }

                classPropertyDescriptors.putIfAbsent(classType, typePropertyDescriptor);
                typePropertyDescriptor = classPropertyDescriptors.get(classType);
            }

            return typePropertyDescriptor;
        } catch (IntrospectionException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Returns true if the <code>PropertyDescriptor</code> name is class otherwise false.
     *
     * @param propertyDescriptor
     * @return
     */
    public final boolean isClassPropertyDescriptor(final PropertyDescriptor propertyDescriptor) {
        return (propertyDescriptor != null && CLASS.equals(propertyDescriptor.getName()));
    }

    /**
     * @param propName
     * @return
     */
    public final String toMutatorMethod(final String prefix, final String propName) {
        return String.format("%s%s%s", prefix, propName.substring(0, 1).toUpperCase(), propName.substring(1));
    }

    /**
     * @param propertyDescriptor
     * @param methodType
     * @return
     */
    public final String toMutatorMethod(final PropertyDescriptor propertyDescriptor, final String methodType) {
        return toMutatorMethod(methodType, propertyDescriptor.getName());
    }

    /**
     * @param propertyDescriptor
     * @return
     */
    public final String getSetterMethod(final PropertyDescriptor propertyDescriptor) {
        return toMutatorMethod(propertyDescriptor, SET);
    }

    /**
     * @param propertyDescriptor
     * @return
     */
    public final String getGetterMethod(final PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor.getPropertyType().isAssignableFrom(Boolean.class)) {
            return toMutatorMethod(propertyDescriptor, IS);
        }

        return toMutatorMethod(propertyDescriptor, GET);
    }

    /**
     * @param method
     * @return
     */
    private boolean hasGetPrefix(final Method method) {
        return (isNotNull(method) && method.getName().startsWith(GET) && method.getName().length() > GET.length());
    }

    /**
     * @param method
     * @return
     */
    private boolean hasIsPrefix(final Method method) {
        return (isNotNull(method) && method.getName().startsWith(IS) && method.getName().length() > IS.length());
    }

    /**
     * @param method
     * @return
     */
    private boolean hasSetPrefix(final Method method) {
        return (isNotNull(method) && method.getName().startsWith(SET) && method.getName().length() > SET.length());
    }

    /**
     * Returns true if the <code>method</code> is getter method otherwise false.
     *
     * @param method
     * @return
     */
    public static boolean isGetter(final Method method) {
        return (isNotNull(method) && method.getParameterTypes().length == 0 && (INSTANCE.hasIsPrefix(method)
                                                                                || INSTANCE.hasGetPrefix(method)));
    }

    /**
     * Returns true if the <code>method</code> is setter method otherwise false.
     *
     * @param method
     * @return
     */
    public static boolean isSetter(final Method method) {
        return (isNotNull(method) && method.getParameterTypes().length == 1 && INSTANCE.hasSetPrefix(method));
    }

    /**
     * Returns the <code>Method</code> object.
     *
     * @param classType
     * @param methodName
     * @param parameterTypes
     * @param <T>
     * @return
     */
    public static <T> Method findMethodByName(Class<T> classType, String methodName, Class<?>... parameterTypes) {
        Method methodByName = null;
        if (isNotNull(classType)) {
            try {
                methodByName = classType.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }

        return methodByName;
    }

    /**
     * Returns true if the <code>method</code> is setter method otherwise false.
     *
     * @param classType
     * @param methodName
     * @return
     */
    public static boolean hasSetter(Class<?> classType, String methodName, Class<?>... parameterTypes) {
        Method method = findMethodByName(classType, methodName, parameterTypes);
        return (isNotNull(method) && method.getParameterTypes().length == 1 && INSTANCE.hasSetPrefix(method));
    }

    /**
     * Returns the type of the property.
     *
     * @param propertyDescriptor
     * @return
     */
    public final Class<?> getReturnType(final PropertyDescriptor propertyDescriptor) {
        return (isNull(propertyDescriptor) ? null : propertyDescriptor.getReadMethod().getReturnType());
    }

    /**
     * Ensures that the method is public and accessible.
     *
     * @param method
     */
    public final void ensurePublic(final Method method) {
        if (isNotNull(method) && !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    /**
     * @param classType
     * @param propertyName
     * @return
     */
    public final PropertyDescriptor findByPropertyName(final Class<?> classType, final String propertyName) {
        return findByClass(classType).get(propertyName);
    }

    /**
     * Returns the properties for the provided class.
     *
     * @param classType
     * @return
     */
    public final Collection<PropertyDescriptor> getProperties(final Class<?> classType) {
        return findByClass(classType).values();
    }

    /**
     * Returns the list of all fields of the provided <code>Class<?></code>.
     *
     * @param classType - should not be null
     * @return fields from the classType and its superclass
     */
    public static List<Field> getAllFields(Class<?> classType) {
        final List<Field> allFields = new ArrayList<>();
        while (classType != null) {
            Field[] fields = classType.getDeclaredFields();
            Collections.addAll(allFields, fields);
            if (classType.getSuperclass() == null) {
                break;
            }
            classType = classType.getSuperclass();
        }

        return allFields;
    }

    /**
     * @param propertyDescriptor
     * @param object
     * @return
     */
    public final Object readObjectProperty(final PropertyDescriptor propertyDescriptor, final Object object) {
        try {
            return propertyDescriptor.getReadMethod().invoke(object);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException(ex.getTargetException());
        }
    }

    /**
     * @param sourceObject
     * @param name
     * @return
     */
    public static final Object readObjectProperty(final Object sourceObject, final String name) {
        return INSTANCE.readObjectProperty(INSTANCE.findByPropertyName(sourceObject.getClass(), name), sourceObject);
    }

    /**
     * Sets the <code>target</code> object's property value.
     *
     * @param propertyDescriptor
     * @param target
     * @param value
     */
    public final void setBeanProperty(PropertyDescriptor propertyDescriptor, Object target, Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(target, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException(ex.getTargetException());
        }
    }

    /**
     * @param classType
     * @return
     */
    private ClassProperties getPropertyDescriptors(final Class<?> classType) {
        ClassProperties classProperties = this.classProperties.get(classType);
        if (isNull(classProperties)) {
            this.classProperties.putIfAbsent(classType, new ClassProperties(classType));
            classProperties = this.classProperties.get(classType);
        }

        return classProperties;
    }

    /**
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T newInstance(final Class<T> classType) throws ReflectiveOperationException {
        return (isNull(classType) ? null : classType.getDeclaredConstructor().newInstance());
    }

    /**
     * The <code>BeanPropertyDescriptor</code> contains the bean <code>name</code>, <code>classType</code>,
     * <code>readMethod</code>, and <code>writeMethod</code>.
     */
    private final class BeanPropertyDescriptor {

        private final String name;
        private final Class<?> classType;
        private final Method readMethod;
        private final Method writeMethod;

        /**
         * @param propertyDescriptor
         */
        public BeanPropertyDescriptor(final PropertyDescriptor propertyDescriptor) {
            LOGGER.trace("+BeanPropertyDescriptor({})", propertyDescriptor);
            this.name = propertyDescriptor.getName();
            this.classType = propertyDescriptor.getReadMethod().getReturnType();
            this.readMethod = propertyDescriptor.getReadMethod();
            this.writeMethod = propertyDescriptor.getWriteMethod();
            LOGGER.trace("-BeanPropertyDescriptor()");
        }

        /**
         * Returns the <code>newInstance</code> of the property class.
         *
         * @return
         * @throws ReflectiveOperationException
         */
        public Object newInstance() throws ReflectiveOperationException {
            if (classType.equals(Set.class)) {
                return new HashSet<>();
            } else if (classType.equals(Map.class)) {
                return new HashMap<>();
            } else if (classType.equals(List.class)) {
                return new ArrayList<>();
            } else {
                return INSTANCE.newInstance(classType);
            }
        }

        /**
         * @param sourceProperty
         * @param source
         * @param target
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         */
        private void setTargetProperty(BeanPropertyDescriptor sourceProperty, Object source, Object target)
            throws InvocationTargetException, IllegalAccessException {
            setTargetProperty(target, sourceProperty.readMethod.invoke(source));
        }

        private void setTargetProperty(Object target, Object value) {
            try {
                this.writeMethod.invoke(target, value);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                //ignore me
            }
        }

        /**
         * Returns the string representation of this object.
         *
         * @return
         */
        @Override
        public String toString() {
            return ToString.of(BeanPropertyDescriptor.class)
                .add("name", name)
                .add("classType", classType)
                .add("readMethod", readMethod)
                .add("writeMethod", writeMethod)
                .toString();
        }
    }

    /**
     * Contains the class properties.
     */
    private final class ClassProperties {

        // readProperties
        public final Map<String, BeanPropertyDescriptor> readProperties;
        // writeProperties
        private final BeanPropertyDescriptor[] writeProperties;

        /**
         * @param classType
         */
        public ClassProperties(final Class<?> classType) {
            LOGGER.trace("+ClassProperties({})", classType);
            final Collection<PropertyDescriptor> allProperties = getProperties(classType);
            LOGGER.trace("allProperties: {}", allProperties);
            this.readProperties = new HashMap<>(allProperties.size(), 1.0F);
            final List<BeanPropertyDescriptor> writeList = new ArrayList<>(allProperties.size());
            allProperties.forEach(propertyDescriptor -> {
                LOGGER.trace("propertyDescriptor: {}", propertyDescriptor);
                ensurePublic(propertyDescriptor.getReadMethod());
                ensurePublic(propertyDescriptor.getWriteMethod());
                final BeanPropertyDescriptor beanDescriptor = new BeanPropertyDescriptor(propertyDescriptor);
                LOGGER.trace("beanDescriptor: {}", beanDescriptor);
                if (isNotNull(propertyDescriptor.getReadMethod())) {
                    this.readProperties.put(propertyDescriptor.getName(), beanDescriptor);
                }

                if (isNotNull(propertyDescriptor.getWriteMethod())) {
                    writeList.add(beanDescriptor);
                }
            });

            this.writeProperties = writeList.toArray(new BeanPropertyDescriptor[0]);
            LOGGER.trace("-ClassProperties()");
        }

        /**
         * @param keyName
         * @return
         */
        public BeanPropertyDescriptor getReadPropertyByName(Object keyName) {
            return readProperties.get(keyName);
        }

        /**
         * @return
         */
        public int getWritePropertiesLength() {
            return writeProperties.length;
        }

        /**
         * @param index
         * @return
         */
        public BeanPropertyDescriptor getWritePropertyByIndex(int index) {
            return writeProperties[index];
        }
    }

    /**
     * @param source
     * @param target
     * @param ignoredProperties
     * @throws IllegalStateException
     */
    public static void copyProperties(final Object source, final Object target, final String... ignoredProperties)
        throws IllegalStateException {
        LOGGER.trace("+copyProperties({}, {}, {})", source, target, ignoredProperties);
        assertNonNull(source, "Source must not be null!");
        assertNonNull(target, "Target must not be null!");
        BeanPropertyDescriptor targetProperty = null;
        try {
            final ClassProperties sourceProperties = INSTANCE.getPropertyDescriptors(source.getClass());
            final ClassProperties targetProperties = INSTANCE.getPropertyDescriptors(target.getClass());
            LOGGER.trace("sourceProperties:{}, targetProperties:{}", sourceProperties, targetProperties);
            final Set<String> lookupIgnored = Sets.asSet(ignoredProperties);
            LOGGER.trace("lookupIgnored:{}", lookupIgnored);
            for (int i = 0; i < targetProperties.getWritePropertiesLength(); ++i) {
                targetProperty = targetProperties.getWritePropertyByIndex(i);
                LOGGER.trace("targetProperty:{}", targetProperty);
                if (!lookupIgnored.contains(targetProperty.name)) {
                    BeanPropertyDescriptor sourceProperty = sourceProperties.getReadPropertyByName(targetProperty.name);
                    if (sourceProperty != null) {
                        final Object value = sourceProperty.readMethod.invoke(source);
                        // only copy non-null values.
                        if (allowsObjectCopying(value)) {
                            targetProperty.setTargetProperty(target, value);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
            LOGGER.error("Error coping [{}] property of [{}] class!", targetProperty.name, target.getClass().getName());
            throw new IllegalStateException(ex);
        }
    }

    /**
     * @param source
     * @param target
     * @param ignoredProperties
     * @throws IllegalStateException
     */
    public static void deepCopyProperties(final Object source, final Object target, final String... ignoredProperties)
        throws IllegalStateException {
        assertNonNull(source, "Source must not be null!");
        assertNonNull(target, "Target must not be null!");
        BeanPropertyDescriptor targetProperty = null;
        try {
            final ClassProperties sourceProperties = INSTANCE.getPropertyDescriptors(source.getClass());
            final ClassProperties targetProperties = INSTANCE.getPropertyDescriptors(target.getClass());
            LOGGER.trace("sourceProperties:{}, targetProperties:{}", sourceProperties, targetProperties);
            final Set<String> lookupIgnored = Sets.asSet(ignoredProperties);
            LOGGER.trace("lookupIgnored:{}", lookupIgnored);
            for (int i = 0; i < targetProperties.getWritePropertiesLength(); ++i) {
                targetProperty = targetProperties.getWritePropertyByIndex(i);
                LOGGER.trace("targetProperty:{}", targetProperty);
                if (!lookupIgnored.contains(targetProperty.name)) {
                    BeanPropertyDescriptor sourceProperty = sourceProperties.getReadPropertyByName(targetProperty.name);
                    if (sourceProperty != null) {
                        if (INSTANCE.isSimpleProperty(sourceProperty.classType)) {
                            targetProperty.setTargetProperty(sourceProperty, source, target);
                        } else {
                            try {
                                final Object sourceObject = sourceProperty.readMethod.invoke(source);
                                final Object targetValue = targetProperty.newInstance();
                                deepCopyProperties(sourceObject, targetValue);
                                targetProperty.setTargetProperty(target, targetValue);
                            } catch (InstantiationException ex) {
                                LOGGER.error("{}, {}", ex.getLocalizedMessage(), ex);
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (ReflectiveOperationException ex) {
            LOGGER.error("Error coping [{}] property of [{}] class!", targetProperty.name, target.getClass().getName());
            throw new IllegalStateException(ex);
        }
    }

    /**
     * @param source
     * @param target
     * @param ignoredProperties
     * @return
     * @throws Exception
     */
    public List<PropertyDescriptor> mergeObject(final Object source, final Object target,
                                                final Set<String> ignoredProperties) throws Exception {
        final List<PropertyDescriptor> updatedProperties = new ArrayList<>();
        // Iterate over all the attributes
        for (PropertyDescriptor propertyDescriptor : getBeanInfo(target.getClass())) {
            // Only copy writable attributes
            if (propertyDescriptor.getWriteMethod() != null) {
                final String propName = propertyDescriptor.getName();
                if (ignoredProperties.contains(propName)) {
                    continue;
                }
                Object sourceValue = propertyDescriptor.getReadMethod().invoke(source);
                Object destValue = propertyDescriptor.getReadMethod().invoke(target);
                if (isNotNull(sourceValue) && notEquals(sourceValue, destValue)) {
                    updatedProperties.add(propertyDescriptor);
                    setBeanProperty(propertyDescriptor, target, sourceValue);
                }
            }
        }

        return updatedProperties;
    }


    /**
     * @param charSequence
     * @param separator
     * @return
     */
    public static String separateCamelCase(final CharSequence charSequence, final String separator) {
        if (isNull(charSequence)) {
            return null;
        } else if (isNull(separator)) {
            return charSequence.toString();
        } else {
            final StringBuilder translation = new StringBuilder();
            int i = 0;
            for (int length = charSequence.length(); i < length; ++i) {
                char character = charSequence.charAt(i);
                if (Character.isUpperCase(character) && translation.length() != 0) {
                    translation.append(separator);
                }

                translation.append(character);
            }

            return translation.toString();
        }
    }

    /**
     * @param text
     * @return
     */
    public static String toSentenceCase(final CharSequence text) {
        if (isNull(text)) {
            return null;
        }

        int firstLetterIndex = 0;
        for (int limit = text.length() - 1;
             !Character.isLetter(text.charAt(firstLetterIndex)) && firstLetterIndex < limit; ++firstLetterIndex) {
            // do nothing
        }

        char firstLetter = text.charAt(firstLetterIndex);
        if (Character.isUpperCase(firstLetter)) {
            return text.toString();
        } else {
            final String fullString = text.toString();
            char upperCaseLetter = Character.toUpperCase(firstLetter);
            return (firstLetterIndex == 0 ? upperCaseLetter + fullString.substring(1)
                                          : fullString.substring(0, firstLetterIndex) + upperCaseLetter
                                            + fullString.substring(firstLetterIndex + 1));
        }
    }

    /**
     * Returns empty if null otherwise string.
     *
     * @param throwable
     * @return
     */
    public static String toString(final Throwable throwable) {
        if (isNull(throwable)) {
            return EMPTY_STR;
        }

        final StringWriter strWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(strWriter));
        return strWriter.toString();
    }

    /**
     * Returns empty if null otherwise string.
     *
     * @param object
     * @return
     */
    public static String toString(final Object object) {
        if (isTypeOfBigDecimal(object)) {
            return ((BigDecimal) object).toPlainString();
        } else if (isTypeOfThrowable(object)) {
            return toString((Throwable) object);
        }

        return Objects.toString(object);
    }

    /**
     * @param value
     * @return
     */
    public static String replaceUnderscoresWithDashes(final String value) {
        if (isEmpty(value)) {
            return value;
        }

        return value
            .replaceAll("([a-z])([A-Z])", "$1-$2")
            .replaceAll("_", "-");
//            .toLowerCase();
    }

}
