package com.kovospace.paster.base.websockets.exceptions;

public class UserNotFoundException extends WsException {
  public UserNotFoundException() {
    super("general.websocket.user.missing");
  }
}
