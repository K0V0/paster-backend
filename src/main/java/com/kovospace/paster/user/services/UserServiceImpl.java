package com.kovospace.paster.user.services;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.user.exceptions.UserException;
import com.kovospace.paster.user.exceptions.UserLoginBadCredentialsException;
import com.kovospace.paster.user.exceptions.UserNotFoundException;
import com.kovospace.paster.user.exceptions.UserProfileNothingUpdatedException;
import com.kovospace.paster.user.exceptions.UserRegisterAlreadyOccupiedException;
import com.kovospace.paster.user.exceptions.UserRegisterPasswordsNotMatchException;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repo;
  private final BCryptPasswordEncoder encoder;
  private final JwtService jwtService;

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
  public User register(String name, String pass, String pass2, String email) throws UserException {
    if (!(pass.equals(pass2))) { throw new UserRegisterPasswordsNotMatchException(); }
    if (repo.findFirstByName(name).isPresent()) { throw new UserRegisterAlreadyOccupiedException(); }
    User user = new User();
    user.setName(name);
    user.setPasword(encoder.encode(pass));
    user.setEmail(email);
    repo.save(user);
    user.setJwtToken(jwtService.generate(user));
    return user;
  }

  @Override
  public boolean exist(long userId) {
    return repo.findById(userId).isPresent();
  }

  @Override
  public User getProfile(long userId) throws UserException {
    Optional<User> userOpt = repo.findById(userId);
    if (userOpt.isPresent()) {
      return userOpt.get();
    }
    throw new UserNotFoundException();
  }

  @Override
  public User updateProfile(long userId, String filePath) throws UserException {
    //TODO file upload
    //repo.findById(userId).ifPresent(user -> {
    Optional<User> userOpt = repo.findById(userId);
    if (userOpt.isPresent()) {
      if (filePath == null) { throw new UserProfileNothingUpdatedException(); }
      User user = userOpt.get();
      if (filePath != null) {
        //TODO decide format of filepath + parse
        user.setAvatarFileName(filePath);
      }
      repo.save(user);
      return user;
    }
    throw new UserNotFoundException();
  }

}
