package com.kovospace.paster.base.websockets.encoders;

import com.google.gson.Gson;
import com.kovospace.paster.base.websockets.dtos.WsRequestDTO;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WsRequestDecoder implements Decoder.Text<WsRequestDTO> {

  private static Gson gson = new Gson();

  @Override
  public WsRequestDTO decode(String s) throws DecodeException {
    return gson.fromJson(s, WsRequestDTO.class);
  }

  @Override
  public boolean willDecode(String s) {
    return (s != null);
  }

  @Override
  public void init(EndpointConfig endpointConfig) {

  }

  @Override
  public void destroy() {

  }
}
