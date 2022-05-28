package com.kovospace.paster.base.websockets.exceptions;

public class ApiKeyNotIncludedException extends WsException {

    public ApiKeyNotIncludedException() {
        super("general.websocket.authentication.apikey.missing");
    }
}
