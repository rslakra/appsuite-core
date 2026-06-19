package com.rslakra.appsuite.core.env;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Rohtash Lakra
 * @created 6/19/26 2:32 PM
 */
public class JavaUtilsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    private @interface Marker {
        String value();
    }

    @Marker("class")
    private static class ClassAndMethodAnnotated {
        @Marker("method")
        public void execute() {
        }

        public void plain() {
        }
    }

    private static class MethodOnlyAnnotated {
        @Marker("method-only")
        public void execute() {
        }
    }

    @Marker("same")
    private static class DuplicateAnnotated {
        @Marker("same")
        public void execute() {
        }
    }

    @Test
    public void testGetAnnotationsWithClassTypeNull() {
        assertTrue(JavaUtils.getAnnotations((Class<Marker>) null).isEmpty());
    }

    @Test
    public void testGetAnnotationsWithAnnotationType() {
        List<Marker> annotations = JavaUtils.getAnnotations(Marker.class);
        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testGetDeclaredAnnotation() throws NoSuchMethodException {
        Method method = ClassAndMethodAnnotated.class.getDeclaredMethod("execute");
        Marker annotation = JavaUtils.getDeclaredAnnotation(method, Marker.class);
        assertNotNull(annotation);
        assertEquals("method", annotation.value());
    }

    @Test
    public void testGetDeclaredAnnotationWithInvalidInput() {
        assertNull(JavaUtils.getDeclaredAnnotation(null, Marker.class));
        Method method = null;
        assertNull(JavaUtils.getDeclaredAnnotation(method, null));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedGetDeclaredAnnotationsDelegates() throws NoSuchMethodException {
        Method method = ClassAndMethodAnnotated.class.getDeclaredMethod("execute");
        Marker annotation = JavaUtils.getDeclaredAnnotations(method, Marker.class);
        assertNotNull(annotation);
        assertEquals("method", annotation.value());
    }

    @Test
    public void testGetAnnotationFromClass() {
        Marker annotation = JavaUtils.getAnnotation(new ClassAndMethodAnnotated(), Marker.class);
        assertNotNull(annotation);
        assertEquals("class", annotation.value());
    }

    @Test
    public void testGetAnnotationFromDeclaredMethodFallback() {
        Marker annotation = JavaUtils.getAnnotation(new MethodOnlyAnnotated(), Marker.class);
        assertNotNull(annotation);
        assertEquals("method-only", annotation.value());
    }

    @Test
    public void testGetAnnotationWithInvalidInput() {
        assertNull(JavaUtils.getAnnotation(null, Marker.class));
        assertNull(JavaUtils.getAnnotation(new Object(), null));
    }

    @Test
    public void testGetAnnotationsFromClassAndMethods() {
        List<Marker> annotations = JavaUtils.getAnnotations(new ClassAndMethodAnnotated(), Marker.class);
        assertNotNull(annotations);
        assertEquals(2, annotations.size());
    }

    @Test
    public void testGetAnnotationsDeduplicates() {
        List<Marker> annotations = JavaUtils.getAnnotations(new DuplicateAnnotated(), Marker.class);
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertEquals("same", annotations.get(0).value());
    }

    @Test
    public void testGetAnnotationsWithInvalidInput() {
        assertTrue(JavaUtils.getAnnotations(null, Marker.class).isEmpty());
        assertTrue(JavaUtils.getAnnotations(new Object(), null).isEmpty());
    }
}
