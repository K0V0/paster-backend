package com.kovospace.paster.item.mappings.dtoConversions.v2;

import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.services.filePreviewService.FilePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FileToFilePreviewConversion implements Function<Item, String> {

    private final FilePreviewService filePreviewService;

    @Autowired
    public FileToFilePreviewConversion(FilePreviewService filePreviewService) {
        this.filePreviewService = filePreviewService;
    }

    @Override
    public String apply(Item item) {
        if (item.getFile() == null || item.getData() == null) return null;
        return filePreviewService.generate(item.getFile());
    }

}
