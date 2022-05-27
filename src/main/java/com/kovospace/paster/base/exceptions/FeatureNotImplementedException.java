package com.kovospace.paster.base.exceptions;

public class FeatureNotImplementedException extends Exception {

    public FeatureNotImplementedException() {
        super("general.implementation.missing");
    }
}
