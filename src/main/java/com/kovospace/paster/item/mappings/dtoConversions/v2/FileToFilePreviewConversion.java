package com.kovospace.paster.item.mappings.dtoConversions.v2;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FileToFilePreviewConversion implements Function<String, String> {

    @Override
    public String apply(String file) {
        if (file == null || file.trim().equals("")) return null;
        //TODO nenerovanie nahladov
        return null;
    }
}
