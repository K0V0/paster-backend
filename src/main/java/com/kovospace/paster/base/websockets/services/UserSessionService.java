package com.kovospace.paster.base.websockets.services;

import com.kovospace.paster.base.websockets.exceptions.WsException;

public interface UserSessionService {
  void store(long userId, String sessionId) throws WsException;
  void detach(String sessionId);
}