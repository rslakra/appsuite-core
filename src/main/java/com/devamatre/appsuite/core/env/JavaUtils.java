package com.devamatre.appsuite.core.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Rohtash Lakra
 * @created 10/31/23 2:10 PM
 */
public enum JavaUtils {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaUtils.class);

    public static <T extends Annotation> List<T> getAnnotations(Class<T> classType) {
        return null;
    }

    /**
     * @param method
     * @param classType
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getDeclaredAnnotations(Method method, Class<T> classType) {
        Annotation annotation = null;
        // If an interface method doesn't have an annotation, then check an  implementation class
        if (Objects.isNull(method)) {
            annotation = Arrays.stream(method.getDeclaredAnnotations())
                .filter(entry -> entry.getClass().isAssignableFrom(classType))
                .findFirst()
                .orElse(null);
        }

        return null;
    }

    /**
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Object object, Class<T> classType) {
        LOGGER.debug("+getAnnotation({}, {})", object, classType);
        // check method has annotation or not
        Annotation annotation = object.getClass().getAnnotation(classType);
        // If an interface method doesn't have an annotation, then check an implementation class
        if (Objects.isNull(annotation)) {
            // filter all methods of the same name
            final List<Method> declaredMethods = Arrays.asList(object.getClass().getDeclaredMethods());
            if (Objects.nonNull(declaredMethods)) {
                // then find the method that has annotation
                final Optional<Method> methodOptional = declaredMethods.stream()
                    .filter(declaredMethod -> declaredMethod.getAnnotation(classType) != null)
                    .findFirst();
                annotation = methodOptional.isPresent() ? methodOptional.get().getAnnotation(classType) : null;
            }
        }

        LOGGER.debug("-getAnnotation(), annotation:{}", annotation);
        return (T) annotation;
    }

    /**
     * @param object
     * @param classType
     * @param <T>
     * @return
     */
    public static <T extends Annotation> List<T> getAnnotations(Object object, Class<T> classType) {
        return null;
    }
}
