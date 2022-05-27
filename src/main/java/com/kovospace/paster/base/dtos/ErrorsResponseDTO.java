package com.kovospace.paster.base.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ErrorsResponseDTO extends ResponseDTO {

  private Map<String, Map<String, String>> messages;

  public ErrorsResponseDTO(Map<String, Map<String, String>> messages) {
    super("error");
    this.messages = messages;
  }

}
