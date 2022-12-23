package com.kovospace.paster.base.services;

import com.google.gson.Gson;
import com.kovospace.paster.base.websockets.dtos.WsAutosyncReplyDTO;
import com.kovospace.paster.base.websockets.handlers.WsSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.Set;

@Service
public class WebsocketServiceImpl implements WebsocketService {

  private static final Logger logger = LoggerFactory.getLogger(WebsocketServiceImpl.class);

  private final WsSessionHandler wsSessionHandler;
  private final Gson gson;

  @Autowired
  public WebsocketServiceImpl(
      WsSessionHandler wsSessionHandler,
      Gson gson
  ) {
    this.wsSessionHandler = wsSessionHandler;
    this.gson = gson;
  }

  @Override
  public void notifyForChanges(long userId) {
    Set<Session> userSessions = wsSessionHandler.getUserSessions(userId);
    wsSessionHandler
        .getUserSessions(userId)
        .forEach(session -> session.getAsyncRemote().sendText(gson.toJson(new WsAutosyncReplyDTO())));
    // TODO mozno bude treba vlozit nieco ako casovu znacku do autosyncreply

    logger.info(String.format("User with user ID %s notified about new content, userSessions: %s", userId, userSessions));
  }
}
