package com.kovospace.paster.base.services;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StringsServiceImpl implements StringsService {

    private static final String DEFAULT_LOCALE = "en";
    private static final Map<String, List<String>> LOCALES_MAP = new HashMap<String, List<String>>(){{
        put("sk", Arrays.asList("cz"));
        put("en", Arrays.asList("gb", "us"));
        put("ua", Arrays.asList("ru", "rus", "by"));
    }};

    private static final String FILE_PREFIX = "strings";
    private static final Pattern FILE_STRUCTURE_PATTERN = Pattern.compile("^\\s*([a-zA-Z0-9.]+)\\s*=\\s*\"([^\"]+)\"\\s*$");
    private static final Map<String, Map<String, String>> values;

    private static Matcher matcher;

    private String locale;

    static {
        Map<String, Map<String, String>> result = new HashMap<>();
        for (String locale : LOCALES_MAP.keySet()) {
            try (Stream<String> lines = getLines(locale)) {
                Map<String, String> tmp = new HashMap<>();
                tmp.putAll(lines
                        .filter(line -> !line.trim().equals(""))
                        .map(FILE_STRUCTURE_PATTERN::matcher)
                        .filter(Matcher::find)
                        .collect(Collectors.toMap(
                                key -> key.group(1).toLowerCase().trim(),
                                value -> value.group(2).trim(),
                                (existing, replacement) -> existing
                        )));
                result.put(locale, tmp);
            } catch (IOException e) {
                // todo zalogovat
            }
        }
        values = Collections.unmodifiableMap(result);
    }

    private static Stream<String> getLines(String locale) throws IOException {
        return Files.lines(
                new ClassPathResource(String.format("%s.%s", FILE_PREFIX, locale)).getFile().toPath(),
                Charset.defaultCharset());
    }

    @Override
    public void setLocale(String locale) {
        if (locale == null || locale.equals("")) {
            this.locale = DEFAULT_LOCALE;
        } else {
            // if language is supported
            if (LOCALES_MAP.containsKey(locale.toLowerCase())) {
                this.locale = locale;
                return;
            }
            // use locale, if is simmilar/compatible
            for (String compatibleLang : LOCALES_MAP.keySet()) {
                if (LOCALES_MAP.get(compatibleLang).contains(locale.toLowerCase())) {
                    this.locale = compatibleLang;
                    return;
                }
            }
            // or use default
            this.locale = DEFAULT_LOCALE;
        }
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public String getTranslation(String code) {
        String result = values.get(locale).get(code.toLowerCase());
        if (result == null) {
            return code;
        }
        return result;
    }

    @Override
    public ErrorResponseDTO getErrorResponseDTO(String code) {
        return new ErrorResponseDTO(code, getTranslation(code));
    }
}
