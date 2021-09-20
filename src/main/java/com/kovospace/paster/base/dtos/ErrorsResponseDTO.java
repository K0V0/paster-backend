package com.kovospace.paster.base.dtos;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorsResponseDTO extends ResponseDTO {

  private Map<String, String> messages;

  public ErrorsResponseDTO(Map<String, String> messages) {
    super("error");
    this.messages = messages;
  }

}
