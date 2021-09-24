package com.kovospace.paster.user.exceptions;

public class UserRegisterAlreadyOccupiedException extends UserException {

  public UserRegisterAlreadyOccupiedException() {
    super("Username is already taken.");
  }
}
