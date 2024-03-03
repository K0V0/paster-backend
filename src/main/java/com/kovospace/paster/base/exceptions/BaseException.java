package com.kovospace.paster.base.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseException extends Exception {

    private static final Logger LOG = LoggerFactory.getLogger(BaseException.class);

    private String code;
    private String message;

    public BaseException() {

    }

    public BaseException(final String code) {
        this.code = code;
    }

    public BaseException(final String code, final String message) {
        this.code = code;
        this.message = message;

        LOG.error(code + ": " + message);
    }
}
