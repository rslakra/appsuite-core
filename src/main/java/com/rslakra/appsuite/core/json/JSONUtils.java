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
 *
 * Devamatre reserves the right to modify the technical specifications and or 
 * features without any prior notice.
 *****************************************************************************/
package com.rslakra.appsuite.core.json;

import com.rslakra.appsuite.core.BeanUtils;
import com.rslakra.appsuite.core.IOUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JSON utility class.
 *
 * @author Rohtash Lakra
 * @version 1.0.0
 * @created May 18, 2015 2:39:11 PM
 */
public enum JSONUtils {
    INSTANCE;

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private GsonBuilder gsonBuilder;

    private JSONUtils() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * @return
     */
    private ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * @param prettyPrint
     * @return
     */
    private GsonBuilder getGsonBuilder(final boolean prettyPrint) {
        if (Objects.isNull(gsonBuilder)) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.enableComplexMapKeySerialization();
            if (prettyPrint) {
                gsonBuilder.setPrettyPrinting();
            }
        }

        return gsonBuilder;
    }

    /**
     * @param jsonString
     * @param responseType
     * @param <T>
     * @return
     */
    public static <T> T fromJson(final String jsonString, final Class<T> responseType) throws IOException {
        LOGGER.debug("fromJson({}, {})", jsonString, responseType);
        return INSTANCE.getObjectMapper().readValue(jsonString, responseType);
    }

    /**
     * @param inputStream
     * @param responseType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(final InputStream inputStream, Class<T> responseType) throws IOException {
        return INSTANCE.getObjectMapper().readValue(inputStream, responseType);
    }


    /**
     * @param object
     * @return
     * @throws IOException
     */
    public static String toJson(final Object object) throws IOException {
        return INSTANCE.getObjectMapper().writeValueAsString(object);
    }

    /**
     * <code>FieldNamingPolicy.IDENTITY</code> will ensure that the field name is unchanged. This is default
     * behavior.
     *
     * <code>FieldNamingPolicy.LOWER_CASE_WITH_DASHES</code> will modify the Java Field name from its camel cased form
     * to a lower case field name where each word is separated by a dash (-).
     *
     * <code>FieldNamingPolicy.LOWER_CASE_WITH_DOTS</code> will modify the Java Field name from its camel cased form to
     * a lower case field name where each word is separated by a dot (.).
     *
     * <code>FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES</code> will modify the Java Field name from its camel cased
     * form to a lower case field name where each word is separated by an underscore (_).
     *
     * <code>FieldNamingPolicy.UPPER_CAMEL_CASE</code> will ensure that the first “letter” of the Java field name is
     * capitalized when serialized to its JSON form.
     *
     * <code>FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES</code> will ensure that the first “letter” of the Java
     * field name is capitalized when serialized to its JSON form and the words will be separated by a space.
     *
     * @param fieldNamingPolicy
     */
    public final void setFieldNamingPolicy(final FieldNamingPolicy fieldNamingPolicy) {
        if (BeanUtils.isNotNull(fieldNamingPolicy)) {
            getGsonBuilder(true).setFieldNamingPolicy(fieldNamingPolicy);
        }
    }

    /**
     * <code>GsonBuilder.serializeNulls()</code> Serialization of null Values
     * By default, Gson ignores null properties during serialization. But, sometimes we want to serialize fields with
     * null value so that it must appear in JSON. Use serializeNulls() method for this purpose.
     */
    public final void serializeNulls() {
        getGsonBuilder(true).serializeNulls();
    }

    /**
     * Returns the GSON object.
     *
     * @param prettyPrint
     * @return
     */
    private Gson createGson(final boolean prettyPrint) {
        return getGsonBuilder(prettyPrint).create();
    }

    /**
     * @return
     */
    private Gson createGson() {
        return createGson(false);
    }

    /**
     * Returns the JSON string for the given JSON object.
     *
     * @param object
     * @param prettyPrint
     * @return
     */
    public static String toJSONString(final Object object, final boolean prettyPrint) {
        return INSTANCE.createGson(prettyPrint).toJson(object);
    }

    /**
     * Returns the JSON string for the given JSON object.
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return toJSONString(object, false);
    }

    /**
     * @param listStrings
     * @return
     */
    public static String toJSONString(final List<String> listStrings) {
        return (BeanUtils.isNotEmpty(listStrings) ? INSTANCE.createGson().toJson(listStrings) : BeanUtils.EMPTY_STR);
    }

    /**
     * Returns the JSON string for the given exception.
     *
     * @param error
     * @return
     */
    public static String toJSONString(final Throwable error) {
        return toJSONString(error, false);
    }

    /**
     * Generates the JSON string from the given map.
     *
     * @param mapData
     * @return
     */
    public static String toJSONString(final Map<String, String> mapData) {
        return INSTANCE.createGson().toJson(mapData);
    }

    /**
     * Returns the object of the class type T for the given JSON string.
     *
     * @param jsonString
     * @param responseType
     * @return
     */
    public static <T> T fromJSONString(final String jsonString, final Class<T> responseType) {
        return INSTANCE.createGson().fromJson(jsonString, responseType);
    }

    /**
     * Returns the value for the given key from the given jsonString.
     *
     * @param jsonString
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T valueForKey(final String jsonString, final String key) {
        return (T) fromJSONString(jsonString, Map.class).get(key);
    }

    /**
     * @param jsonObject
     * @param propertyName
     * @return
     */
    public static Integer valueForKeyAsInteger(final JsonObject jsonObject, final String propertyName) {
        JsonElement jsonElement = jsonObject.get(propertyName);
        return (jsonElement == null ? null : jsonElement.getAsInt());
    }

    /**
     * Returns the list of the specified objects from the given JSON string.
     *
     * @param jsonString
     * @param classType
     * @return
     */
    public static <T> List<T> listOfObjects(final String jsonString, final Class<T> classType) {
        List<T> objects = new ArrayList<T>();
        JsonArray jsonArray = toJSONArray(jsonString);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                Gson gson = new Gson();
                T object = gson.fromJson(jsonArray.get(i), classType);
                objects.add(object);
            }
        }

        return objects;
    }

    /**
     * Returns the list of the specified objects from the given JSON string.
     *
     * @param jsonString
     * @param classTypes
     * @return
     */
    public static List<?> listOfObjects(final String jsonString, final String key, final Class<?>... classTypes) {
        List<Object> mixedObjects = new ArrayList<Object>();
        if (!isValidJSONString(jsonString)) {
            return mixedObjects;
        }

        JsonArray jsonArray = toJSONArray(jsonString);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                String objectJSONString = toJSONString(jsonArray.get(i));
                for (int j = 0; j < classTypes.length; j++) {
                    String value = (String) toMap(objectJSONString).get(key);
                    if (value.equalsIgnoreCase(classTypes[j].getSimpleName())) {
                        Object object = fromJSONString(objectJSONString, classTypes[j]);
                        mixedObjects.add(object);
                        break;
                    }
                }
            }
        }

        return mixedObjects;
    }

    /**
     * @param jsonString
     * @return
     */
    public static JsonArray toJSONArray(final String jsonString) {
        final JsonElement jsonElement = toJsonElement(jsonString);
        return (jsonElement.isJsonArray() ? jsonElement.getAsJsonArray() : null);
    }

    /**
     * @param jsonString
     * @return
     */
    public static JsonElement toJsonElement(final String jsonString) {
        return JsonParser.parseString(jsonString);
    }

    /**
     * @param jsonString
     * @return
     */
    public static boolean isValidJSONString(final String jsonString) {
        if (BeanUtils.isNotEmpty(jsonString)) {
            try {
                JsonParser.parseString(jsonString);
            } catch (JsonSyntaxException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Returns the list of strings for the given JSON string.
     *
     * @param jsonString
     * @return
     */
    public static <T> List<T> listOfType(final String jsonString) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        List<T> listOfObjects = INSTANCE.createGson().fromJson(jsonString, typeToken.getType());
        return listOfObjects;
    }

    /**
     * @param jsonString
     * @return
     */
    public static List<String[]> listOfStringArrays(final String jsonString) {
        TypeToken<List<String[]>> typeToken = new TypeToken<List<String[]>>() {
        };

        List<String[]> list = INSTANCE.createGson().fromJson(jsonString, typeToken.getType());
        return list;
    }

    /**
     * Returns the Map object from the given string.
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> toMap(final String jsonString) {
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
        };
        return INSTANCE.createGson().fromJson(jsonString, typeToken.getType());
    }

    /**
     * Returns the Map object from the given string.
     *
     * @param jsonString
     * @return
     */
    public static Map<String, List<String>> asHeaders(final String jsonString) {
        TypeToken<Map<String, List<String>>> typeToken = new TypeToken<Map<String, List<String>>>() {
        };
        return INSTANCE.createGson().fromJson(jsonString, typeToken.getType());
    }

    /**
     * @param dataBytes
     * @return
     */
    public static Map<String, List<String>> asHeaders(final byte[] dataBytes) {
        TypeToken<Map<String, List<String>>> typeToken = new TypeToken<Map<String, List<String>>>() {
        };
        return INSTANCE.createGson().fromJson(IOUtils.toUTF8String(dataBytes), typeToken.getType());
    }

    /**
     * Returns the Map object from the given data bytes.
     *
     * @param dataBytes
     * @return
     */
    public static Map<String, Object> toMap(final byte[] dataBytes) {
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
        };
        return INSTANCE.createGson().fromJson(IOUtils.toUTF8String(dataBytes), typeToken.getType());
    }

    /**
     * Returns the Map object from the given object.
     *
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(final Object object) {
        return toMap(toJSONString(object));
    }

    /**
     * @param jsonString
     * @return null if jsonString is null or empty
     */
    public static List<Object[]> listOfObjectArrays(final String jsonString) {
        TypeToken<List<Object[]>> typeToken = new TypeToken<List<Object[]>>() {
        };
        List<Object[]> list = INSTANCE.createGson().fromJson(jsonString, typeToken.getType());
        return list;
    }

    /**
     * @param jsonArray
     * @return
     */
    public static List<String> toListOfStrings(final JsonArray jsonArray) {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        List<String> list = INSTANCE.createGson().fromJson(jsonArray, typeToken.getType());
        return list;
    }

    /**
     * Returns true if the <code>jsonObject</code> contains the <code>key</code> otherwise false.
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static boolean hasElement(final JsonObject jsonObject, final String key) {
        return (BeanUtils.isNotNull(jsonObject) && BeanUtils.isNotEmpty(key) && jsonObject.has(key));
    }

    /**
     * @param jsonObject
     * @param key
     * @return
     */
    public static JsonElement getElement(final JsonObject jsonObject, final String key) {
        return (hasElement(jsonObject, key) ? jsonObject.get(key) : null);
    }

    /**
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getAsString(final JsonObject jsonObject, final String key) {
        return (hasElement(jsonObject, key) ? jsonObject.get(key).getAsString() : null);
    }

    /**
     * Returns the value for the given key from the given jsonArray.
     *
     * @param jsonArray
     * @param key
     * @return
     */
    public static String valueForKeyAsString(final JsonArray jsonArray, String key) {
        String value = null;
        if (BeanUtils.isNotEmpty(jsonArray) && BeanUtils.isNotEmpty(key)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                if (hasElement(jsonObject, "name") && getAsString(jsonObject, "name").equals(key)) {
                    value = getAsString(jsonObject, "value");
                    break;
                }
            }
        }

        return value;
    }

    /**
     * @param jsonObject
     * @param key
     * @return
     */
    public static boolean getAsBoolean(JsonObject jsonObject, String key) {
        return Boolean.valueOf(getAsString(jsonObject, key));
    }

    /**
     * Returns an image bytes from the given JSON string.
     *
     * @param jsonString
     * @return
     */
    public static byte[] toImageBytes(final String jsonString) {
        byte[] imageBytes = null;
        if (BeanUtils.isNotEmpty(jsonString)) {
            String javaString = fromJSONString(jsonString, String.class);
            imageBytes = javaString.getBytes();
        }

        return imageBytes;
    }

    /**
     * Returns an image files from the given JSON bytes.
     *
     * @param responseBytes
     * @return
     */
    public static byte[] toImageBytes(byte[] responseBytes) {
        if (BeanUtils.isNotEmpty(responseBytes)) {
            /*
             * JSON String is encoded using the Base64 String which needs to
             * convert back to Java String for images.
             */
            String jsonString = IOUtils.toUTF8String(responseBytes);
            responseBytes = toImageBytes(jsonString);
        }

        return responseBytes;
    }

}
