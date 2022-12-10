package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtoHelpers.FirstOrder;
import com.kovospace.paster.base.dtoHelpers.SecondOrder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@GroupSequence({
        FileItemUploadChunkRequestDTO.class,
        FirstOrder.class,
})
public class FileItemUploadChunkRequestDTO extends FileItemRequestDTO {

    @NotNull(message = "item.fileupload.upload.file.missing", groups = FirstOrder.class)
    private byte[] fileContent;

    @NotNull(message = "item.fileupload.upload.chunk.number.missing", groups = FirstOrder.class)
    @NotBlank(message = "item.fileupload.upload.chunk.number.empty", groups = SecondOrder.class)
    private Long chunkNumber;

    @NotNull(message = "item.fileupload.upload.file.id.missing", groups = FirstOrder.class)
    @NotBlank(message = "item.fileupload.upload.file.id.empty", groups = SecondOrder.class)
    private Long fileId;

    @NotNull(message = "item.fileupload.upload.item.id.missing", groups = FirstOrder.class)
    @NotBlank(message = "item.fileupload.upload.item.id.empty", groups = SecondOrder.class)
    private Long itemId;

}
