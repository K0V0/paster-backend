package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

/** Contract
 * short text => both String data and String dataPreview filled
 * long text => only String dataPreview filled
 * uncomplete/corrupted file => only boolean isFile set
 * completed file => String data is filled with URL, boolean isFile is set
 * file has preview => String dataPreview is filled with URL to preview, boolean isFile is set. File needs to be completed.
 */

//TODO for not completed/corrupted file, implement endpoint to get state
//TODO for large text, implement endpoint to query whole text

@Getter
@Setter
public class ItemResponseDTO extends OkResponseDTO {
  private long id;
  private String dataPreview;
  private String data;
  private long timestamp;
  private PlatformEnum platform;
  private String deviceName;
  private boolean isFile;
}
