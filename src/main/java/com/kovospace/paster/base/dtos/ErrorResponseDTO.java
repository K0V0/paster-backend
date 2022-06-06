package com.kovospace.paster.base.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDTO extends ResponseDTO {

  private String code;
  private String message;

  public ErrorResponseDTO(String code) {
    this(code, code);
  }

  public ErrorResponseDTO(String code, String message) {
    super("error");
    this.code = code;
    this.message = message;
  }
}
