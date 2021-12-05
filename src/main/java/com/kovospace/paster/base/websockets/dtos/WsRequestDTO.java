package com.kovospace.paster.base.websockets.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsRequestDTO {
  private String jwtToken;

  @Override
  public String toString() {
    return "jwtToken: " + this.jwtToken;
  }
}
