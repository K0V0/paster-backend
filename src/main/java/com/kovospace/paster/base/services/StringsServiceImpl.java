package com.kovospace.paster.base.services;

import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StringsServiceImpl implements StringsService {

    private static final Logger logger = LoggerFactory.getLogger(StringsServiceImpl.class);

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
                logger.debug("Cannot find translation / code parsing error", e);
            }
        }
        values = Collections.unmodifiableMap(result);
    }

    private static Stream<String> getLines(String locale) throws IOException {
        return new BufferedReader(new InputStreamReader(new ClassPathResource(
                String.format("%s.%s", FILE_PREFIX, locale)).getInputStream()
        )).lines();
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
        if (code == null || code.trim().equals("")) { return ""; }
        return Optional.ofNullable(values)
                .map(v -> v.get(locale))
                .map(v -> v.get(code.toLowerCase()))
                .orElse(code);
    }

    @Override
    public ErrorResponseDTO getErrorResponseDTO(String code) {
        return new ErrorResponseDTO(code, getTranslation(code));
    }
}
