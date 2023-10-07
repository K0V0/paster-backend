package com.kovospace.paster.base.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.v3.core.util.Json;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class Config {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public ObjectMapper objectMapper() { return new ObjectMapper(); }

  @Bean
  public BCryptPasswordEncoder encoder() { return new BCryptPasswordEncoder(); }

  @Bean
  public Gson gson() { return new Gson(); }

}
