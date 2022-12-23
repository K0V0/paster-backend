package com.kovospace.paster.item.services.filePreviewService;

import com.kovospace.paster.item.mappings.enums.FiletypeGroupsEnum;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.services.filePreviewService.services.ImagePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class FilePreviewServiceImpl extends FilePreviewServiceBase {

    private final ImagePreviewService imagePreviewService;

    @Autowired
    public FilePreviewServiceImpl(ImagePreviewService imagePreviewService) {
        this.imagePreviewService = imagePreviewService;
    }

    @Override
    public String generate(File file) {
        return Optional.of(file)
                .map(File::getExtension)
                .map(this::getFileGroup)
                .map(group -> {
                    switch(group) {
                        case IMAGE_FILES: return imagePreviewService.generate(file);
                        default: return null;
                    }
                })
                .orElse(null);
    }

    private FiletypeGroupsEnum getFileGroup(String fileExtension) {
        if (fileExtension == null) return null;
        for(FiletypeGroupsEnum fileTypeGroup : FILETYPE_GROUPS.keySet()) {
            if (FILETYPE_GROUPS.get(fileTypeGroup).contains(fileExtension.trim().toLowerCase())) return fileTypeGroup;
        }
        return null;
    }

    @Override
    protected Set<String> getAllowedExtensions() {
        return null; //OK, tu nema byt implementovane
    }
}
