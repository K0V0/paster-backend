package com.kovospace.paster.user.exceptions;

public class UserLoginBadCredentialsException extends UserException {

    public UserLoginBadCredentialsException() { super("user.login.credentials.wrong"); }
}
