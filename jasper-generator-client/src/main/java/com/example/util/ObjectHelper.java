package com.example.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ObjectHelper {

    private ObjectHelper() {
    }


    public static Map<String, String> getFieldsName(Object clazz) {
        return Arrays.stream(clazz.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Field::getName));
    }

    public static <T> List<Map<String, String>> getFieldsAsMap(List<T> list) {
        return list.stream()
                .map(object -> {
                    return Arrays.stream(object.getClass().getDeclaredFields())
                            .collect(Collectors.toMap(Field::getName, field -> {
                                field.setAccessible(true);
                                try {
                                    return field.get(object).toString();
                                } catch (IllegalAccessException e) {
                                    log.error("illegal access exception", e);
                                }
                                return "";
                            }));
                }).collect(Collectors.toList());

    }
}
