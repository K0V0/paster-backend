package com.kovospace.paster.item.dtos.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponseDTO {
    private byte[] data;
    private String filename;
    private String originalFilename;
}
