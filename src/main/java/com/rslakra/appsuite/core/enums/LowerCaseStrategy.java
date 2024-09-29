package com.rslakra.appsuite.core.enums;

import com.rslakra.appsuite.core.BeanUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Rohtash Lakra
 * @created 5/7/20 5:31 PM
 */
public class LowerCaseStrategy extends PropertyNamingStrategy {

    /**
     * @param config
     * @param field
     * @param defaultName
     * @return
     */
    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return field.getName();
    }

    /**
     * @param config
     * @param method
     * @param defaultName
     * @return
     */
    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return extractMethodName(method, defaultName);
    }

    /**
     * @param config
     * @param method
     * @param defaultName
     * @return
     */
    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return extractMethodName(method, defaultName);
    }

    /**
     * get the class from getter/setter methods
     *
     * @param method
     * @param defaultName - jackson generated name
     * @return the correct property name
     */
    private String extractMethodName(AnnotatedMethod method, String defaultName) {
        Class<?> methodClass = method.getDeclaringClass();
        List<Field> allFields = BeanUtils.getAllFields(methodClass);
        for (Field field : allFields) {
            if (field.getName().equalsIgnoreCase(defaultName)) {
                return field.getName();
            }
        }

        return defaultName;
    }

}
