package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.v2.FileCreationException;
import com.kovospace.paster.item.exceptions.v2.FileException;
import com.kovospace.paster.item.models.File;

public interface FilesystemOperationsService {

    File createEmptyFile(File file) throws FileCreationException;

    File addChunkToFile(File file, byte[] chunk, long chunkNumber);

    byte[] getFileData(File file) throws FileException;

    File moveFromTempToFinal(File file);

    void delete(File file);

    void deleteAll();
}
