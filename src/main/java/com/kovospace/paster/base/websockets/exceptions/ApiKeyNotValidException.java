package com.kovospace.paster.base.websockets.exceptions;

public class ApiKeyNotValidException extends WsException {

    public ApiKeyNotValidException() {
        super("general.websocket.authentication.apikey.invalid");
    }
}
