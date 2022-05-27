package com.kovospace.paster.base.websockets.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsRequestDTO {
  private String message;

  @Override
  public String toString() {
    return "ws message: " + this.message;
  }
}

// javascript: ws.send(JSON.stringify({ "message": "frontend" }))
