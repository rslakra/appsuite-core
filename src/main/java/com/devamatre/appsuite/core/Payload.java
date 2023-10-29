package com.devamatre.appsuite.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rohtash Lakra
 * @created 4/15/20 6:42 PM
 */
public final class Payload<K, V> extends ConcurrentHashMap<K, V> {

    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String DELETED = "deleted";

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(Payload.class);

    /**
     * @param payload
     */
    public Payload(final Payload payload) {
        super(payload);
        LOGGER.debug("Payload({})", payload);
    }

    /**
     * @param map
     */
    public Payload(final Map<K, V> map) {
        super(map);
        LOGGER.debug("Payload({})", map);
    }

    /**
     * Default
     */
    public Payload() {
        super();
    }

    /**
     * @param key
     * @return
     */
    public V get(final String key) {
        LOGGER.debug("get({})", key);
        return super.get(key);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public V getOrDefault(final String key, final V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public Payload getPayload(final K key) {
        Object payload = get(key);
        if (payload instanceof Payload) {
            return new Payload((Payload) payload);
        } else if (payload instanceof LinkedHashMap) {
            return new Payload((LinkedHashMap) payload);
        }

        return new Payload((Map) payload);
    }

    /**
     * @param key
     * @param value
     */
    public V add(final K key, final V value) {
        return super.put(key, value);
    }

    /**
     * @param key
     * @param value
     */
    public V addIfAbsent(final K key, final V value) {
        return super.putIfAbsent(key, value);
    }


    /**
     * @param key
     * @return
     */
    public boolean contains(final String key) {
        return super.containsKey(key);
    }

    /**
     * Returns the new <code>Payload</code> object with the provided <code>payload</code> parameter.
     *
     * @return
     */
    public static <K, V> Payload<K, V> newBuilder(Map<K, V> payload) {
        return (BeanUtils.isNull(payload) ? new Payload<K, V>() : new Payload<K, V>(payload));
    }

    /**
     * Returns the new <code>Payload</code> object.
     *
     * @return
     */
    public static <K, V> Payload<K, V> newBuilder() {
        return newBuilder(null);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Payload ofPair(final K key, final V value) {
        super.put(key, value);
        return this;
    }

    /**
     * @param payload
     * @return
     */
    public Payload ofPayload(final Payload payload) {
        super.putAll(payload);
        return this;
    }


    /**
     * @param status
     * @return
     */
    public Payload withStatus(final String status) {
        return ofPair((K) STATUS, (V) status);
    }

    /**
     * @param message
     * @return
     */
    public Payload withMessage(final String message) {
        return ofPair((K) MESSAGE, (V) message);
    }

    /**
     * @param pattern
     * @param messages
     * @return
     */
    public Payload withMessage(final String pattern, final Object... messages) {
        return ofPair((K) MESSAGE, (V) String.format(pattern, messages));
    }

    /**
     * @param cause
     * @return
     */
    public Payload withCause(final Throwable cause) {
        return ofPair((K) ERROR, (V) cause.getLocalizedMessage());
    }

    /**
     * @param isDeleted
     * @return
     */
    public Payload withDeleted(final boolean isDeleted) {
        return ofPair((K) DELETED, (V) Boolean.valueOf(isDeleted));
    }


}
