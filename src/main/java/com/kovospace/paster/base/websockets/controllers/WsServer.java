package com.kovospace.paster.base.websockets.controllers;

import com.google.gson.Gson;
import com.kovospace.paster.base.configurations.websocketConfig.WsServerSpringConfigurator;
import com.kovospace.paster.base.services.ApiKeyService;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.websockets.dtos.WsAutosyncReplyDTO;
import com.kovospace.paster.base.websockets.dtos.WsRequestDTO;
import com.kovospace.paster.base.websockets.encoders.WsRequestDecoder;
import com.kovospace.paster.base.websockets.exceptions.ApiKeyNotIncludedException;
import com.kovospace.paster.base.websockets.exceptions.ApiKeyNotValidException;
import com.kovospace.paster.base.websockets.exceptions.JwtTokenNotIncludedException;
import com.kovospace.paster.base.websockets.exceptions.WsException;
import com.kovospace.paster.base.websockets.handlers.WsSessionHandler;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.naming.AuthenticationException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;

// TODO bacha na restart aplikacie, websocket session ids idu zase od nuly - defenzivne programovanie
// TODO nejaky logging namiesto sout()

@Component
@CrossOrigin
@ServerEndpoint(
    value = "/websocket",
    decoders = WsRequestDecoder.class,
    configurator = WsServerSpringConfigurator.class
)
public class WsServer {
  private WsSessionHandler wsSessionHandler;
  private JwtService jwtService;
  private ApiKeyService apiKeyService;
  private Gson gson;

  @Autowired
  public WsServer(
      WsSessionHandler wsSessionHandler,
      JwtService jwtService,
      ApiKeyService apiKeyService,
      Gson gson
  ) {
    this.wsSessionHandler = wsSessionHandler;
    this.jwtService = jwtService;
    this.apiKeyService = apiKeyService;
    this.gson = gson;
  }

  @OnOpen
  public void onOpen(Session session) throws WsException, JwtException {
    System.out.println("websocket connection open");
    String apiKey = getParamOrThrow("apiKey", new ApiKeyNotIncludedException(), session);
    if (!apiKeyService.isValid(apiKey)) { throw new ApiKeyNotValidException(); }
    String jwtToken = getParamOrThrow("jwtToken", new JwtTokenNotIncludedException(), session);
    long userId = jwtService.parse("Bearer " + jwtToken);
    wsSessionHandler.addUserSession(userId, session);
    // TODO nastavenie na moznost vypnut autosync v ucte pouzivatela
    session.getAsyncRemote().sendText(gson.toJson(new WsAutosyncReplyDTO(true)));
  }

  @OnMessage
  public void onMessage(Session session, WsRequestDTO wsRequestDTO) {
    System.out.println("on message");
    System.out.println(wsRequestDTO.getMessage());
  }

  @OnClose
  public void onClose(Session session) {
    wsSessionHandler.removeSession(session);
    System.out.println("websocket session closed");
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    if (throwable instanceof WsException || throwable instanceof JwtException || throwable instanceof AuthenticationException) {
      System.out.println("send error message, but continue");
      session.getAsyncRemote().sendText(throwable.getMessage());
    } else {
      session.close();
    }
    System.out.println(throwable.getMessage());
  }

  private String getParamOrThrow(String param, WsException exception, Session session)
  throws WsException
  {
    return Optional
            .ofNullable(session.getRequestParameterMap())
            .map(stringListMap -> stringListMap.get(param))
            .map(strings -> strings.get(0))
            .orElseThrow(() -> exception);
  }

}


