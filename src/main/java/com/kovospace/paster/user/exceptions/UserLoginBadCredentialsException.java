package com.kovospace.paster.user.exceptions;

public class UserLoginBadCredentialsException extends UserException {

  public UserLoginBadCredentialsException() {
    super("Wrong combination of username and password.");
  }
}
