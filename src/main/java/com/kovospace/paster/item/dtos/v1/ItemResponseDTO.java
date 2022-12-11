package com.kovospace.paster.item.dtos.v1;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

/** Behaviour (caveats) of v1 API
 * Text over 512 chars - flag set
 * Both preview and text filled in DTO no matter how big is text
 */

/** Compatibility mode with v2 API
 * use URL to get file - secured in contract of model - just copy content of field
 */

@Getter
@Setter
public class ItemResponseDTO extends OkResponseDTO {
  private long id;
  private String text;
  private String preview;
  private long timestamp;
  private boolean isLarge;
  private PlatformEnum platform;
  private String deviceName;
}
