package com.kovospace.paster.base.configurations.websocketConfig;

import com.kovospace.paster.base.configurations.websocketConfig.WsServerSpringConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
public class WebsocketConfig /*extends AbstractWebSocketMessageBrokerConfigurer*/ {

  @Bean
  public ServerEndpointExporter serverEndpoint() {
    return new ServerEndpointExporter();
  }

  @Bean
  public WsServerSpringConfigurator customSpringConfigurator() {
    return new WsServerSpringConfigurator();
  }

  /*@Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/your/topic");
    config.setApplicationDestinationPrefixes("/yourapp");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/your/endpoint").setAllowedOrigins("http://localhost:4200").withSockJS();
  }*/

}