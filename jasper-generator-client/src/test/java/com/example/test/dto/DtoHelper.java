package com.example.test.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DtoHelper {

    public static List<Map<String, String>> generateData(int size, int rowsNumber) {
        return Stream.iterate(0, i -> i + 1)
                .limit(size)
                .map(i -> getMap(rowsNumber))
                .collect(Collectors.toList());
    }

    public static Map<String, String> getMap(int rowsNumber) {
        return Stream.iterate(0, i -> i + 1)
                .limit(rowsNumber)
                .collect(Collectors.toMap(i -> "rowNumber" + i, i -> "cellNumber" + i, throwingMerger(), LinkedHashMap::new));
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (var0, var1) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", var0));
        };
    }

    public static void main(String[] args) {

    }
}
