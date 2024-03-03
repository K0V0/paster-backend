package com.kovospace.paster.item.exceptions.v2;

public class FileCreationException extends FileException {

    public FileCreationException() {
        super("item.filerepo.creation.wrong");
    }

    public FileCreationException(final String errorMessage) {
        super("item.filerepo.creation.wrong", errorMessage);
    }
}
