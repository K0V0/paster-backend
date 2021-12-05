package com.kovospace.paster.base.websockets.exceptions;

public abstract class WsException extends Exception {
  public WsException(String message) {
    super("Websocket: " + message);
  }
}
