package com.kovospace.paster.base.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorsResponseDTO extends ResponseDTO {

  private Map<String, List<ErrorResponseDTO>> messages;

  public ErrorsResponseDTO(Map<String, List<ErrorResponseDTO>> messages) {
    super("error");
    this.messages = messages;
  }

}
