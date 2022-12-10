package com.kovospace.paster.item.exceptions.v2;

public class FileNotFoundException extends FileException {
    public FileNotFoundException() {
        super("item.filerepo.file.missing");
    }
}
