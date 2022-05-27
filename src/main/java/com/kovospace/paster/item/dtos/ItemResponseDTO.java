package com.kovospace.paster.item.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
