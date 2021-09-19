package com.kovospace.paster.base.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO extends ResponseDTO {

  private String message;

  public ErrorResponseDTO() { new ErrorResponseDTO(""); }

  public ErrorResponseDTO(String message) {
    super("error");
    this.message = message;
  }

}
