package com.kovospace.paster.base.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ApiKeyMissingException extends AuthenticationException {

    public ApiKeyMissingException() {
        super("general.endpoint.authentication.apikey.missing");
    }
}
