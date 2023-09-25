package com.kovospace.paster.base.swagger.base;

public enum ExpectedDocumentType {
    YAML("yml"),
    JSON("json");

    private String extension;

    ExpectedDocumentType(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
