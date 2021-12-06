package com.kovospace.paster.base.websockets.services;

import com.kovospace.paster.base.websockets.exceptions.UserNotFoundException;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.models.WsSession;
import com.kovospace.paster.base.websockets.repositories.WsSessionRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WsSessionServiceImpl implements WsSessionService {
  private UserRepository userRepo;
  //private WsSessionRepository sessionRepo;

  @Autowired
  public WsSessionServiceImpl(
      UserRepository userRepo//,
      //WsSessionRepository sessionRepo
  ) {
    //this.sessionRepo = sessionRepo;
    this.userRepo = userRepo;
  }

  @Override
  public void store(long userId, String sessionId) throws WsException {
    Optional<User> userOptional = userRepo.findById(userId);
    if (userOptional.isEmpty()) { throw new UserNotFoundException(); }
    /*sessionRepo
        .findByUserAndAndSessionId(userOptional.get(), sessionId)
        .orElse(sessionRepo.save(new WsSession(userOptional.get(), sessionId)));*/
    // TODO zistit ci nemozno poriesit neukladanie not unique user a session na urovni modelu
    //  (okrem validacie ktora vypluje vynimku) ?
  }

  @Override
  public void detach(String sessionId) {
    // musi byt takto lebo inak noEntityManager for current thread error
    /*sessionRepo
        .findAllBySessionIdEquals(sessionId)
        .forEach(wsSession -> sessionRepo.delete(wsSession));*/
  }

  @Override
  public List<String> getAllUserSessions(long userId) {
    return userRepo
        .findById(userId)
        .map(User::getWsSessions)
        .orElse(new ArrayList<>())
        .stream()
        .map(WsSession::getSessionId)
        .collect(Collectors.toList());
  }

}
