package com.kovospace.paster.base.configurations.websocketConfig;

import com.kovospace.paster.base.configurations.websocketConfig.WsServerSpringConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebsocketConfig {

  @Bean
  public ServerEndpointExporter serverEndpoint() {
    return new ServerEndpointExporter();
  }

  @Bean
  public WsServerSpringConfigurator customSpringConfigurator() {
    return new WsServerSpringConfigurator();
  }
}