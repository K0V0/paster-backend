package com.kovospace.paster.user.exceptions;

public class UserRegisterAlreadyOccupiedException extends UserException {

  public UserRegisterAlreadyOccupiedException() {
    super("user.register.username.taken");
  }
}
