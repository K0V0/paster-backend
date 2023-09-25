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
        FileItemRequestDTO.class,
        FirstOrder.class,
        SecondOrder.class,
})
public abstract class FileItemRequestDTO extends ItemRequestDTO<String> {

    @NotNull(message = "item.fileupload.initiation.originalFilename.missing", groups = FirstOrder.class)
    @NotBlank(message = "item.fileupload.initiation.originalFilename.empty", groups = SecondOrder.class)
    private String originalFileName;

    @NotNull(message = "item.fileupload.initiation.filetype.missing", groups = FirstOrder.class)
    @NotBlank(message = "item.fileupload.initiation.filetype.empty", groups = SecondOrder.class)
    private String mimeType;

}
