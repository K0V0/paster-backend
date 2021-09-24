package com.kovospace.paster.base.services;

import com.kovospace.paster.base.exceptions.jwtTokenException.InvalidJwtTokenException;
import com.kovospace.paster.user.models.User;

public interface JwtService {

  String generate(User user);
  long parse(String jwtToken) throws InvalidJwtTokenException;

}
