package com.kovospace.paster.base.websockets.handlers;

import java.util.Set;
import javax.websocket.Session;

public interface WsSessionHandler {

  Set<Session> getUserSessions(long userId);

  void addUserSession(long userId, Session session);

  void removeSession(Session session);

}
