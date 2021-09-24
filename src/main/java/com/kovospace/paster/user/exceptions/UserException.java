package com.kovospace.paster.user.exceptions;

public abstract class UserException extends Exception {

  public UserException() {}
  public UserException(String msg) { super(msg); }
}
