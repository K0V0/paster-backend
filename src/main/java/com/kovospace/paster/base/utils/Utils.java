package com.kovospace.paster.base.utils;

public class Utils {

    public static boolean isFilled(String str) {
        return str != null && !str.trim().equals("");
    }

    public static boolean isFilled(Long num) {
        return num != null && num != 0;
    }

    public static <T> T exceptionHandler(ExceptionCallback<T> callback) {
        try {
            return callback.execute();
        } catch (Exception e) {
            // TODO nejaky logging ??
            return null; // You can return a default value or throw a custom exception if needed
        }
    }

    public interface ExceptionCallback<T> {
        T execute() throws Exception;
    }

    public static String getConvertedPlatformValue(Object input) {
        if (input == null) {
            return null;
        }

        if (input instanceof Enum<?>) {
            return ((Enum<?>) input).name();
        } else if (input instanceof String) {
            return (String) input;
        } else {
            throw new IllegalArgumentException("Unsupported input type: " + input.getClass());
        }
    }
}
