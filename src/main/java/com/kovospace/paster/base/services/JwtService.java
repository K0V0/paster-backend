package com.kovospace.paster.base.services;

import com.kovospace.paster.user.models.User;
import io.jsonwebtoken.JwtException;

public interface JwtService {

  String generate(User user);
  long parse(String jwtToken) throws JwtException;

}
