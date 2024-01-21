/******************************************************************************
 * Copyright (C) Devamatre 2009 - 2022. All rights reserved.
 *
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
 *
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
 *
 * Devamatre reserves the right to modify the technical specifications and or 
 * features without any prior notice.
 *****************************************************************************/
package com.devamatre.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class handles the file handling operations.
 *
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created Apr 22, 2015 4:52:15 PM
 */
public enum IOUtils {
    INSTANCE;

    // LOGGER
    private static Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TARGET = "target";
    public static final String BUILD = "build";

    /**
     * BUFFER_1K
     */
    public static final int BUFFER_1K = 1024;
    /**
     * BUFFER_4K
     */
    public static final int BUFFER_4K = 4 * BUFFER_1K;
    /**
     * BUFFER_8K
     */
    public static final int BUFFER_8K = 8 * BUFFER_1K;
    /**
     * BUFFER_20K
     */
    public static final int BUFFER_20K = 20 * BUFFER_1K;

    /**
     * SLASH
     */
    public static final String SLASH = "/";
    /**
     * NEWLINE
     */
    public static final String NEWLINE = "\n";
    /**
     * HEX_DIGIT_CHARS
     */
    public static final String HEX_DIGITS = "0123456789ABCDEF";

    /**
     * imageTypes
     */
    private final List<String> imageTypes = new ArrayList<>();

