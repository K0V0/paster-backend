package com.kovospace.paster.base.exceptions;

public abstract class JwtTokenException extends Exception {

  public JwtTokenException() {}

  public JwtTokenException(String msg) {
    super(msg);
  }

}
