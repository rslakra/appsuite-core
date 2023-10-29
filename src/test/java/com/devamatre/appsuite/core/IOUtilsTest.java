package com.devamatre.appsuite.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rohtash Lakra
 * @created 3/29/21 11:21 AM
 */
public class IOUtilsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(IOUtilsTest.class);

    /**
     *
     */
    @Test
    public void testApplyFilePermissions() {
        Path tempPathFolder = Paths.get(IOUtils.getBuildDir("target", "temp", "test.tmp"));
        LOGGER.debug("tempPathFolder: {}", tempPathFolder);
        try {
            IOUtils.applyFilePermissions(tempPathFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return
     */
    private static Stream<Arguments> classFilePathData() {
        return Stream.of(
            //same key/value
            Arguments.of(null, null),
            Arguments.of(BeanUtilsTest.class, BeanUtils.getPackagePath(BeanUtilsTest.class)),
            Arguments.of(IOUtils.class, BeanUtils.getPackagePath(IOUtils.class))
        );
    }

    /**
     * @param classType
     * @param expectedPath
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("classFilePathData")
    public <T> void testGetClassFilePath(Class<T> classType, String expectedPath) {
        String classPath = IOUtils.getClassFilePath(classType);
        LOGGER.debug("classPath:{}", classPath);
        if (BeanUtils.isNull(expectedPath)) {
            assertNull(classPath);
        } else {
            assertNotNull(classPath);
            assertTrue(classPath.endsWith(expectedPath));
        }
    }

    /**
     * List Jar Entries <code>jar -tf jarFile.jar</code>
     *
     * <pre>
     *  META-INF/
     *  META-INF/MANIFEST.MF
     *  logback.xml
     *  com/
     *  com/rslakra/
     *  com/rslakra/frameworks/
     *  com/rslakra/frameworks/.DS_Store
     *  com/rslakra/frameworks/common/
     *  com/rslakra/frameworks/common/CharSets.class
     *  com/rslakra/frameworks/common/.DS_Store
     *  com/rslakra/frameworks/common/MathUtils.class
     *  com/rslakra/frameworks/common/UnsafeUtils.class
     *  com/rslakra/frameworks/common/Pair.class
     *  com/rslakra/frameworks/common/HashUtils.class
     *  com/rslakra/frameworks/common/ArrayIterator.class
     *  com/rslakra/frameworks/common/xml/
     *  com/rslakra/frameworks/common/xml/XmlUtils.class
     *  com/rslakra/frameworks/common/Payload.class
     *  com/rslakra/frameworks/common/Sets.class
     *  com/rslakra/frameworks/common/exception/
     *  com/rslakra/frameworks/common/exception/ServerRuntimeException.class
     *  com/rslakra/frameworks/common/exception/InvalidRequestException.class
     *  application.properties
     * </pre>
     *
     * @throws IOException
     */
    @Test
    public void testGetJarEntries() throws IOException {
        LOGGER.debug("testGetJarEntries()");
        String JAR_PATH = IOUtils.pathString(IOUtils.getUserDir(), "target/test-classes/jarFile.jar");
        LOGGER.debug(JAR_PATH);
        Set<String> expectedJarEntries = Sets.asSet(
            "META-INF/",
            "META-INF/MANIFEST.MF",
            "logback.xml",
            "com/",
            "com/rslakra/",
            "com/rslakra/frameworks/",
            "com/rslakra/frameworks/core/",
            "com/rslakra/frameworks/core/CharSets.class",
            "com/rslakra/frameworks/core/MathUtils.class",
            "com/rslakra/frameworks/core/UnsafeUtils.class",
            "com/rslakra/frameworks/core/Pair.class",
            "com/rslakra/frameworks/core/HashUtils.class",
            "com/rslakra/frameworks/core/ArrayIterator.class",
            "com/rslakra/frameworks/core/xml/",
            "com/rslakra/frameworks/core/xml/XmlUtils.class",
            "com/rslakra/frameworks/core/Payload.class",
            "com/rslakra/frameworks/core/Sets.class",
            "com/rslakra/frameworks/core/exception/",
            "com/rslakra/frameworks/core/exception/ServerRuntimeException.class",
            "com/rslakra/frameworks/core/exception/InvalidRequestException.class",
            "application.properties"
        );

        LOGGER.debug("expectedJarEntries: {}", expectedJarEntries);
        Set<String> jarEntries = IOUtils.getJarEntries(new File(JAR_PATH));
        LOGGER.debug("jarEntries: {}", jarEntries);
        assertTrue(jarEntries.containsAll(expectedJarEntries));
//        assertEquals(expectedJarEntries, jarEntries);
        LOGGER.debug("-testGetJarEntries()");
    }


    /**
     * Jar Class File Names
     *
     * @throws IOException
     */
    @Test
    public void testGetJarFileClassNames() throws IOException {
        LOGGER.debug("-testGetJarFileClassNames()");
        String JAR_PATH = IOUtils.pathString(IOUtils.getUserDir(), "target/test-classes/jarFile.jar");
        LOGGER.debug(JAR_PATH);
        Set<String> expectedClassNames = Sets.asSet(
            "com.rslakra.frameworks.common.CharSets",
            "com.rslakra.frameworks.common.MathUtils",
            "com.rslakra.frameworks.common.UnsafeUtils",
            "com.rslakra.frameworks.common.Pair",
            "com.rslakra.frameworks.common.HashUtils",
            "com.rslakra.frameworks.common.ArrayIterator",
            "com.rslakra.frameworks.common.xml.XmlUtils",
            "com.rslakra.frameworks.common.Payload",
            "com.rslakra.frameworks.common.Sets",
            "com.rslakra.frameworks.common.exception.ServerRuntimeException",
            "com.rslakra.frameworks.common.exception.InvalidRequestException"
        );

        LOGGER.debug("expectedClassNames: {}", expectedClassNames);
        Set<String> jarFileClassNames = IOUtils.getJarFileClassNames(new File(JAR_PATH));
        LOGGER.debug("jarFileClassNames: {}", jarFileClassNames);
        assertEquals(expectedClassNames, jarFileClassNames);
        LOGGER.debug("-testGetJarFileClassNames()");
    }

    /**
     * Jar Class File Names
     *
     * @throws IOException
     */
    @Test
    public void testGetJarFileClasses() throws IOException, ClassNotFoundException {
        LOGGER.debug("-testGetJarFileClasses()");
        Set<String> expectedClassNames = Sets.asSet(
            "com.rslakra.frameworks.common.CharSets",
            "com.rslakra.frameworks.common.MathUtils",
            "com.rslakra.frameworks.common.UnsafeUtils",
            "com.rslakra.frameworks.common.Pair",
            "com.rslakra.frameworks.common.HashUtils",
            "com.rslakra.frameworks.common.ArrayIterator",
            "com.rslakra.frameworks.common.xml.XmlUtils",
            "com.rslakra.frameworks.common.Payload",
            "com.rslakra.frameworks.common.Sets",
            "com.rslakra.frameworks.common.exception.ServerRuntimeException",
            "com.rslakra.frameworks.common.exception.InvalidRequestException"
        );

        LOGGER.debug("expectedClassNames: {}", expectedClassNames);
        File jarFile = new File(IOUtils.pathString(IOUtils.getUserDir(), "target/test-classes/jarFile.jar"));
        LOGGER.debug("jarFile: {}", jarFile.getAbsolutePath());
//        Set<String> jarFileClassNames = IOUtils.getJarFileClassNames(jarFile);
//        LOGGER.debug("jarFileClassNames: {}", jarFileClassNames);
        Set<Class> jarFileClasses = IOUtils.getJarFileClasses(jarFile);
        LOGGER.debug("jarFileClasses: {}", jarFileClasses);
        Set<String> jarClasses = jarFileClasses.stream().map(Class::getName).collect(Collectors.toSet());
        assertEquals(expectedClassNames, jarClasses);
        LOGGER.debug("-testGetJarFileClasses()");
    }
}
