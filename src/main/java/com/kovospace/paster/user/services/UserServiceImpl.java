package com.kovospace.paster.user.services;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.exceptions.UserRegisterAlreadyOccupiedException;
import com.kovospace.paster.user.exceptions.UserRegisterPasswordsNotMatchException;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository repo;
  private BCryptPasswordEncoder encoder;
  private JwtService jwtService;

  @Autowired
  public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder encoder, JwtService jwtService) {
    this.repo = repo;
    this.encoder = encoder;
    this.jwtService = jwtService;
  }

  @Override
  public User login(String name, String pass) throws UserLoginBadCredentialsException {
    Optional<User> user = repo.findFirstByName(name);
    if (user.isPresent()) {
      User u = user.get();
      if (encoder.matches(pass, u.getPasword())) {
        u.setJwtToken(jwtService.generate(user.get()));
        return u;
      }
    }
    throw new UserLoginBadCredentialsException();
  }

  @Override
  public User register(String name, String pass, String pass2) throws UserException {
    if (!(pass.equals(pass2))) { throw new UserRegisterPasswordsNotMatchException(); }
    if (repo.findFirstByName(name).isPresent()) { throw new UserRegisterAlreadyOccupiedException(); }
    User user = new User();
    user.setName(name);
    user.setPasword(encoder.encode(pass));
    repo.save(user);
    user.setJwtToken(jwtService.generate(user));
    return user;
  }

}
