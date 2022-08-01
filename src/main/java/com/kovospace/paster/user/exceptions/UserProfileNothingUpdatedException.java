package com.kovospace.paster.user.exceptions;

public class UserProfileNothingUpdatedException extends UserException {

    public UserProfileNothingUpdatedException() {
        super("user.profile.update.nothing");
    }
}
