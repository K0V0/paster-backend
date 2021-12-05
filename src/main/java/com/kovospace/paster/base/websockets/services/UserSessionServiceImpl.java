package com.kovospace.paster.base.websockets.services;

import com.kovospace.paster.base.websockets.exceptions.UserNotFoundException;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.models.UserSession;
import com.kovospace.paster.base.websockets.repositories.UserSessionRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSessionServiceImpl implements UserSessionService {
  private UserRepository userRepo;
  private UserSessionRepository sessionRepo;

  @Autowired
  public UserSessionServiceImpl(
      UserRepository userRepo,
      UserSessionRepository sessionRepo
  ) {
    this.sessionRepo = sessionRepo;
    this.userRepo = userRepo;
  }

  @Override
  public void store(long userId, String sessionId) throws WsException {
    Optional<User> userOptional = userRepo.findById(userId);
    if (userOptional.isEmpty()) { throw new UserNotFoundException(); }
    sessionRepo
        .findByUserAndAndSessionId(userOptional.get(), sessionId)
        .orElse(sessionRepo.save(new UserSession(userOptional.get(), sessionId)));
    // TODO zistit ci nemozno poriesit neukladanie not unique user a session na urovni modelu
    //  (okrem validacie ktora vypluje vynimku) ?
  }

  @Override
  public void detach(String sessionId) {
    sessionRepo.deleteAllBySessionId(sessionId);
  }

}
