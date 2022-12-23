package com.kovospace.paster.item.services.filePreviewService.conversions.images;

import com.kovospace.paster.item.models.File;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JpegPreviewConversion implements Function<File, String> {
    @Override
    public String apply(File file) {
        return null;
    }
}
