package com.ahafeez.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ObjectMapper: Salahuddin wrote this class to map DTO to resultset.
 * Works for now, BUT SHOULD BE REPLACED WITHOUT REFLECTION FOR NATIVE IMAGE
 * BUILT
 */

public class ObjectMapper {

    public static void loadResultSetIntoObject(ResultSet rst, Object object, List<String> projections)
            throws IllegalArgumentException, IllegalAccessException, SQLException {
        Class<?> zclass = object.getClass();
        for (Field field : zclass.getDeclaredFields()) {
            field.setAccessible(true);
            Table column = field.getAnnotation(Table.class);
            ResponseParam responseParam = field.getAnnotation(ResponseParam.class);

            if (!projections.contains(responseParam.name())) {
                continue;
            }

            Object value = rst.getObject(column.column());

            Class<?> type = field.getType();
            if (isPrimitive(type)) {
                Class<?> boxed = boxPrimitiveClass(type);
                value = boxed.cast(value);
            }
            field.set(object, value);
        }
    }

    public static boolean isPrimitive(Class<?> type) {
        return (type == int.class || type == long.class || type == double.class || type == float.class
                || type == boolean.class || type == byte.class || type == char.class || type == short.class);
    }

    public static Class<?> boxPrimitiveClass(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            String string = "class '" + type.getName() + "' is not a primitive";
            throw new IllegalArgumentException(string);
        }
    }

    public static void fnLoadResultSetIntoObject(ResultSet rst, Object object, List<String> projections)
            throws IllegalArgumentException, IllegalAccessException, SQLException {
        Class<?> zclass = object.getClass();
        for (Field field : zclass.getDeclaredFields()) {
            field.setAccessible(true);
            Table column = field.getAnnotation(Table.class);
            ResponseParam responseParam = field.getAnnotation(ResponseParam.class);

            if (responseParam != null && !projections.contains(responseParam.name())) {
                continue;
            }

            if (column != null) {
                Object value = rst.getObject(column.column());
                Class<?> type = field.getType();
                if (isPrimitive(type)) {
                    Class<?> boxed = boxPrimitiveClass(type);
                    value = boxed.cast(value);
                }
                field.set(object, value);
            }

        }
    }

}
