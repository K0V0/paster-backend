package com.kovospace.paster.user.services;

import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.exceptions.UserNotFoundException;
import com.kovospace.paster.user.exceptions.UserProfileNothingUpdatedException;
import com.kovospace.paster.user.models.User;

public interface UserService {

  User login(String name, String pass) throws UserLoginBadCredentialsException;

  User register(String name, String pass, String pass2, String email) throws UserException;

  User changePassword(String email, String oldPass, String oldPass2, String newPass) throws UserException;

  boolean exist(long userId);

  User getProfile(long userId) throws UserException;

  User updateProfile(long userId, String filePath, String email) throws UserException;

}
