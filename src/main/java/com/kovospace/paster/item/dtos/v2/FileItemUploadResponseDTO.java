package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileItemUploadResponseDTO extends OkResponseDTO {
    private String fileName;
    private String originalFileName;
    private long chunksCount;
    private Long chunkNumber;
    private long chunkSize;
    private String mimeType;
    private String extension;
    private long itemId;
    private long fileId;
    private String filePath;
}
