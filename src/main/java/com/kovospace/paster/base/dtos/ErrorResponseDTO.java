package com.kovospace.paster.base.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDTO extends ResponseDTO {

  private String message;

  public ErrorResponseDTO(String message) {
    super("error");
    this.message = message;
  }

}
