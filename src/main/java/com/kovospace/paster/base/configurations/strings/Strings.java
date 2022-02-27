package com.kovospace.paster.base.configurations.strings;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// todo unit test
public class Strings {
    private static final String FILE_PATH = "strings.strings";
    private static final Pattern pattern = Pattern
            .compile("^\\s*([a-zA-Z0-9.]+)\\s*=\\s*\"([^\"]+)\"\\s*$");
    private static final Map<String, String> values = new HashMap<>();
    private static Matcher matcher;

    static {
        readFile();
    }

    private static void readFile() {
        try (Stream<String> lines = Files.lines(
                new ClassPathResource(FILE_PATH).getFile().toPath(),
                Charset.defaultCharset()))
        {
            values.putAll(lines
                    .filter(line -> !line.trim().equals(""))
                    .map(pattern::matcher)
                    .filter(Matcher::find)
                    .collect(Collectors.toMap(
                            key -> key.group(1).toLowerCase().trim(),
                            value -> value.group(2).trim(),
                            (existing, replacement) -> existing
                    )));
        } catch (IOException e) {
            // todo zalogovat
        }
    }

    public static String s(String key) {
        if (values.containsKey(key.toLowerCase())) {
            return values.get(key.toLowerCase());
        }
        return key;
    }
}
