package com.kovospace.paster.user.services;

import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.models.User;

public interface UserService {

  User login(String name, String pass) throws UserLoginBadCredentialsException;

  User register(String name, String pass, String pass2, String email) throws UserException;

}
