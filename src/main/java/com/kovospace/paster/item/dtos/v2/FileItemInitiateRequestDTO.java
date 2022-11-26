package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.validators.FieldsSameStateValidator;
import com.kovospace.paster.item.validators.v2.fileItemInitiateRequestDTOvalidator.NoChunkParametersOnContinueRequestValidator;
import lombok.Getter;
import lombok.Setter;

/** Contract
 *      long chunksCount - default null (real default value managed in deeper layer(s) according to application.properties)
 *      long chunkSize - default null (real default value managed in deeper layer(s) according to application.properties)
 *      Long fileId - default null
 *      Long itemId - default null
 *
 *      NEW FILE FLOW:
 *      - fileId and itemId BOTH null or empty:
 *      - chunksCount and chunkSize can be set, otherwise default values (1 chunk and X size) are used
 *      - chunksCount and chunkSize must be set BOTH or NONE, otherwise 400 error
 *
 *      CONTINUE FILE FLOW:
 *      - fileId and itemId BOTH set:
 *      - in that case setting chunksCount and chunkSize is not allowed and results in 400 error
 */
@Getter
@Setter
@FieldsSameStateValidator.List({
        @FieldsSameStateValidator(fields = {"fileId", "itemId"}, message = "item.fileupload.initiation.identificators.invalid"),
        @FieldsSameStateValidator(fields = {"chunksCount", "chunkSize"}, message = "item.fileupload.initiation.chunk.info.invalid ")
})
@NoChunkParametersOnContinueRequestValidator
public class FileItemInitiateRequestDTO extends FileItemRequestDTO {
        private Long chunksCount;
        private Long chunkSize;
        private Long fileId;
        private Long itemId;
}
