package com.kovospace.paster.item.mappings.dtoConversions.v2;

import com.kovospace.paster.item.dtos.v2.FileResponseDTO;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.services.FilesystemOperationsService;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class FileToFileResponseDTOConversion implements BiFunction<File, byte[], FileResponseDTO> {

    private final FilesystemOperationsService filesystem;

    private File entity;

    public FileToFileResponseDTOConversion(FilesystemOperationsService filesystem) {
        this.filesystem = filesystem;
    }

    @Override
    public FileResponseDTO apply(File entity, byte[] data) {
        if (entity == null || entity.getItem() == null || entity.getItem().getData() == null) return null;
        this.entity = entity;
        FileResponseDTO dto = new FileResponseDTO();

        dto.setData(data);
        dto.setFilename(getFilename());
        dto.setOriginalFilename(entity.getOriginalFileName());

        return dto;
    }

    private String getFilename() {
        String fileExt = entity.getExtension();
        return fileExt == null
                ? entity.getFileName()
                : String.format("%s.%s", entity.getFileName(), entity.getExtension());
    }

}
