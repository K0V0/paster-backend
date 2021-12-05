package com.kovospace.paster.base.websockets.controllers;

import com.kovospace.paster.base.configurations.websocketConfig.CustomSpringConfigurator;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.websockets.dtos.WsRequestDTO;
import com.kovospace.paster.base.websockets.encoders.WsRequestDecoder;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.services.UserSessionService;
import com.kovospace.paster.user.services.UserService;
import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

// TODO do message napchat jwt token a overit
// TODO user's sessions service - aktivne ostatne zariadenia

@Component
@ServerEndpoint(
    value = "/websocket",
    decoders = WsRequestDecoder.class,
    configurator = CustomSpringConfigurator.class
)
public class WsServer {
  private UserSessionService userSessionService;
  private JwtService jwtService;
  private UserService userService;

  @Autowired
  public WsServer(
      UserSessionService userSessionService,
      JwtService jwtService
  ) {
    this.userSessionService = userSessionService;
    this.jwtService = jwtService;
  }

  @OnOpen
  public void onOpen(Session session) throws IOException {
    System.out.println("websocket connection open");
    System.out.println(session.getId());
  }

  @OnMessage
  public void onMessage(Session session, WsRequestDTO wsRequestDTO) throws WsException {
    long userId;
    System.out.println(session.getId());
    System.out.println(wsRequestDTO);
    // TODO skontrolovat funkcnost jwt service (sfailuje asi pre bearer)
    // TODO exceptions handling vo WS controlleroch s @ServerEndpoint
    /*if ((userId = jwtService.parse(wsRequestDTO.getJwtToken())) > -1) {
      userSessionService.store(userId, session.getId());
    }*/
    // Handle new messages
  }

  @OnClose
  public void onClose(Session session) {

    // WebSocket connection closes
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
  }

}


