package com.kovospace.paster.base.websockets.controllers;

import com.google.gson.Gson;
import com.kovospace.paster.base.configurations.websocketConfig.CustomSpringConfigurator;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.websockets.dtos.WsAutosyncReplyDTO;
import com.kovospace.paster.base.websockets.dtos.WsRequestDTO;
import com.kovospace.paster.base.websockets.encoders.WsRequestDecoder;
import com.kovospace.paster.base.websockets.exceptions.JwtTokenNotIncludedException;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.services.UserSessionService;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
    configurator = CustomSpringConfigurator.class
)
public class WsServer {
  private UserSessionService userSessionService;
  private JwtService jwtService;
  private Gson gson;

  @Autowired
  public WsServer(
      UserSessionService userSessionService,
      JwtService jwtService,
      Gson gson
  ) {
    this.userSessionService = userSessionService;
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
    userSessionService.store(userId, session.getId());
    session.getUserProperties().put("userId", userId);
    // TODO nastavenie autosync v ucte pouzivatela
    session.getAsyncRemote().sendText(gson.toJson(new WsAutosyncReplyDTO(true)));
  }

  @OnMessage
  public void onMessage(Session session, WsRequestDTO wsRequestDTO) {
    System.out.println("on message");
    System.out.println(wsRequestDTO.getMessage());
    System.out.println(session.getOpenSessions());

    // testovaci kod - posielanie sprav pre vsetky sessions pre usera
    long userId = (long) session.getUserProperties().get("userId");
    // better performance to use set instead contains and list of elements
    Set<String> userSessions = new HashSet<>(userSessionService.getAllUserSessions(userId));
    session.getOpenSessions()
        .stream()
        .filter(sess -> userSessions.contains(sess.getId()))
        .forEach(sess -> {
          sess.getAsyncRemote().sendText(wsRequestDTO.getMessage());
        });
  }

  @OnClose
  public void onClose(Session session) {
    userSessionService.detach(session.getId());
    System.out.println("session closed");
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    System.out.println(throwable.getMessage());
    if (throwable instanceof WsException || throwable instanceof JwtException) {
      //System.out.println(throwable.getMessage());
      session.getAsyncRemote().sendText(throwable.getMessage());
    } else {
      session.close();
    }
  }

}


