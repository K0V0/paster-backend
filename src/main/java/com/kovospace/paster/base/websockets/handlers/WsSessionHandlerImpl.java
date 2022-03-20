package com.kovospace.paster.base.websockets.handlers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import org.springframework.stereotype.Service;

@Service
public class WsSessionHandlerImpl implements WsSessionHandler {

  // TODO concurrent hashmap editing set<> read if is safe
  private static final ConcurrentHashMap<Long, Set<Session>> WS_SESSIONS = new ConcurrentHashMap<>();

  @Override
  public Set<Session> getUserSessions(long userId) {
    return Optional
        .ofNullable(WS_SESSIONS.get(userId))
        .orElseGet(HashSet::new);
  }

  @Override
  public void addUserSession(long userId, Session session) {
    session.getUserProperties().put("userId", userId);
    if (!WS_SESSIONS.containsKey(userId)) {
      WS_SESSIONS.put(userId, new HashSet<Session>() {{ add(session); }});
    } else {
      // vraj zbytocny check TODO precitat nieco
      //if (!WS_SESSIONS.get(userId).contains(session)) {
        WS_SESSIONS.get(userId).add(session);
      //}
    }
  }

  @Override
  public void removeSession(Session session) {
    // TODO viac safe
    long userId = (long) session.getUserProperties().get("userId");
    WS_SESSIONS.get(userId).remove(session);
  }

}
