package com.kovospace.paster.item.dtos.v2;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import com.kovospace.paster.item.dtos.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * String data:
 * in case of short text, this text should be placed here
 * in case of long text, text preview will be placed here AND flag set
 * in case of file, URL will be placed here AND corresponding flag set
 */
@Getter
@Setter
public class ItemResponseDTO extends OkResponseDTO {
  private long id;
  private String data;
  private long timestamp;
  private PlatformEnum platform;
  private String deviceName;
  private boolean isFile;
  private boolean isLargeText;
}
