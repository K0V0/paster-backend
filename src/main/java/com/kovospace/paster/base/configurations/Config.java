package com.kovospace.paster.base.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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
