package com.kovospace.paster.item.dtos.v2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileItemRequestDTO extends ItemRequestDTO {
    private MultipartFile file;
    //TODO - fileId v tvare <user_id>-<md5hash>
    private String fileId;
    private String fileName;
    private long chunksCount;
    private long chunkNumber;
    private long chunkSize;
    private String mimeType;
    private String extension;
}
