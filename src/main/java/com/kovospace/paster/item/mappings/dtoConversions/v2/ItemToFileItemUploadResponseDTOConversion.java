package com.kovospace.paster.item.mappings.dtoConversions.v2;

import com.kovospace.paster.item.dtos.v2.FileItemUploadResponseDTO;
import com.kovospace.paster.item.mappings.dtoConversions.ItemToFileNameConversion;
import com.kovospace.paster.item.models.File;
import com.kovospace.paster.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ItemToFileItemUploadResponseDTOConversion implements Function<Item, FileItemUploadResponseDTO> {

    private final ItemToFileNameConversion itemToFileNameConversion;

    @Value("${item.file-chunk-size}")
    private static long DEFAULT_CHUNK_SIZE = 524288;
    private Item entity;
    private File fileEntity;
    private FileItemUploadResponseDTO dto;

    @Autowired
    public ItemToFileItemUploadResponseDTOConversion(ItemToFileNameConversion itemToFileNameConversion) {
        this.itemToFileNameConversion = itemToFileNameConversion;
    }

    @Override
    public FileItemUploadResponseDTO apply(Item item) {
        if (item == null || item.getFile() == null) return null;
        this.entity = item;
        this.fileEntity = item.getFile();
        dto = new FileItemUploadResponseDTO();

        dto.setFileName(getFileName());
        dto.setOriginalFileName(getOriginalFileName());
        dto.setChunksCount(getChunksCount());
        dto.setChunkNumber(getChunkNumber());
        dto.setChunkSize(getChunkSize());
        dto.setMimeType(getMimeType());
        dto.setExtension(fileEntity.getExtension());
        dto.setItemId(entity.getId());
        dto.setFileId(fileEntity.getId());
        dto.setFilePath(fileEntity.getFilePath());

        return dto;
    }

    private String getFileName() {
        return fileEntity.getFileName() != null
                ? fileEntity.getFileName()
                : itemToFileNameConversion.apply(entity);
    }

    private long getChunksCount() {
        return fileEntity.getChunksCount() == null ? 1 : fileEntity.getChunksCount();
    }

    private Long getChunkNumber() {
        /** counting from 0 */
        return fileEntity.getChunkNumber();
    }

    private long getChunkSize() {
        return fileEntity.getChunkSize() == null ? DEFAULT_CHUNK_SIZE : fileEntity.getChunkSize();
    }

    private String getOriginalFileName() {
        return Optional.ofNullable(fileEntity.getOriginalFileName())
                .orElseThrow(() -> new IllegalArgumentException("File table record saved without filename"));
    }

    private String getMimeType() {
        return Optional.ofNullable(fileEntity.getMimeType())
                .orElseThrow(() -> new IllegalArgumentException("File table record saved without file MIME type"));
    }

}
