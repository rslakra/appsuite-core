package com.rslakra.appsuite.core.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Rohtash Lakra
 * @created 10/31/23 2:10 PM
 */
public enum JavaUtils {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaUtils.class);

    private static <T extends Annotation> List<T> toAnnotationList(Annotation[] annotations, Class<T> classType) {
        return Arrays.stream(annotations)
            .filter(classType::isInstance)
            .map(classType::cast)
            .toList();
    }

    private static Method[] getDeclaredMethods(Object object) {
        return object.getClass().getDeclaredMethods();
    }

    /**
     * Returns annotations declared on the provided annotation type.
     *
     * @param classType
     *            the annotation class to inspect
     * @param <T>
     *            the annotation type
     * @return a list of declared annotations, or an empty list if {@code classType} is {@code null}
     */
    public static <T extends Annotation> List<T> getAnnotations(Class<T> classType) {
        LOGGER.debug("+getAnnotations({})", classType);
        if (Objects.isNull(classType)) {
            LOGGER.debug("-getAnnotations(), annotations:{}", List.of());
            return List.of();
        }

        final List<T> annotations = toAnnotationList(classType.getDeclaredAnnotations(), classType);
        LOGGER.debug("-getAnnotations(), annotations:{}", annotations);
        return annotations;
    }

    /**
     * Returns the first declared annotation of the requested type on a method.
     *
     * @param method
     *            the method to inspect
     * @param classType
     *            the annotation class to match
     * @param <T>
     *            the annotation type
     * @return the first matching annotation, or {@code null} if none is found
     */
    public static <T extends Annotation> T getDeclaredAnnotation(Method method, Class<T> classType) {
        LOGGER.debug("+getDeclaredAnnotation({}, {})", method, classType);
        if (Objects.isNull(method) || Objects.isNull(classType)) {
            LOGGER.debug("-getDeclaredAnnotation(), annotation:{}", (Object) null);
            return null;
        }

        final T annotation = Arrays.stream(method.getDeclaredAnnotations())
            .filter(classType::isInstance)
            .map(classType::cast)
            .findFirst()
            .orElse(null);
        LOGGER.debug("-getDeclaredAnnotation(), annotation:{}", annotation);
        return annotation;
    }

    /**
     * @deprecated use {@link #getDeclaredAnnotation(Method, Class)} instead.
     */
    @Deprecated
    public static <T extends Annotation> T getDeclaredAnnotations(Method method, Class<T> classType) {
        return getDeclaredAnnotation(method, classType);
    }

    /**
     * Returns a matching annotation from the class or one of its declared methods.
     *
     * @param object
     *            the target object whose class is inspected
     * @param classType
     *            the annotation class to match
     * @param <T>
     *            the annotation type
     * @return the first matching annotation, or {@code null} if none is found
     */
    public static <T extends Annotation> T getAnnotation(Object object, Class<T> classType) {
        LOGGER.debug("+getAnnotation({}, {})", object, classType);
        if (Objects.isNull(object) || Objects.isNull(classType)) {
            LOGGER.debug("-getAnnotation(), annotation:{}", (Object) null);
            return null;
        }

        // check method has annotation or not
        Annotation annotation = object.getClass().getAnnotation(classType);
        // If an interface method doesn't have an annotation, then check an implementation class
        if (Objects.isNull(annotation)) {
            annotation = Arrays.stream(getDeclaredMethods(object))
                .map(declaredMethod -> declaredMethod.getAnnotation(classType))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        }

        LOGGER.debug("-getAnnotation(), annotation:{}", annotation);
        return (T) annotation;
    }

    /**
     * Returns all matching annotations found on the class and its declared methods.
     *
     * @param object
     *            the target object whose class is inspected
     * @param classType
     *            the annotation class to match
     * @param <T>
     *            the annotation type
     * @return a distinct list of matching annotations, or an empty list if input is invalid
     */
    public static <T extends Annotation> List<T> getAnnotations(Object object, Class<T> classType) {
        LOGGER.debug("+getAnnotations({}, {})", object, classType);
        if (Objects.isNull(object) || Objects.isNull(classType)) {
            LOGGER.debug("-getAnnotations(), annotations:{}", List.of());
            return List.of();
        }

        final List<T> classAnnotations = toAnnotationList(object.getClass().getAnnotations(), classType);
        final List<T> methodAnnotations = Arrays.stream(getDeclaredMethods(object))
            .map(method -> method.getAnnotation(classType))
            .filter(Objects::nonNull)
            .toList();

        final List<T> annotations = Arrays.asList(classAnnotations, methodAnnotations)
            .stream()
            .flatMap(List::stream)
            .distinct()
            .toList();

        LOGGER.debug("-getAnnotations(), annotations:{}", annotations);
        return annotations;
    }


}