    // ALL_PERMISSIONS
    public static FileAttribute<Set<PosixFilePermission>> ALL_PERMISSIONS = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE));

    // WRITE_PERMISSION
    public static FileAttribute<Set<PosixFilePermission>> WRITE_PERMISSION = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));

    // READ_PERMISSION
    public static FileAttribute<Set<PosixFilePermission>> READ_PERMISSION = PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ));

    /**
     * Returns the OS name.
     *
     * @return
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * Returns the Java VM Name.
     *
     * @return
     */
    public static String getJVMName() {
        return System.getProperty("java.vm.name");
    }

    /**
     * @return
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * @return
     */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
     * @param pathSegments
     * @return
     */
    public static String getBuildDir(final String... pathSegments) {
        final StringBuilder pathBuilder = new StringBuilder(getUserDir());
        if (BeanUtils.isNotEmpty(pathSegments)) {
            for (int index = 0; index < pathSegments.length; index++) {
                pathBuilder.append(File.separator).append(pathSegments[index]);
            }
        }

        return pathBuilder.toString();
    }

    /**
     * @return
     */
    public static String getJavaTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Returns the system file encoding.
     *
     * @return
     */
    public static String getFileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * @param logString
     */
    public static void debug(String logString) {
        System.out.println(logString);
    }

    /**
     * @param logString
     */
    public static void error(String logString) {
        System.err.println(logString);
    }

    /**
     * @param ex
     */
    public static void error(Exception ex) {
        System.err.println(ex);
    }

    /**
     * Returns the path of the given class.
     *
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> String getClassFilePath(final Class<T> classType) {
        if (BeanUtils.isNotNull(classType)) {
            String filePath = BeanUtils.getClassPath(classType, false);
            if (BeanUtils.isNotEmpty(filePath)) {
                URL url = classType.getClassLoader().getResource(filePath);
                if (BeanUtils.isNotNull(url)) {
                    filePath = url.toExternalForm();
                    filePath = filePath.replace(" ", "%20");
                    try {
                        URI uri = URI.create(filePath);
                        if (BeanUtils.isEmpty(uri.getPath())) {
                            filePath = uri.toString();
                            if (filePath.startsWith("jar:file:")) {
                                // Update path and define ZIP file
                                filePath = filePath.substring(filePath.indexOf("file:/"));
                                filePath = filePath.substring(0, filePath.toLowerCase().indexOf(".jar") + 4);
                                // Check is UNC path string
                                if (filePath.startsWith("file://")) {
                                    filePath = filePath.substring(filePath.indexOf("file:/") + 6);
                                }
                                filePath = URI.create(filePath).getPath();
                            }
                        } else {
                            filePath = uri.getPath();
                        }
                    } catch (IllegalArgumentException ex) {
                        LOGGER.error(ex.getLocalizedMessage(), ex);
                    }
                }
            }

            return filePath;
        }

        return null;
    }

    /**
     * Returns the full path for the directory and childName.
     *
     * @param parentFolder
     * @param childName
     * @return
     */
    public static String pathString(String parentFolder, String childName) {
        String pathString = null;
        if (BeanUtils.isEmpty(parentFolder)) {
            throw new IllegalArgumentException("Parent directory should not be null/empty!");
        } else {
            /* Removes unnecessary spaces from parentFolder and fileName. */
            pathString = parentFolder.trim();
            if (BeanUtils.isNotEmpty(childName)) {
                pathString += (childName.startsWith(SLASH) ? "" : File.separator) + childName.trim();
            }
        }

        return pathString;
    }

    /**
     * Returns the path of the given file, if its not null otherwise null.
     *
     * @param file
     * @return
     */
    public static String getFilePath(File file) {
        return (file == null ? null : file.getAbsolutePath());
    }

    /**
     * Writes the file on the specified path and populated with the provided data.
     *
     * @param path
     * @param data
     * @return
     */
    public static boolean writeFile(String path, byte[] data) {
        boolean result = false;
        if (BeanUtils.isEmpty(path)) {
            throw new NullPointerException("Path must be provided!");
        }

        if (BeanUtils.isEmpty(data)) {
            throw new NullPointerException("data must be provided!");
        }

        try {
            result = saveFile(data, path);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        return delete(path);
    }

    /**
     * @param folderPath
     * @return
     */
    public static boolean deleteFolder(String folderPath) {
        return delete(folderPath, true);
    }

    /**
     * @param fileOrFolder
     * @return
     */
    public static boolean deleteFolder(File fileOrFolder) {
        return delete(fileOrFolder, true);
    }

    /**
     * Returns the bytes of the specified file, if exists, otherwise null.
     *
     * @param file
     * @return
     */
    public static byte[] readFile(File file) {
        byte[] bytes = null;
        if (BeanUtils.isNotNull(file)) {
            // Read file into buffer
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
                long length = randomAccessFile.length();
                bytes = new byte[(int) length];
                randomAccessFile.readFully(bytes);
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                closeSilently(randomAccessFile);
            }
        }

        return bytes;
    }

    /**
     * Returns the bytes of the specified path, if exists, otherwise null.
     *
     * @param path
     * @return
     */
    public static byte[] readFile(String path) {
        return (BeanUtils.isEmpty(path) ? null : readFile(new File(path)));
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readContents(final InputStream inputStream) throws IOException {
        final StringBuilder sBuilder = new StringBuilder();
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line);
            }
        }

        return sBuilder.toString();
    }

    /**
     * @param fileName
     * @return
     */
    public static String readContents(final String fileName) {
        try {
            return readContents(INSTANCE.getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns true if the given extension exists in the list otherwise false.
     *
     * @param extension
     * @param forceReload
     * @return
     */
    private boolean isImageType(String extension, boolean forceReload) {
        // populate with supported images types.
        if (BeanUtils.isEmpty(imageTypes)) {
            imageTypes.add("tif");
            imageTypes.add("tiff");
            imageTypes.add("jpg");
            imageTypes.add("jpeg");
            imageTypes.add("png");
            imageTypes.add("pcd");
            imageTypes.add("psd");
            imageTypes.add("tga");
            imageTypes.add("dcx");
            imageTypes.add("emf");
            imageTypes.add("wmf");
            imageTypes.add("gif");
            imageTypes.add("bmp");
            imageTypes.add("ico");
            imageTypes.add("svg");
            imageTypes.add("cur");
        }

        // extract the extension of the image type
        if (extension.startsWith(".") && extension.length() > 1) {
            extension = extension.substring(1);
        }

        return imageTypes.contains(extension);
    }

    /**
     * Returns true if the given extension exists in the list otherwise false.
     *
     * @param extension
     * @return
     */
    public static boolean isImageType(String extension) {
        return INSTANCE.isImageType(extension, false);
    }

    /**
     * Loads the properties file pointing to an inputStream.
     *
     * @param inputStream
     * @return
     */
    public static Properties loadProperties(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return properties;
    }

    /**
     * Loads the specified properties file. All the android specific properties should be kept in the assets folder and
     * just pass the name of the properties file
     *
     * @param filePath
     * @return
     */
    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try {
            properties = loadProperties(new FileInputStream(filePath));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return properties;
    }

    /**
     * Returns the Properties object prepared from the given dataBytes.
     *
     * @param dataBytes
     * @return
     */
    public static Properties createProperties(byte[] dataBytes) {
        return loadProperties(new ByteArrayInputStream(dataBytes));
    }

    /**
     * Saves the specified properties into the specified file path.
     *
     * @param filePath
     * @param properties
     */
    public static void saveProperties(String filePath, Properties properties) {
        if (BeanUtils.isNotEmpty(filePath)) {
            FileOutputStream outputStream = null;
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    boolean fileCreated = file.createNewFile();
                    if (!fileCreated) {
                        LOGGER.warn("Unable to create the properties file:{}", filePath);
                    }
                }
                outputStream = new FileOutputStream(file);
                properties.store(outputStream, null);
                outputStream.flush();
                LOGGER.info("Saved properties file:{}", filePath);
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            } finally {
                closeSilently(outputStream);
            }
        }
    }

    /**
     * Prints the specified properties.
     *
     * @param properties
     */
    public static void printProperties(Properties properties) {
        if (properties != null) {
            Enumeration<Object> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = (String) properties.get(key);
                System.out.println(key + ": " + value);
            }
        }
    }

    /**
     * Merges the given sourceProperties into the given targetProperties.
     *
     * @param sourceProperties
     * @param targetProperties
     * @return
     */
    public static Properties mergeProperties(Properties sourceProperties, Properties targetProperties) {
        System.out.println("Merging properties ...");
        if (BeanUtils.isNull(targetProperties)) {
            targetProperties = new Properties();
        }

        // if not null, merge it into target.
        if (BeanUtils.isNotNull(sourceProperties)) {
            targetProperties.putAll(sourceProperties);
        }

        return targetProperties;
    }

    /**
     * Returns true if the specified file exists, otherwise false.
     *
     * @param filePath
     */
    public static boolean isExist(String filePath) {
        return (BeanUtils.isNotEmpty(filePath) && new File(filePath).exists());
    }

    /**
     * Returns true if the specified file exists, otherwise false.
     *
     * @param file
     */
    public static boolean isExist(File file) {
        return (file != null && file.exists());
    }

    /**
     * Returns true if the specified file is a directory, otherwise false.
     *
     * @param file
     */
    public static boolean isDirectory(File file) {
        return (file != null && file.isDirectory());
    }

    /**
     * Returns true if the specified file exists and is a directory, otherwise false.
     *
     * @param file
     */
    public static boolean isExistAndFolder(File file) {
        return (isExist(file) && file.isDirectory());
    }

    /**
     * Creates the buffer with the available size if its greater than the defaultSize.
     *
     * @param available
     * @param defaultSize
     * @return
     */
    public static byte[] getBuffer(int available, int defaultSize) {
        return new byte[(available > defaultSize ? available : defaultSize)];
    }

    /**
     * @param available
     * @return
     */
    public static byte[] getBuffer(int available) {
        return getBuffer(available, BUFFER_20K);
    }

    /**
     * Copies the contents of an <code>sourceStream</code> into an
     * <code>targetStream</code>.
     *
     * @param sourceStream
     * @param targetStream
     * @param closeStreams
     * @return
     * @throws IOException
     */
    public static int copyStream(InputStream sourceStream, OutputStream targetStream, boolean closeStreams) throws IOException {
        LOGGER.debug("+copyStream({}, {}, {})", sourceStream, targetStream, closeStreams);
        int fileSize = 0;
        if (BeanUtils.isNotNull(sourceStream) && BeanUtils.isNotNull(targetStream)) {
            try {
                byte[] buffer = getBuffer(sourceStream.available());
                int byteCount = 0;
                while ((byteCount = sourceStream.read(buffer)) != -1) {
                    targetStream.write(buffer, 0, byteCount);
                    fileSize += byteCount;
                }

                /* flush output streams. */
                targetStream.flush();
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw ex;
            } finally {
                /* close streams. */
                if (closeStreams) {
                    closeSilently(sourceStream, targetStream);
                }
            }
        }

        LOGGER.debug("-copyStream(), fileSize:{}", fileSize);
        return fileSize;
    }

    /**
     * Writes the <code>bytes</code> to <code>outputStream</code> and closes it.
     *
     * @param dataBytes
     * @param outputStream
     * @param closeStream
     * @throws IOException
     */
    public static boolean writeBytes(byte[] dataBytes, OutputStream outputStream, boolean closeStream) throws IOException {
        LOGGER.debug("+writeBytes({}, {}, {})", dataBytes, outputStream, closeStream);
        boolean result = false;
        if (BeanUtils.isNotEmpty(dataBytes) && BeanUtils.isNotNull(outputStream)) {
            try {
                outputStream.write(dataBytes);
                /* flush output streams. */
                outputStream.flush();
                result = true;
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw ex;
            } finally {
                /* close streams. */
                if (closeStream) {
                    closeSilently(outputStream);
                }
            }
        }

        LOGGER.debug("-writeBytes(), result:{}", result);
        return result;
    }

    /**
     * Writes the <code>file</code> contents to the given
     * <code>outputStream</code> and flush the results.
     *
     * @param file
     * @param outputStream
     * @throws IOException
     */
    public static void sendLocalFile(File file, OutputStream outputStream) throws IOException {
        System.out.println("+sendLocalFile(" + file + ", " + outputStream + ")");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] buffer = getBuffer(inputStream.available());
            int byteCount = 0;
            while ((byteCount = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteCount);
            }

            // flush output streams.
            outputStream.flush();
        } catch (IOException ex) {
            System.err.println(ex);
            throw ex;
        } finally {
            /* close streams. */
            closeSilently(inputStream, outputStream);
        }

        System.out.println("-sendLocalFile()");
    }

    /**
     * Copies the contents of an <code>sourceStream</code> into an
     * <code>targetFile</code>.
     *
     * @param sourceFile
     * @param targetFile
     * @param closeStreams
     * @return
     * @throws IOException
     */
    public static int copyFile(FileInputStream sourceFile, FileOutputStream targetFile, boolean closeStreams) throws IOException {
        System.out.println("+copyFile(" + sourceFile + ", " + targetFile + ", " + closeStreams + ")");
        int fileSize = 0;
        if (sourceFile != null && targetFile != null) {
            try {
                byte[] buffer = getBuffer(sourceFile.available());
                int byteCount = 0;
                while ((byteCount = sourceFile.read(buffer)) != -1) {
                    targetFile.write(buffer, 0, byteCount);
                    fileSize += byteCount;
                }

                // flush output streams.
                targetFile.flush();
            } catch (IOException ex) {
                System.err.println(ex);
                throw ex;
            } finally {
                /* close streams. */
                if (closeStreams) {
                    closeSilently(sourceFile, targetFile);
                }
            }
        }

        System.out.println("-copyFile(), fileSize:" + fileSize);
        return fileSize;
    }

    /**
     * Saves the input stream contents into the specified file path.
     *
     * @param inputStream
     * @param filePath
     */
    public static void saveFile(InputStream inputStream, String filePath) {
        // System.out.println("saveFile(" + inputStream + ", " + filePath +
        // ")");
        if (BeanUtils.isNotNull(inputStream) && BeanUtils.isNotEmpty(filePath)) {
            OutputStream outputStream = null;
            int fileSize = 0;
            try {
                File file = new File(filePath);
                /* create the file if it does not exist. */
                if (!file.exists()) {
                    file.createNewFile();
                }

                // write the contents of the input stream to output stream
                outputStream = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = getBuffer(inputStream.available());
                int byteCount = 0;
                while ((byteCount = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteCount);
                    fileSize += byteCount;
                }

                // flush output streams.
                outputStream.flush();
                System.out.println("File [" + filePath + "] saved successfully! fileSize:" + fileSize);
            } catch (Exception ex) {
                System.err.println(ex);
            } finally {
                closeSilently(inputStream, outputStream);
            }
        }
    }

    /**
     * Saves the input into the specified file.
     *
     * @param input
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean saveFile(byte[] input, File file) throws IOException {
        // System.out.println("+saveFile(" + input + ", " + file + ")");
        boolean result = false;
        if (BeanUtils.isNotNull(file) && BeanUtils.isNotEmpty(input)) {
            FileOutputStream outputStream = null;
            try {
                System.out.println("Writing file:" + file.getAbsolutePath());
                // make sure the parent directories exists.
                makeFolder(file.getParentFile());

                /* create the file if it does not exist. */
                if (!file.exists()) {
                    boolean newFileCreated = file.createNewFile();
                    if (!newFileCreated) {
                        System.out.println("Unable to create the file:" + file.getAbsolutePath());
                    }
                }

                outputStream = new FileOutputStream(file);
                outputStream.write(input);
                outputStream.flush();
                result = true;
            } catch (IOException ex) {
                System.err.println(ex);
                throw ex;
            } finally {
                closeSilently(outputStream);
            }
        }

        // System.out.println("-saveFile(), result:" + result);
        return result;
    }

    /**
     * Saves the input into the specified file.
     *
     * @param input
     * @param filePath
     * @return
     * @throws IOException
     */
    public static boolean saveFile(byte[] input, String filePath) throws IOException {
        return saveFile(input, new File(filePath));
    }

    /**
     * Creates the folder if it does not exist.
     *
     * @param folder
     */
    public static File makeFolder(File folder) {
        if (BeanUtils.isNotNull(folder)) {
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    LOGGER.info("Unable to create '{}' folder!", folder.getAbsolutePath());
                }
            }
        }

        return folder;
    }

    /**
     * Creates the folder if it's not exists. If <code>override</code> is set to true, delete the existing folder and
     * creates the new one.
     *
     * @param folder
     * @param override
     */
    public static File makeFolder(File folder, boolean override) {
        LOGGER.debug("+makeFolder({}, {})", folder, override);
        if (BeanUtils.isNotNull(folder)) {
            if (folder.exists()) {
                LOGGER.warn("Folder '" + folder.getAbsolutePath() + "' already exists.");
                if (folder.isDirectory() && override) {
                    if (!delete(folder, override)) {
                        LOGGER.error("Unable to delete '{}' folder!", folder.getAbsolutePath());
                    }

                    // recreate the folder
                    folder = makeFolder(folder);
                }
            } else {
                // create the folder
                folder = makeFolder(folder);
            }
        }

        LOGGER.debug("-makeFolder(), folder:{}", (folder == null ? "" : folder.getAbsolutePath()));
        return folder;
    }

    /**
     * Creates the directory if not exists. If <code>override</code> is set to true, delete the existing directory and
     * creates the new one.
     *
     * @param dirPath
     * @param override
     * @return
     */
    public static File makeFolder(String dirPath, boolean override) {
        return makeFolder(new File(dirPath), override);
    }

    /**
     * Creates the directory if not exists. If <code>override</code> is set to true, delete the existing directory and
     * creates the new one.
     *
     * @param dirPath
     */
    public static File makeFolder(String dirPath) {
        return makeFolder(dirPath, false);
    }

    /**
     * Deletes the contents of the specified <code>path</code> irrespective of its contents, if its not null or empty.
     *
     * @param path
     * @param force
     */
    public static boolean delete(File path, boolean force) {
        System.out.println("+delete(" + path + ", " + force + ")");
        boolean deleted = false;
        if (isExist(path)) {
            if (path.isDirectory()) {
                /* check if any file exist in directory. */
                File[] files = path.listFiles();
                if (files != null) {
                    /* if exist, then delete all files. */
                    for (File file : files) {
                        if (file.isDirectory()) {
                            /* call recursive to delete folders. */
                            deleted = delete(file, force);
                        } else if (file.exists()) {
                            /* delete file. */
                            deleted = file.delete();
                        }
                    }
                }

                /* finally delete the folder. */
                deleted = path.delete();
            } else if (path.isFile()) {
                deleted = path.delete();
            }
        }

        System.out.println("-delete(), deleted:" + deleted);
        return deleted;
    }

    /**
     * Deletes the contents of the specified <code>path</code> irrespective of its contents, if its not null or empty.
     *
     * @param path
     * @param force
     */
    public static boolean delete(String path, boolean force) {
        /* check the file path is not null or not empty. */
        if (BeanUtils.isNotEmpty(path)) {
            return delete(new File(path), force);
        }

        return false;
    }

    /**
     * Deletes the contents of the specified <code>path</code> irrespective of its contents, if its not null or empty.
     *
     * @param path
     */
    public static boolean delete(String path) {
        return delete(path, false);
    }

    /**
     * Returns the file {@link InputStream}.
     *
     * @param pathString
     * @return
     * @throws IOException
     */
    public static InputStream newFileInputStream(String pathString) throws IOException {
        return (BeanUtils.isEmpty(pathString) ? null : new FileInputStream(pathString));
    }

    /**
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream newFileInputStream(File file) throws IOException {
        return (BeanUtils.isNull(file) ? null : new FileInputStream(file));
    }

    /**
     * Returns the file {@link InputStream}.
     *
     * @param dataBytes
     * @return
     * @throws IOException
     */
    public static InputStream newByteArrayInputStream(byte[] dataBytes) throws IOException {
        return (BeanUtils.isNull(dataBytes) ? null : new ByteArrayInputStream(dataBytes));
    }

    /**
     * Make a BufferedReader to get incoming data.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public static BufferedReader newBufferedReader(Socket socket) throws IOException {
        return (new BufferedReader(new InputStreamReader(socket.getInputStream())));
    }

    /**
     * Make a PrintWriter to send outgoing data. This PrintWriter will automatically flush stream when
     * <code>println(...)</code> is called.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public static PrintWriter newPrintWriter(Socket socket) throws IOException {
        /* autoFlush set to be true */
        return (new PrintWriter(socket.getOutputStream(), true));
    }

    /**
     * Returns the input stream populated with the given bytes.
     *
     * @param dataBytes
     * @return
     */
    public static InputStream toInputStream(byte[] dataBytes) {
        try {
            return newByteArrayInputStream(dataBytes);
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    /**
     * Returns the bytes of the specified input stream.
     *
     * @param inputStream
     * @return
     */
    public static byte[] readBytes(final InputStream inputStream, final boolean closeStream) {
        System.out.println("+readBytes(" + inputStream + ", " + closeStream + ")");
        byte[] dataBytes = null;
        if (inputStream != null) {
            ByteArrayOutputStream byteStream = null;
            try {
                byteStream = new ByteArrayOutputStream();
                byte[] buffer = getBuffer(inputStream.available(), BUFFER_20K);
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteStream.write(buffer, 0, length);
                }

                byteStream.flush();
                dataBytes = byteStream.toByteArray();
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                /* close streams. */
                closeSilently(byteStream);
                if (closeStream) {
                    closeSilently(inputStream);
                }
            }
        }

        System.out.println("-readBytes(), dataBytes:" + dataBytes);
        return dataBytes;
    }

    /**
     * /** Returns the UTF-8 String representation of the given
     * <code>bytes</code>.
     *
     * @param bytes
     * @param replaceNonDigitChars
     * @return
     */
    public static String toUTF8String(byte[] bytes, boolean replaceNonDigitChars) {
        String utf8String = toString(bytes, CharSets.UTF_8);
        if (replaceNonDigitChars && BeanUtils.isNotEmpty(utf8String)) {
            utf8String = utf8String.replaceAll("\\D+", "");
        }

        return utf8String;
    }

    /**
     * Returns the UTF-8 String representation of the given <code>bytes</code>.
     *
     * @param bytes
     * @return
     */
    public static String toUTF8String(byte[] bytes) {
        return toUTF8String(bytes, false);
    }

    /**
     * Returns the UTF-8 String representation of the given <code>string</code>.
     *
     * @param string
     * @return
     */
    public static String toUTF8String(String string) {
        return toUTF8String(string.getBytes());
    }

    /**
     * Returns the ISO-8859-1 String representation of the given
     * <code>bytes</code>.
     *
     * @param bytes
     * @return
     */
    public static String toISOString(final byte[] bytes) {
        return toString(bytes, CharSets.ISO_8859_1);
    }

    /**
     * Converts the specified <code>string</code> into bytes using the specified
     * <code>charset</code>.
     *
     * @param string
     * @param charSets
     * @return
     */
    public static byte[] toBytes(final String string, final CharSets charSets) {
        byte[] dataBytes = null;
        if (BeanUtils.isNotNull(string)) {
            try {
                dataBytes = BeanUtils.isNull(charSets) ? string.getBytes() : string.getBytes(charSets.toCharset());
            } catch (Exception ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }

        return dataBytes;
    }

    /**
     * Converts the specified <code>string</code> into bytes.
     *
     * @param string
     * @return
     */
    public static byte[] toBytes(String string) {
        return toBytes(string, null);
    }

    /**
     * Returns the UTF-8 bytes for the given string.
     *
     * @param string
     * @return
     */
    public static byte[] toUTF8Bytes(final String string) {
        return toBytes(string, CharSets.UTF_8);
    }

    /**
     * Returns the ISO-8859-1 bytes for the given string.
     *
     * @param string
     * @return
     */
    public static byte[] toISOBytes(final String string) {
        return toBytes(string, CharSets.ISO_8859_1);
    }

    /**
     * Returns the string of the specified input stream.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        return toUTF8String(readBytes(inputStream));
    }

    /**
     * Returns the defaultCharset, if the given charsetName is either null or empty otherwise the same.
     *
     * @param charsetName
     * @return
     */
    public static String defaultCharset(final String charsetName) {
        return (BeanUtils.isEmpty(charsetName) ? Charset.defaultCharset().displayName() : charsetName);
    }

    /**
     * Converts the specified <code>bytes</code> to the specified
     * <code>charsetName</code> String.
     *
     * @param bytes
     * @param charSets
     * @return
     */
    public static String toString(final byte[] bytes, final CharSets charSets) {
        String bytesAsString = null;
        if (BeanUtils.isNotEmpty(bytes)) {
            try {
                bytesAsString = BeanUtils.isNull(charSets) ? new String(bytes) : new String(bytes, charSets.toCharset());
            } catch (Exception ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
                bytesAsString = Objects.toString(bytes);
            }
        }

        return bytesAsString;
    }

    /**
     * Returns the string of the given strings array.
     *
     * @param strings
     * @return
     */
    public static String toString(final String... strings) {
        StringBuilder sBuilder = new StringBuilder();
        if (BeanUtils.isEmpty(strings)) {
            sBuilder.append("[]");
        } else {
            sBuilder.append("[");
            for (int i = 0; i < strings.length; i++) {
                sBuilder.append(strings[i]);
                if (i < (strings.length - 1)) {
                    sBuilder.append(",");
                }
            }

            sBuilder.append("]");
        }

        return sBuilder.toString();
    }

    /**
     * Convert a byte array to a HEXA String of the format "1f 30 b7".
     *
     * @param bytes
     * @return
     */
    public static String toHexString(final byte[] bytes) {
        String hexString = null;
        if (BeanUtils.isNotEmpty(bytes)) {
            StringBuilder hexBuilder = new StringBuilder(bytes.length * 2);
            for (int index = 0; index < bytes.length; index++) {
                int hn = ((int) (bytes[index]) & 0x00ff) / 16;
                int ln = ((int) (bytes[index]) & 0x000f);

                hexBuilder.append(HEX_DIGITS.charAt(hn));
                hexBuilder.append(HEX_DIGITS.charAt(ln));
            }

            hexString = hexBuilder.toString();
            // available for GC.
            hexBuilder = null;
        }

        return hexString;
    }

    /**
     * Convert the hex string into an array of bytes.
     *
     * @param hexString
     * @return
     */
    public static byte[] toHexBytes(final String hexString) {
        byte[] hexBytes = null;
        if (BeanUtils.isNotEmpty(hexString)) {
            int length = hexString.length() / 2;
            hexBytes = new byte[length];
            for (int i = 0; i < length; i++) {
                hexBytes[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            }
        }

        return hexBytes;
    }

    /**
     * Reads the specified file bytes.
     *
     * @param fileName
     * @return
     */
    public static byte[] readBytes(final String fileName) throws IOException {
        return readBytes(newFileInputStream(fileName), true);
    }

    /**
     * This method writes the specified string into the specified output stream.
     *
     * @param outputStream
     * @param parameters
     * @throws IOException
     */
    public static void writeStringToOutputStream(OutputStream outputStream, String parameters) throws IOException {
        if (outputStream != null) {
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeBytes(parameters);
                dataOutputStream.flush();
            } catch (IOException ex) {
                System.err.println(ex);
                throw ex;
            } finally {
                closeSilently(dataOutputStream);
            }
        }
    }

    /**
     * Converts the bytes to StringBuilder object.
     *
     * @param bytes
     * @return StringBuilder
     */
    public static StringBuilder getBytesAsStringBuilder(final byte... bytes) {
        StringBuilder sBuilder = new StringBuilder();
        if (bytes != null) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
                String inputLine = null;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    sBuilder.append(inputLine);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                closeSilently(bufferedReader);
            }
        }

        return sBuilder;
    }

    /**
     * Copies the source file at the target file.
     *
     * @param sourceFilePath
     * @param targetFilePath
     * @return
     * @throws IOException
     */
    public static boolean copyFile(String sourceFilePath, String targetFilePath) throws IOException {
        LOGGER.debug("+copyFile({}, {})", sourceFilePath, targetFilePath);
        boolean copied = false;
        if (BeanUtils.isNotEmpty(sourceFilePath) && BeanUtils.isNotEmpty(targetFilePath)) {
            File srcFile = new File(sourceFilePath);
            if (srcFile.exists()) {
                int fileSize = copyFile(new FileInputStream(srcFile), new FileOutputStream(targetFilePath), true);
                if (fileSize > 0) {
                    copied = true;
                    LOGGER.debug("File [{}] copied successfully! fileSize:{}", sourceFilePath, fileSize);
                }
            } else {
                System.out.println("File [" + sourceFilePath + "] does not exist!");
            }
        }

        System.out.println("-copyFile(), copied:" + copied);
        return copied;
    }

    /**
     * @param outputStream
     * @param object
     * @param compress
     * @throws Exception
     */
    public static void writeObject(OutputStream outputStream, Object object, boolean compress) throws IOException {
        if (outputStream != null) {
            ObjectOutputStream objOutputStream = null;
            try {
                objOutputStream = new ObjectOutputStream(outputStream);
                if (compress) {
                    /* avoid resources leak. */
                    objOutputStream.close();
                    outputStream = new ObjectOutputStream(new GZIPOutputStream(outputStream));
                }

                objOutputStream.writeObject(object);
                objOutputStream.flush();
            } catch (IOException ex) {
                System.err.println(ex);
                throw ex;
            } finally {
                closeSilently(objOutputStream);
            }
        }
    }

    /**
     * @param outputStream
     * @param object
     * @throws IOException
     */
    public static void writeObject(OutputStream outputStream, Object object) throws IOException {
        writeObject(outputStream, object, false);
    }

    /**
     * @param inputStream
     * @param compress
     * @return
     * @throws Exception
     */
    public static Object readObject(InputStream inputStream, boolean compress) throws Exception {
        Object object = null;
        if (inputStream != null) {
            ObjectInputStream objInputStream = null;
            try {
                objInputStream = new ObjectInputStream(inputStream);
                if (compress) {
                    /* avoid resources leak. */
                    objInputStream.close();
                    objInputStream = new ObjectInputStream(new GZIPInputStream(inputStream));
                }

                object = objInputStream.readObject();
            } catch (Exception ex) {
                System.err.println(ex);
                throw ex;
            } finally {
                closeSilently(objInputStream);
            }
        }

        return object;
    }

    /**
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Object readObject(InputStream inputStream) throws Exception {
        return readObject(inputStream, false);
    }

    /**
     * Writes the input stream data into the specified response string.
     *
     * @param inputStream
     * @param closeStreams
     * @throws IOException
     */
    public static StringBuilder streamAsStringBuilder(InputStream inputStream, boolean closeStreams) throws IOException {
        System.out.println("+streamAsStringBuilder(" + inputStream + ", " + closeStreams + ")");
        StringBuilder streamString = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = null;
            while ((inputLine = bufferedReader.readLine()) != null) {
                streamString.append(inputLine);
            }

            if (closeStreams) {
                closeSilently(bufferedReader);
            }
        }

        System.out.println("-streamAsStringBuilder(), streamString:" + streamString.toString());
        return streamString;
    }

    /**
     * Writes the input stream data into the specified response string.
     *
     * @param inputStream
     * @param closeStreams
     * @param useExistingFile
     * @param hashCodeFilePath
     * @return
     * @throws IOException
     */
    public static StringBuilder writeResponse(InputStream inputStream, boolean closeStreams, boolean useExistingFile, String hashCodeFilePath) throws IOException {
        System.out.println("+writeResponse(" + inputStream + ", " + closeStreams + ", " + useExistingFile + ", " + hashCodeFilePath + ")");
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = null;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

            if (closeStreams) {
                closeSilently(bufferedReader);
            }
        }

        System.out.println("-writeResponse(), response:" + response.toString());
        return response;
    }

    /**
     * Returns the validated file or folder/directory name as per the file management naming conventions.
     *
     * @param fileName
     * @return
     */
    public static String validateFileOrFolderName(String fileName) {
        // populate with supported images types.
        if (BeanUtils.isNotEmpty(fileName)) {
            fileName = fileName.replace("<", "_");
            fileName = fileName.replace(">", "_");
            fileName = fileName.replace(":", "_");
            fileName = fileName.replace("\"", "_");
            fileName = fileName.replace("\\", "_");
            fileName = fileName.replace("/", "_");
            fileName = fileName.replace("|", "_");
            fileName = fileName.replace("?", "_");
            fileName = fileName.replace("*", "_");
            fileName = fileName.replace(",", "_");
            fileName = fileName.replace("[", "_");
            fileName = fileName.replace("]", "_");
            fileName = fileName.replace("{", "_");
            fileName = fileName.replace("}", "_");
            fileName = fileName.replace("!", "_");
            fileName = fileName.replace(";", "_");
            fileName = fileName.replace("$", "_");
            fileName = fileName.replace("¢", "_");
            fileName = fileName.replace("®", "_");
            fileName = fileName.replace("™", "_");
        }

        return fileName;
    }

    /**
     * Checks whether the directory has proper read/write permissions or not.
     *
     * @param dirPath
     * @return
     */
    public static boolean hasReadWritePrivileges(String dirPath) {
        System.out.println("+hasReadWritePrivileges(" + dirPath + ")");
        boolean allowed = false;
        File dirFile = new File(dirPath);
        // if not exists, show warning.
        if (!dirFile.exists()) {
            System.out.println("Directory '" + dirPath + "' does not exist.");
        }

        // Create 'lock.rsl' file and delete to check read write permission.
        if (dirFile.exists() && dirFile.isDirectory()) {
            try {
                File lockFile = File.createTempFile("lock.rsl", null, dirFile);
                allowed = lockFile.delete();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        System.out.println("-hasReadWritePrivileges(), allowed:" + allowed);
        return allowed;
    }

    /**
     * Returns true if the fileName extension ends with any of the given extensions otherwise false.
     *
     * @param fileName
     * @param extensions
     * @return
     */
    public static boolean endsWith(String fileName, String... extensions) {
        boolean result = false;
        if (BeanUtils.isNotEmpty(fileName) && BeanUtils.isNotEmpty(extensions)) {
            for (String extension : extensions) {
                if (fileName.endsWith(extension)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns the list of all files of the given <code>extensions</code> from the given <code>directory</code> (and
     * optionally its sub-directories, if recursive is true). If the given <code>extensions</code> is null or empty, all
     * files are returned.
     *
     * @param directory
     * @param extensions
     * @param recursive
     * @return
     */
    public static List<File> listFiles(File directory, String[] extensions, boolean recursive) {
        System.out.println("+listFiles(" + directory + ", " + toString(extensions) + ", " + recursive + ")");
        List<File> listFiles = new ArrayList<File>();
        if (isExistAndFolder(directory)) {
            File[] files = directory.listFiles();
            if (BeanUtils.isNotEmpty(files)) {
                for (File file : files) {
                    if (recursive && isDirectory(file)) {
                        listFiles.addAll(listFiles(directory, extensions, recursive));
                    } else {
                        if (BeanUtils.isEmpty(extensions)) {
                            listFiles.add(file);
                        } else {
                            if (endsWith(file.getName(), extensions)) {
                                listFiles.add(file);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("-listFiles(), listFiles:" + listFiles);
        return listFiles;
    }

    /**
     * Returns the list of all files of the given <code>extensions</code> from the given <code>directory</code>. If the
     * given <code>extensions</code> is null or empty, all files are returned.
     *
     * @param directory
     * @param extensions
     * @return
     */
    public static List<File> listFiles(File directory, String... extensions) {
        return listFiles(directory, extensions, false);
    }

    /**
     * Returns the list of all files of the given <code>extensions</code> from the given <code>directory</code>. If the
     * given <code>extensions</code> is null or empty, all files are returned.
     *
     * @param directory
     * @return
     */
    public static List<File> listFiles(File directory) {
        return listFiles(directory, null, false);
    }

    /**
     * Returns the list of file names of the given <code>extensions</code> from the given <code>directory</code> (and
     * optionally its sub-directories, if recursive is true). If the given <code>extensions</code> is null or empty, all
     * files are returned.
     *
     * @param directory
     * @param extensions
     * @param recursive
     * @return
     */
    public static List<String> listFileNames(File directory, String[] extensions, boolean recursive) {
        List<String> listFiles = new ArrayList<String>();
        if (isExistAndFolder(directory)) {
            File[] files = directory.listFiles();
            if (BeanUtils.isNotEmpty(files)) {
                for (File file : files) {
                    if (recursive && isDirectory(file)) {
                        listFiles.addAll(listFileNames(directory, extensions, recursive));
                    } else {
                        if (BeanUtils.isEmpty(extensions)) {
                            listFiles.add(file.getName());
                        } else {
                            if (endsWith(file.getName(), extensions)) {
                                listFiles.add(file.getName());
                            }
                        }
                    }
                }
            }
        }

        return listFiles;
    }

    /**
     * Returns the list of all file names of the given <code>extensions</code> from the given <code>directory</code>. If
     * the given
     * <code>extensions</code> is null or empty, all files are returned.
     *
     * @param directory
     * @param extensions
     * @return
     */
    public static List<String> listFileNames(File directory, String... extensions) {
        return listFileNames(directory, extensions, false);
    }

    /**
     * Returns the list of all files of the given <code>extension</code> from the given <code>directory</code>. If the
     * given <code>extension</code> is null or empty, all files are returned.
     *
     * @param directory
     * @return
     */
    public static List<String> getAllFiles(File directory) {
        return listFileNames(directory, (String) null);
    }

    /**
     * Returns the list of all files of the given <code>extension</code> from the given <code>directory</code>. If the
     * given <code>extension</code> is null or empty, all files are returned.
     *
     * @param directory
     * @param extension
     * @return
     */
    public static List<String> getAllFiles(String directory, String extension) {
        return listFileNames(new File(directory), extension);
    }

    /**
     * Returns the list of all files from the given <code>directory</code>.
     *
     * @param directory
     * @return
     */
    public static List<String> getAllFiles(String directory) {
        return getAllFiles(directory, null);
    }

    /**
     * Returns the list of all .ZIP files from the given <code>directory</code>.
     *
     * @param directory
     * @return
     */
    public static List<String> getAllZipFiles(File directory) {
        return listFileNames(directory, ".zip");
    }

    /**
     * Returns the list of all .ZIP files from the given <code>directory</code>.
     *
     * @param directory
     * @return
     */
    public static List<String> getAllZipFiles(String directory) {
        return getAllZipFiles(new File(directory));
    }

    /**
     * Returns the latest version from the version list. For example: if your versions list contains the following
     * values:
     * <code>[1.0, 2.0, 3.0, 1.0.0, 1.1.1, 1.0.0.1, 2.0.1, 1.0.1]</code>, then
     * it will return the <code>3.0</code> as latest version. If the version list is null or empty, it returns empty
     * string;
     *
     * @param listOfVersions
     * @return
     */
    public static String getLatestVersion(List<String> listOfVersions) {
        System.out.println("+getLatestVersion(" + listOfVersions + ")");
        String latestVersion = "";
        if (BeanUtils.isNotEmpty(listOfVersions)) {
            Collections.sort(listOfVersions, new Comparator<String>() {
                /**
                 * @see java.util.Comparator#compare(java.lang.Object,
                 *      java.lang.Object)
                 */
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });

            latestVersion = (listOfVersions.get(listOfVersions.size() - 1));
        }

        System.out.println("-getLatestVersion(), latestVersion:" + latestVersion);
        return latestVersion;
    }

    /**
     * Converts an object into the bytes.
     *
     * @param object
     * @return
     */
    public static <T> byte[] toBytes(T object) {
        byte[] objectBytes = null;
        if (BeanUtils.isNotNull(object)) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                objectBytes = byteArrayOutputStream.toByteArray();
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                closeSilently(objectOutputStream, byteArrayOutputStream);
            }
        }

        return objectBytes;
    }

    /**
     * Converts the bytes into an object.
     *
     * @param dataBytes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] dataBytes) {
        T object = null;
        if (BeanUtils.isNotNull(dataBytes)) {
            ByteArrayInputStream byteArrayInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(dataBytes);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                object = (T) objectInputStream.readObject();
            } catch (ClassNotFoundException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                closeSilently(byteArrayInputStream, objectInputStream);
            }
        }

        return object;
    }

    /**
     * Deletes the files which are older than the specified days.
     *
     * @param dirPath
     * @param olderThanNDays
     */
    public static void deleteFilesOlderThanNDays(File dirPath, int olderThanNDays) {
        if (isExistAndFolder(dirPath)) {
            File[] listFiles = dirPath.listFiles();
            long purgeTimeMillis = System.currentTimeMillis() - (olderThanNDays * 24 * 60 * 60 * 1000);
            System.out.println("purgeTimeMillis:" + purgeTimeMillis);
            for (File file : listFiles) {
                try {
                    if (file.lastModified() < purgeTimeMillis) {
                        if (!file.delete()) {
                            System.out.println("Unable to delete file:" + file + "file.lastModified:" + file.lastModified());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        }
    }

    /**
     * Deletes the files which are older than the specified days.
     *
     * @param dirPath
     * @param olderThanNDays
     */
    public static void deleteFilesOlderThanNDays(String dirPath, int olderThanNDays) {
        deleteFilesOlderThanNDays(new File(dirPath), olderThanNDays);
    }

    /**
     * Returns the list of file names of the given <code>prefix</code> from the given <code>directory</code>. If the
     * given <code>extensions</code> is null or empty, all files are returned.
     *
     * @param directory
     * @param prefix
     * @return
     */
    public static List<File> listPrefixedFiles(File directory, String prefix) {
        List<File> listFiles = new ArrayList<File>();
        if (isExistAndFolder(directory)) {
            File[] files = directory.listFiles(new RequestHashCodeFileFilter(prefix));
            listFiles.addAll(Arrays.asList(files));
        }

        return listFiles;
    }

    /**
     * Returns the fileName which starts with the given prefix from the given parentFolder.
     *
     * @param filePath
     * @return
     */
    public static String getPrefixedFilePath(String filePath) {
        String prefixedFilePath = null;
        if (BeanUtils.isNotEmpty(filePath)) {
            File path = new File(filePath);
            String requestHashCode = getFileName(filePath, false);
            List<File> listFiles = listPrefixedFiles(path.getParentFile(), requestHashCode);
            if (BeanUtils.isNotEmpty(listFiles)) {
                prefixedFilePath = listFiles.get(0).getAbsolutePath();
            }
            path = null;
        }

        return prefixedFilePath;
    }

    /**
     * Returns the extension from the specified fullPath, if its not null or empty otherwise null.
     *
     * @param fullPath
     * @return
     */
    public static String getExtension(String fullPath) {
        String extension = null;
        if (BeanUtils.isNotEmpty(fullPath)) {
            int dotIndex = fullPath.lastIndexOf(".");
            extension = ((dotIndex > -1 && dotIndex < fullPath.length() - 1) ? fullPath.substring(dotIndex + 1) : "");
        }

        return extension;
    }

    /**
     * Returns the filename from the specified fullPath, if its not null or empty otherwise null.
     *
     * @param fullPath
     * @return
     */
    public static String getFileName(String fullPath, boolean withExtension) {
        String fileName = null;
        if (BeanUtils.isNotEmpty(fullPath)) {
            int pathSeparatorIndex = fullPath.lastIndexOf(File.separator);
            if (pathSeparatorIndex < fullPath.length() - 1) {
                fileName = fullPath.substring(pathSeparatorIndex + 1);
                if (!withExtension) {
                    int dotIndex = fileName.lastIndexOf(".");
                    fileName = ((dotIndex > -1 && dotIndex < fileName.length() - 1) ? fileName.substring(0, dotIndex) : fileName);
                }
            }
        }

        return fileName;
    }

    /**
     * @param socket
     */
    public static void logSocket(Socket socket) {
        System.out.println("Socket Class: " + socket.getClass());
        System.out.println("Remote Address:" + socket.getInetAddress());
        System.out.println("Remote Port:" + socket.getPort());
        System.out.println("Local Socket Address:" + socket.getLocalSocketAddress());
        System.out.println("Local Address:" + socket.getLocalAddress());
        System.out.println("Local Port:" + socket.getLocalPort());

        if (socket instanceof javax.net.ssl.SSLSocket) {
            javax.net.ssl.SSLSocket sslSocket = (javax.net.ssl.SSLSocket) socket;
            System.out.println("Need Client Authentication:" + sslSocket.getNeedClientAuth());
            javax.net.ssl.SSLSession sslSession = sslSocket.getSession();
            System.out.println("Cipher Suite:" + sslSession.getCipherSuite());
            System.out.println("Protocol:" + sslSession.getProtocol());
        }
    }

    /**
     * Prints the <code>ServerSocket</code> information.
     *
     * @param serverSocket
     */
    public static void logServerSocket(ServerSocket serverSocket) {
        System.out.println("ServerSocket Class:" + serverSocket.getClass());
        System.out.println("ServerSocket Address:" + serverSocket.getInetAddress().toString());
        System.out.println("ServerSocket Port:" + serverSocket.getLocalPort());
        if (serverSocket instanceof javax.net.ssl.SSLServerSocket) {
            javax.net.ssl.SSLServerSocket sslServerSocket = (javax.net.ssl.SSLServerSocket) serverSocket;
            System.out.println("Need Client Authentication:" + sslServerSocket.getNeedClientAuth());
            System.out.println("Want Client Authentication:" + sslServerSocket.getWantClientAuth());
            System.out.println("Use Client Mode:" + sslServerSocket.getUseClientMode());
        }
    }

    /**
     * @author Rohtash Lakra
     * @version 1.0.0
     * @created Dec 9, 2015 4:42:16 PM
     */
    public static final class RequestHashCodeFileFilter implements FileFilter {

        // requestHashCode
        private String requestHashCode;

        /**
         * @param requestHashCode
         */
        public RequestHashCodeFileFilter(String requestHashCode) {
            this.requestHashCode = requestHashCode;
        }

        /**
         * Returns true, if the filename starts with the requestHashCode otherwise false.
         *
         * @see java.io.FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File pathName) {
            return (requestHashCode != null && pathName != null && pathName.getName().startsWith(requestHashCode));
        }
    }

    /**
     * Applies the file permission on the given path.
     *
     * @param path
     * @throws IOException
     */
    public static void applyFilePermissions(final Path path) throws IOException {
        LOGGER.debug("+applyFilePermissions({})", path);
        final String userName = System.getProperty("user.name");
        LOGGER.info("userName: {}", userName);
        if ("nobody".equalsIgnoreCase(userName)) {
            throw new IOException(String.format("'%s' is not allowed!", userName));
        } else if (!Files.exists(path, new LinkOption[0])) {
            try {
                Files.createDirectories(path.getParent(), ALL_PERMISSIONS);
            } catch (FileAlreadyExistsException ex) {
                LOGGER.error("Folder [{}] already exists!", path.getParent(), ex);
                //ignore me
            }

            //create a file with write access
            Files.createFile(path, WRITE_PERMISSION);
        } else if (!Files.isRegularFile(path, new LinkOption[0])) {
            throw new IOException(String.format("The file should only be a regular file: %s!", path));
        } else {
            Files.setPosixFilePermissions(path, EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));
            Files.setOwner(path, path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(userName));
        }
        LOGGER.debug("-applyFilePermissions()");
    }

    /**
     * Closes the <code>objects</code> silently.
     *
     * @param objects
     */
    public static void closeSilently(final Object... objects) {
        if (BeanUtils.isNotEmpty(objects)) {
            for (Object object : objects) {
                try {
                    if (object instanceof AutoCloseable) {
                        ((AutoCloseable) object).close();
                    } else if (object instanceof Socket) {
                        ((Socket) object).close();
                    } else if (object instanceof ServerSocket) {
                        ((ServerSocket) object).close();
                    } else {
                        throw new IllegalArgumentException("Unknown object to close!");
                    }
                } catch (Exception ex) {
                    LOGGER.error("Error while closing object: {}", object, ex);
                    IOUtils.error(ex);
                }
            }
        }
    }

    /**
     * Reads the <code>inputStream</code> as bytes.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(final InputStream inputStream) throws IOException {
        return readBytes(inputStream, false);
    }

    /**
     * Reads the file as bytes.
     *
     * @param path
     * @return
     * @throws IOException
     */
    public byte[] readBytes(final Path path) throws IOException {
        return Files.readAllBytes(path);
    }


    /**
     * Writes the <code>jsonString</code> to <code>outputStream</code>.
     *
     * @param outputStream
     * @param jsonString
     * @throws IOException
     */
    public static void writeToStream(final OutputStream outputStream, final String jsonString) throws IOException {
        LOGGER.debug("+writeToStream({}, {})", outputStream, jsonString);
        final OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        streamWriter.write(jsonString);
        streamWriter.flush();
        LOGGER.debug("-writeToStream()");
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static StringBuilder readStream(final InputStream inputStream) throws IOException {
        LOGGER.debug("+readStream({})", inputStream);
        final StringBuilder sBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sBuilder.append(inputLine);
        }
        closeSilently(in);
        LOGGER.debug("-readStream(), sBuilder:{}", sBuilder);
        return sBuilder;
    }


    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream newInputStream(final String path) throws IOException {
        LOGGER.debug("+newInputStream({})", path);
        return new BufferedInputStream(new FileInputStream(new File(path)));
    }

    /**
     * @param hostName
     * @param port
     * @param pathSegment
     * @param contentType
     * @return
     * @throws IOException
     */
    public HttpURLConnection postRequest(final String hostName, final int port, final String pathSegment, String contentType) throws IOException {
        HttpURLConnection connection = null;
        // 1. URL with specific pathSegment
        final URL url = new URL(String.format("%s:%d/%s", hostName, port, pathSegment));
        // 2. Open connection
        connection = (HttpURLConnection) url.openConnection();
        // 3. Specify POST method
        connection.setRequestMethod("POST");
        // 4. Set the headers
        if (contentType == null || contentType.trim().length() == 0) {
            contentType = APPLICATION_JSON;
        }
        connection.setRequestProperty(CONTENT_TYPE, contentType);
        // A URL connection can be used for input and/or output. Set the DoOutput flag to true if you intend to use the URL connection for output, false if not. The default is false.
        connection.setDoOutput(true);

        return connection;
    }

    /**
     * Returns the set of jar file names.
     * <pre>
     * Command:
     * jar tf jarFile.jar
     *  META-INF/
     *  META-INF/MANIFEST.MF
     *  Test-Jar-File/
     *  Test-Jar-File/buildGradle.sh
     *  Test-Jar-File/checkStyles.sh
     *  Test-Jar-File/buildMaven.sh
     *  Test-Jar-File/checkstyle.xml
     * </pre>
     *
     * @param jarFilePath
     * @return
     */
    public static Set<String> getJarEntries(File jarFilePath) throws IOException {
        LOGGER.debug("+getJarEntries({})", jarFilePath);
        Set<String> jarAllEntries = new LinkedHashSet<>();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                jarAllEntries.add(jarEntry.getName());
            }
        }

        LOGGER.debug("-getJarEntries(), jarEntries: {}", jarAllEntries);
        return jarAllEntries;
    }

    /**
     * Returns the set of jar file names.
     * <pre>
     * Command:
     *
     * jar tf jarFile.jar
     *
     *  META-INF/
     *  META-INF/MANIFEST.MF
     *  Jar-File/
     *  Jar-File/buildGradle.sh
     *  Jar-File/checkStyles.sh
     *  Jar-File/buildMaven.sh
     *  Jar-File/checkstyle.xml
     *  Jar-File/com/
     *  Jar-File/com/rslakra/
     *  Jar-File/com/rslakra/core/
     *  Jar-File/com/rslakra/core/ClassTest.class
     *  Jar-File/com/rslakra/core/.DS_Store
     *  Jar-File/com/rslakra/core/entity/
     *  Jar-File/com/rslakra/core/entity/MockEntity.class
     *  Jar-File/com/rslakra/core/entity/TestEntity.class
     *  Jar-File/com/rslakra/core/entity/TestEnums.class
     *  Jar-File/com/rslakra/core/text/
     *  Jar-File/com/rslakra/core/text/CharIteratorTest.class
     *  Jar-File/com/rslakra/core/text/ByteIterableTest.class
     *  Jar-File/com/rslakra/core/text/ByteIteratorTest.class
     *  Jar-File/com/rslakra/core/text/LineIterableTest.class
     *  Jar-File/com/rslakra/core/text/TextUtilsTest.class
     * </pre>
     *
     * @param jarFilePath
     * @return
     */
    public static Set<String> getJarFileClassNames(File jarFilePath) throws IOException {
        LOGGER.debug("+getJarFileClassNames({})", jarFilePath);
        Set<String> classNames = new LinkedHashSet<>();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace("/", ".").replace(".class", "");
//                    className = className.substring(className.indexOf(".") + 1);
                    classNames.add(className);
                }
            }
        }

        LOGGER.debug("-getJarFileClassNames(), classNames: {}", classNames);
        return classNames;
    }

    /**
     * @param jarFile
     * @return
     * @throws MalformedURLException
     */
    public static URL getJarFileUrl(File jarFile) throws MalformedURLException {
        return new URL("jar:file:" + jarFile + "!/");
    }

    /**
     * @param jarFile
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class> getJarFileClasses(File jarFile) throws IOException, ClassNotFoundException {
        LOGGER.debug("+getJarFileClasses({})", jarFile);
        Set<String> classNames = getJarFileClassNames(jarFile);
        Set<Class> classHashSet = new LinkedHashSet<>(classNames.size());
        try (URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{getJarFileUrl(jarFile)})) {
            for (String className : classNames) {
                // Load the class by its className
                Class loadClass = urlClassLoader.loadClass(className);
                classHashSet.add(loadClass);
            }
        }

        LOGGER.debug("-getJarFileClasses(), classHashSet: {}", classHashSet);
        return classHashSet;
    }

}
