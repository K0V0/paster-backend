package com.kovospace.paster.base.websockets.exceptions;

public class JwtTokenNotIncludedException extends WsException {

  public JwtTokenNotIncludedException() {
    super("JWT Token not included as param of handshake request.");
  }
}
