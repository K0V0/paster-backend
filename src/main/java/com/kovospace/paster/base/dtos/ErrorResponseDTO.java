package com.kovospace.paster.base.dtos;

import com.kovospace.paster.base.configurations.strings.Strings;
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
    super("error");
    this.code = code;
    this.message = Strings.s(code);
  }

}
