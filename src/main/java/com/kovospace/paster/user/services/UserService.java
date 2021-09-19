package com.kovospace.paster.user.services;

import com.kovospace.paster.user.models.User;

public interface UserService {

  User login(String name, String pass);

}
