package com.kovospace.paster.base.websockets.controllers;

import com.google.gson.Gson;
import com.kovospace.paster.base.configurations.websocketConfig.WsServerSpringConfigurator;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.websockets.dtos.WsAutosyncReplyDTO;
import com.kovospace.paster.base.websockets.dtos.WsRequestDTO;
import com.kovospace.paster.base.websockets.encoders.WsRequestDecoder;
import com.kovospace.paster.base.websockets.exceptions.JwtTokenNotIncludedException;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.handlers.WsSessionHandler;
import com.kovospace.paster.base.websockets.handlers.WsSessionHandlerImpl;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Optional;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO user's sessions service - aktivne ostatne zariadenia
// TODO skontrolovat funkcnost jwt service (sfailuje asi pre bearer)
// TODO exceptions handling vo WS controlleroch s @ServerEndpoint
// TODO bacha na restart aplikacie, websocket session ids idu zase od nuly

@Component
@ServerEndpoint(
    value = "/websocket",
    decoders = WsRequestDecoder.class,
    configurator = WsServerSpringConfigurator.class
)
public class WsServer {
  private WsSessionHandler wsSessionHandler;
  private JwtService jwtService;
  private Gson gson;

  @Autowired
  public WsServer(
      //WsSessionService wsSessionService,
      WsSessionHandler wsSessionHandler,
      JwtService jwtService,
      Gson gson
  ) {
    //this.wsSessionService = wsSessionService;
    this.wsSessionHandler = wsSessionHandler;
    this.jwtService = jwtService;
    this.gson = gson;
  }

  @OnOpen
  public void onOpen(Session session) throws WsException, JwtException {
    System.out.println("websocket connection open");
    String jwtToken = Optional
        .ofNullable(session.getRequestParameterMap())
        .map(stringListMap -> stringListMap.get("jwtToken"))
        .map(strings -> strings.get(0))
        .orElseThrow(JwtTokenNotIncludedException::new);
    long userId = jwtService.parse("Bearer " + jwtToken);
    //wsSessionService.store(userId, session.getId());
    //session.getUserProperties().put("userId", userId);
    wsSessionHandler.addUserSession(userId, session);
    // TODO nastavenie autosync v ucte pouzivatela
    session.getAsyncRemote().sendText(gson.toJson(new WsAutosyncReplyDTO(true)));
  }

  @OnMessage
  public void onMessage(Session session, WsRequestDTO wsRequestDTO) {
    System.out.println("on message");
    System.out.println(wsRequestDTO.getMessage());
    System.out.println(wsSessionHandler.getUserSessions((long) session.getUserProperties().get("userId")));
    //System.out.println(session.getOpenSessions());

    //long userId = (long) session.getUserProperties().get("userId");


    // testovaci kod - posielanie sprav pre vsetky sessions pre usera
    // better performance to use set instead contains and list of elements
    /*Set<String> userSessions = new HashSet<>(wsSessionService.getAllUserSessions(userId));
    session.getOpenSessions()
        .stream()
        .filter(sess -> userSessions.contains(sess.getId()))
        .forEach(sess -> {
          sess.getAsyncRemote().sendText(wsRequestDTO.getMessage());
        });*/
  }

  @OnClose
  public void onClose(Session session) {
    //wsSessionService.detach(session.getId());
    wsSessionHandler.removeSession(session);
    System.out.println("session closed");
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    System.out.println(throwable.getMessage());
    if (throwable instanceof WsException || throwable instanceof JwtException) {
      System.out.println("send error message, but continue");
      session.getAsyncRemote().sendText(throwable.getMessage());
    } else {
      session.close();
    }
  }

}


