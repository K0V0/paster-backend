package com.kovospace.paster.base.utils;

import java.util.Arrays;

public class Utils {

    private Utils() {}

    public static boolean isFilled(String str) {
        return str != null && !str.trim().equals("");
    }

    public static boolean isFilled(Long num) {
        return num != null && num != 0;
    }

    public static <TYP extends Enum<TYP>> TYP stringValueToEnum(final String value, final Class<TYP> klazz) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Arrays.stream(klazz.getEnumConstants())
                .filter(constant -> constant .name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
