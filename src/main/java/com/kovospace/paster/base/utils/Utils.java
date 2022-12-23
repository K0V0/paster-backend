package com.kovospace.paster.base.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class Utils {

    public static boolean isFilled(String str) {
        return str != null && !str.trim().equals("");
    }

    public static boolean isFilled(Long num) {
        return num != null && num != 0;
    }

    public static <TYPE> Set<TYPE> toUnmodifiableSet(TYPE... params) {
        return Stream.of(params)
                .collect(collectingAndThen(toCollection(HashSet::new), Collections::unmodifiableSet));
    }
}
