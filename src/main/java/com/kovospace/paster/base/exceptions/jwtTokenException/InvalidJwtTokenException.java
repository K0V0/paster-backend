package com.kovospace.paster.base.exceptions.jwtTokenException;

import com.kovospace.paster.base.exceptions.JwtTokenException;

public class InvalidJwtTokenException extends JwtTokenException {

  public InvalidJwtTokenException(String msg) {
    super(msg);
  }
}
