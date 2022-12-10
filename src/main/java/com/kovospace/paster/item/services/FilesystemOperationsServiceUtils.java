package com.kovospace.paster.item.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FilesystemOperationsServiceUtils {

    protected static String getTempFileLocation(com.kovospace.paster.item.models.File fileEntity, String tempLocation) {
        if (fileEntity == null) return null;
        return String.format("%s/%s", tempLocation, fileEntity.getFileName());
    }

    protected static String getFinalFileLocation(com.kovospace.paster.item.models.File fileEntity, String finalLocation) {
        if (fileEntity == null) return null;
        return String.format(
                "%s/%s.%s",
                finalLocation,
                fileEntity.getFileName(),
                Objects.toString(fileEntity.getExtension(), ""));
    }

    protected static Path getTempFilePath(com.kovospace.paster.item.models.File fileEntity, String tempLocation) {
        if (fileEntity == null) return null;
        return Paths.get(getTempFileLocation(fileEntity, tempLocation));
    }

    protected static Path getFinalFilePath(com.kovospace.paster.item.models.File fileEntity, String finalLocation) {
        if (fileEntity == null) return null;
        return Paths.get(getFinalFileLocation(fileEntity, finalLocation));
    }

}
