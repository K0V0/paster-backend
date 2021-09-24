package com.kovospace.paster.user.services;

import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.models.User;

public interface UserService {

  User login(String name, String pass);

  User register(String name, String pass, String pass2) throws UserException;

}
