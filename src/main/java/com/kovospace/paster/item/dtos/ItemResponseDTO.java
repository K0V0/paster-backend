package com.kovospace.paster.item.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO extends OkResponseDTO {
  private String text;
  private String preview;
  private String date;
  private boolean isLarge;
}
