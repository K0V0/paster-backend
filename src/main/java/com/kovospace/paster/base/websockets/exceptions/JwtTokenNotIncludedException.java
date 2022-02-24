package com.kovospace.paster.base.websockets.exceptions;

public class JwtTokenNotIncludedException extends WsException {

  public JwtTokenNotIncludedException() {
    super("general.websocket.authentication.jwt.missing");
  }
}
