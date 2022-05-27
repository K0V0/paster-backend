package com.kovospace.paster.base.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ApiKeyInvalidException extends AuthenticationException {

    public ApiKeyInvalidException() {
        super("general.endpoint.authentication.apikey.wrong");
    }
}
