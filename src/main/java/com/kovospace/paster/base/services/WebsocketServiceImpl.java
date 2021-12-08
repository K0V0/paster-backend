package com.kovospace.paster.base.services;

import com.google.gson.Gson;
import com.kovospace.paster.base.websockets.dtos.WsAutosyncReplyDTO;
import com.kovospace.paster.base.websockets.handlers.WsSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

  private WsSessionHandler wsSessionHandler;
  private Gson gson;

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
    System.out.println("notify changes");
    System.out.println(userId);
    System.out.println(wsSessionHandler.getUserSessions(userId));
    wsSessionHandler
        .getUserSessions(userId)
        .forEach(session -> session.getAsyncRemote().sendText(gson.toJson(new WsAutosyncReplyDTO())));
    // TODO mozno bude treba vlozit nieco ako casovu znacku do autosyncreply
  }
}
