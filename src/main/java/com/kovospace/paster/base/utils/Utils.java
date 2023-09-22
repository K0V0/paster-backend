package com.kovospace.paster.base.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Utils {

    private static final Set<Pattern> SWAGGER_URI_PATTERNS = new HashSet<Pattern>(){{
        add(Pattern.compile("/swagger-ui\\.html$"));
        add(Pattern.compile("/swagger-ui/.*"));
        add(Pattern.compile("/swagger-resources/.*"));
        add(Pattern.compile("/v3/api-docs\\.*"));

    }};

    private Utils() {}

    public static boolean isFilled(String str) {
        return str != null && !str.trim().equals("");
    }

    public static boolean isFilled(Long num) {
        return num != null && num != 0;
    }

    public static boolean isSwaggerEndpoint(String URI) {
        return SWAGGER_URI_PATTERNS.stream().anyMatch(patt -> patt.matcher(URI).find());
    }
}
