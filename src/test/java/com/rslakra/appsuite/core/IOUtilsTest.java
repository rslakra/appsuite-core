package com.rslakra.appsuite.core;

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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rohtash Lakra
 * @created 3/29/21 11:21 AM
 */
public class IOUtilsTest {

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(IOUtilsTest.class);

    /**
     * Tests <code>getUserDir()</code> method.
     */
    @Test
    public void testGetUserDir() {
        LOGGER.debug("+testGetUserDir()");
        String userDir = IOUtils.getUserDir();
        LOGGER.debug("userDir: {}", userDir);
        assertNotNull(userDir);
        assertTrue(userDir.endsWith("appsuite-core"));
        LOGGER.debug("-testGetUserDir()");
    }

    /**
     * Tests <code>getBuildDir()</code> method.
     */
    @Test
    public void testPathWithWorkingDir() {
        LOGGER.debug("+testPathWithWorkingDir()");
        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        LOGGER.debug("-testPathWithWorkingDir()");
    }

    /**
     * Tests <code>applyFilePermissions()</code> method.
     */
    @Test
    public void testApplyFilePermissions() {
        LOGGER.debug("+testApplyFilePermissions()");
        Path tempPathFolder = Paths.get(IOUtils.pathWithWorkingDir("target", "temp", "test.tmp"));
        LOGGER.debug("tempPathFolder: {}", tempPathFolder);
        try {
            IOUtils.applyFilePermissions(tempPathFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug("-testApplyFilePermissions()");
    }


    /**
     * @return
     */
    private static Stream<Arguments> classFilePathData() {
        return Stream.of(
                //same key/value
                Arguments.of(null, null), Arguments.of(BeanUtilsTest.class, BeanUtils.getPackagePath(BeanUtilsTest.class)), Arguments.of(IOUtils.class, BeanUtils.getPackagePath(IOUtils.class)));
    }

    /**
     * Tests <code>getClassFilePath()</code> method.
     *
     * @param classType
     * @param expectedPath
     * @param <T>
     */
    @ParameterizedTest
    @MethodSource("classFilePathData")
    public <T> void testGetClassFilePath(Class<T> classType, String expectedPath) {
        LOGGER.debug("+testGetClassFilePath()");
        String classPath = IOUtils.getClassFilePath(classType);
        LOGGER.debug("classPath:{}", classPath);
        if (BeanUtils.isNull(expectedPath)) {
            assertNull(classPath);
        } else {
            assertNotNull(classPath);
            assertTrue(classPath.endsWith(expectedPath));
        }
        LOGGER.debug("-testGetClassFilePath()");
    }

    /**
     * Tests <code>readContents()</code> method.
     */
    @Test
    public void testReadAllContents() {
        LOGGER.debug("+testReadAllContents()");
        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        String versionContents = IOUtils.readAllContents(versionFilePath);
        LOGGER.debug("versionContents: {}", versionContents);
        assertNotNull(versionContents);
        LOGGER.debug("-testReadAllContents()");
    }

    /**
     * Tests <code>readAllLines()</code> method.
     */
    @Test
    public void testReadAllLines() {
        LOGGER.debug("+testReadAllLines()");
        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        List<String> versionContents = IOUtils.readAllLines(versionFilePath);
        LOGGER.debug("versionContents: {}", versionContents);
        assertNotNull(versionContents);
        assertNotNull(versionContents.get(0));
        assertTrue(versionContents.size() > 0);
        LOGGER.debug("-testReadAllLines()");
    }

    /**
     * Tests <code>getJarEntries()</code> method.
     * List Jar Entries <code>jar -tf jarFile.jar</code>
     *
     * <pre>
     *  META-INF/
     *  META-INF/MANIFEST.MF
     *  logback.xml
     *  com/
     *  com/rslakra/
     *  com/rslakra/appsuite/
     *  com/rslakra/appsuite/.DS_Store
     *  com/rslakra/appsuite/common/
     *  com/rslakra/appsuite/common/CharSets.class
     *  com/rslakra/appsuite/common/MathUtils.class
     *  com/rslakra/appsuite/common/UnsafeUtils.class
     *  com/rslakra/appsuite/common/Pair.class
     *  com/rslakra/appsuite/common/HashUtils.class
     *  com/rslakra/appsuite/common/ArrayIterator.class
     *  com/rslakra/appsuite/common/xml/
     *  com/rslakra/appsuite/common/xml/XmlUtils.class
     *  com/rslakra/appsuite/common/Payload.class
     *  com/rslakra/appsuite/common/Sets.class
     *  com/rslakra/appsuite/common/exception/
     *  com/rslakra/appsuite/common/exception/ServerRuntimeException.class
     *  com/rslakra/appsuite/common/exception/InvalidRequestException.class
     *  application.properties
     * </pre>
     *
     * @throws IOException
     */
    @Test
    public void testGetJarEntries() throws IOException {
        LOGGER.debug("+testGetJarEntries()");
        Set<String> expectedJarEntries = Sets.asSet("META-INF/", "META-INF/MANIFEST.MF", "com/", "com/rslakra/", "com/rslakra/appsuite/", "com/rslakra/appsuite/core/", "com/rslakra/appsuite/core/CharSets.class", "com/rslakra/appsuite/core/MathUtils.class", "com/rslakra/appsuite/core/UnsafeUtils.class", "com/rslakra/appsuite/core/Pair.class", "com/rslakra/appsuite/core/HashUtils.class", "com/rslakra/appsuite/core/ArrayIterator.class", "com/rslakra/appsuite/core/xml/", "com/rslakra/appsuite/core/xml/XmlUtils.class", "com/rslakra/appsuite/core/Payload.class", "com/rslakra/appsuite/core/Sets.class", "com/rslakra/appsuite/core/exception/", "com/rslakra/appsuite/core/exception/ServerRuntimeException.class", "com/rslakra/appsuite/core/exception/InvalidRequestException.class", "application.properties");

        LOGGER.debug("expectedJarEntries: {}", expectedJarEntries);

        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        List<String> versionContents = IOUtils.readAllLines(versionFilePath);
        LOGGER.debug("versionContents={}", versionContents);
        assertNotNull(versionContents);
        assertNotNull(versionContents.get(0));
        assertTrue(versionContents.size() > 0);

        // reads .jar file
        String jarFileName = "appsuite-core-{}.jar".replace("{}", versionContents.get(0));
        LOGGER.debug("jarFileName={}", jarFileName);
        String jarFilePath = IOUtils.pathWithWorkingDir("target", jarFileName);
        LOGGER.debug("jarFilePath={}", jarFilePath);
        assertNotNull(jarFilePath);
        Set<String> jarEntries = IOUtils.getJarEntries(jarFilePath);
        LOGGER.debug("jarEntries={}", jarEntries);
        assertTrue(jarEntries.containsAll(expectedJarEntries));
        // assertEquals(expectedJarEntries, jarEntries);
        LOGGER.debug("-testGetJarEntries()");
    }


    /**
     * Jar Class File Names
     *
     * @throws IOException
     */
    @Test
    public void testGetJarFileClassNames() throws IOException {
        LOGGER.debug("+testGetJarFileClassNames()");
        Set<String> expectedClassNames = Sets.asSet("com.rslakra.appsuite.core.CharSets", "com.rslakra.appsuite.core.MathUtils", "com.rslakra.appsuite.core.UnsafeUtils", "com.rslakra.appsuite.core.Pair", "com.rslakra.appsuite.core.HashUtils", "com.rslakra.appsuite.core.ArrayIterator", "com.rslakra.appsuite.core.xml.XmlUtils", "com.rslakra.appsuite.core.Payload", "com.rslakra.appsuite.core.Sets", "com.rslakra.appsuite.core.exception.ServerRuntimeException", "com.rslakra.appsuite.core.exception.InvalidRequestException");

        LOGGER.debug("expectedClassNames={}", expectedClassNames);

        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        List<String> versionContents = IOUtils.readAllLines(versionFilePath);
        LOGGER.debug("versionContents={}", versionContents);
        assertNotNull(versionContents);
        assertNotNull(versionContents.get(0));
        assertTrue(versionContents.size() > 0);

        // reads .jar file
        String jarFileName = "appsuite-core-{}.jar".replace("{}", versionContents.get(0));
        LOGGER.debug("jarFileName={}", jarFileName);
        String jarFilePath = IOUtils.pathWithWorkingDir("target", jarFileName);
        LOGGER.debug("jarFilePath={}", jarFilePath);
        assertNotNull(jarFilePath);

        try {
            Set<String> jarFileClassNames = IOUtils.getJarFileClassNames(jarFilePath);
            LOGGER.debug("jarFileClassNames={}", jarFileClassNames);
            assertTrue(jarFileClassNames.containsAll(expectedClassNames));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.debug("-testGetJarFileClassNames()");
    }

    /**
     * Jar Class File Names
     *
     * @throws IOException
     */
    @Test
    public void testGetJarFileClasses() throws IOException, ClassNotFoundException {
        LOGGER.debug("+testGetJarFileClasses()");
        Set<String> expectedClassNames = Sets.asSet("com.rslakra.appsuite.core.CharSets", "com.rslakra.appsuite.core.MathUtils", "com.rslakra.appsuite.core.UnsafeUtils", "com.rslakra.appsuite.core.Pair", "com.rslakra.appsuite.core.HashUtils", "com.rslakra.appsuite.core.ArrayIterator", "com.rslakra.appsuite.core.xml.XmlUtils", "com.rslakra.appsuite.core.Payload", "com.rslakra.appsuite.core.Sets", "com.rslakra.appsuite.core.exception.ServerRuntimeException", "com.rslakra.appsuite.core.exception.InvalidRequestException");

        LOGGER.debug("expectedClassNames: {}", expectedClassNames);
        File jarFile = new File(IOUtils.pathString(IOUtils.getUserDir(), "target/test-classes/jarFile.jar"));
        LOGGER.debug("jarFile: {}", jarFile.getAbsolutePath());

        String versionFilePath = IOUtils.pathWithWorkingDir("version.txt");
        LOGGER.debug("versionFilePath={}", versionFilePath);
        assertNotNull(versionFilePath);
        assertTrue(versionFilePath.endsWith("appsuite-core/version.txt"));
        List<String> versionContents = IOUtils.readAllLines(versionFilePath);
        LOGGER.debug("versionContents={}", versionContents);
        assertNotNull(versionContents);
        assertNotNull(versionContents.get(0));
        assertTrue(versionContents.size() > 0);

        // reads .jar file
        String jarFileName = "appsuite-core-{}.jar".replace("{}", versionContents.get(0));
        LOGGER.debug("jarFileName={}", jarFileName);
        String jarFilePath = IOUtils.pathWithWorkingDir("target", jarFileName);
        LOGGER.debug("jarFilePath={}", jarFilePath);
        assertNotNull(jarFilePath);

        //get jar file classes
        Set<Class> jarFileClasses = IOUtils.getJarFileClasses(jarFilePath);
        LOGGER.debug("jarFileClasses={}", jarFileClasses);
        Set<String> jarClasses = jarFileClasses.stream().map(Class::getName).collect(Collectors.toSet());
        LOGGER.debug("jarClasses={}", jarClasses);
        assertTrue(jarClasses.containsAll(expectedClassNames));
        LOGGER.debug("-testGetJarFileClasses()");
    }
}
