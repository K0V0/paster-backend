package com.kovospace.paster.user.services;

import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository repo;

  @Autowired
  public UserServiceImpl(UserRepository repo) { this.repo = repo; }

  @Override
  public User login(String name, String pass) {
    return null;
  }

}
