package com.kovospace.paster.base.services;

import com.kovospace.paster.base.websockets.handlers.WsSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

  private WsSessionHandler wsSessionHandler;

  @Autowired
  public WebsocketServiceImpl(WsSessionHandler wsSessionHandler) {
    this.wsSessionHandler = wsSessionHandler;
  }

  @Override
  public void notifyForChanges(long userId) {
    System.out.println("notify changes");
    System.out.println(userId);
    System.out.println(wsSessionHandler.getUserSessions(userId));
    wsSessionHandler
        .getUserSessions(userId)
        .forEach(session -> session.getAsyncRemote().sendText("update"));
  }
}
