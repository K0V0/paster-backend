package com.kovospace.paster.user.exceptions;

public class UserRegisterPasswordsNotMatchException extends UserException {

  public UserRegisterPasswordsNotMatchException() {
    super("user.register.passwordConfirmation.nomatch");
  }
}
