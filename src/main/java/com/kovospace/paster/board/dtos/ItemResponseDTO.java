package com.kovospace.paster.board.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
  private String text;
  private String preview;
  private String date;
  private boolean isLarge;
}
