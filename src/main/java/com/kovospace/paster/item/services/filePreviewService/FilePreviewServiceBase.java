package com.kovospace.paster.item.services.filePreviewService;

import com.kovospace.paster.item.mappings.enums.FiletypeGroupsEnum;
import com.kovospace.paster.item.models.File;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.kovospace.paster.base.utils.Utils.toUnmodifiableSet;

public abstract class FilePreviewServiceBase implements FilePreviewService {

    protected static final Map<FiletypeGroupsEnum, Set<String>> FILETYPE_GROUPS;
    static {
        Map<FiletypeGroupsEnum, Set<String>> tmpMap = new HashMap<>();
        tmpMap.put(
                FiletypeGroupsEnum.IMAGE_FILES,
                toUnmodifiableSet("jpg", "jpeg", "gif", "png", "bmp", "tiff", "svg", "tif", "webp"));
        FILETYPE_GROUPS = Collections.unmodifiableMap(tmpMap);
    }

    protected abstract Set<String> getAllowedExtensions();

    protected boolean isExtensionSupported(String fileExtension) {
        if (fileExtension == null) return false;
        if (getAllowedExtensions() == null) return false;
        return getAllowedExtensions().contains(fileExtension);
    }

    protected boolean isExtensionSupported(File file) {
        //FIXME nemalo by sa stat, ak hej, porozmyslat o vyhodeni vynimky
        if (file == null) return false;
        if (file.getExtension() == null) return false;
        return isExtensionSupported(file.getExtension());
    }
}
