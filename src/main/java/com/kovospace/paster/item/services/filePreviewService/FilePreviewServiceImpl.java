package com.kovospace.paster.item.services.filePreviewService;

import com.kovospace.paster.item.services.FilesystemOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilePreviewServiceImpl implements FilePreviewService {

    private final FilesystemOperationsService filesystemOperationsService;

    @Autowired
    public FilePreviewServiceImpl(FilesystemOperationsService filesystemOperationsService) {
        this.filesystemOperationsService = filesystemOperationsService;
    }
}
