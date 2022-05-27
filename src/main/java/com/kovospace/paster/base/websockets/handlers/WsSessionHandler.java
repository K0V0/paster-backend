package com.kovospace.paster.base.websockets.handlers;

import javax.websocket.Session;
import java.util.Set;

public interface WsSessionHandler {

  Set<Session> getUserSessions(long userId);

  void addUserSession(long userId, Session session);

  void removeSession(Session session);

}
