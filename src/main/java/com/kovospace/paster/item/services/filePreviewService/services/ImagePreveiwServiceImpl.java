package com.kovospace.paster.item.services.filePreviewService.services;

import com.kovospace.paster.item.mappings.enums.FiletypeGroupsEnum;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.services.filePreviewService.FilePreviewServiceBase;
import com.kovospace.paster.item.services.filePreviewService.conversions.images.JpegPreviewConversion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class ImagePreveiwServiceImpl extends FilePreviewServiceBase implements ImagePreviewService {

    private final JpegPreviewConversion jpegPreviewConversion;

    @Autowired
    public ImagePreveiwServiceImpl(JpegPreviewConversion jpegPreviewConversion) {
        this.jpegPreviewConversion = jpegPreviewConversion;
    }

    @Override
    public String generate(File file) {
        if (isExtensionSupported(file)) {
            switch (file.getExtension()) {
                case "jpg": return jpegPreviewConversion.apply(file);
                default: return null;
            }
        }
        return null;
    }

    @Override
    protected Set<String> getAllowedExtensions() {
        return FILETYPE_GROUPS.get(FiletypeGroupsEnum.IMAGE_FILES);
    }
}
