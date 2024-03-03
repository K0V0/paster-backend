package com.kovospace.paster.item.exceptions.v2;

import com.kovospace.paster.item.exceptions.ItemException;

public abstract class FileException extends ItemException {
    public FileException() {}
    public FileException(final String code) {
        super(code);
    }
    public FileException(final String code, final String message) { super(code, message); }
}
