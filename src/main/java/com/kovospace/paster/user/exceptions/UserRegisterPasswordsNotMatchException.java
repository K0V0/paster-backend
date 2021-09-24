package com.kovospace.paster.user.exceptions;

public class UserRegisterPasswordsNotMatchException extends UserException {

  public UserRegisterPasswordsNotMatchException() {
    super("Password and its confirmation did not match.");
  }
}
