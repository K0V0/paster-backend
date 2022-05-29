package com.kovospace.paster.base.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // TODO ulozit allowed origins niekde do configu
    configuration.setAllowedOrigins(Arrays.asList(
            "http://0.0.0.0:4200",
            "http://0.0.0.0:6060",
            "http://kovo.space:4200",
            "http://kovo.space:6060",
            "http://localhost:4200",
            "http://localhost:6060",
            "http://paster.cloud",
            "http://www.paster.cloud",
            "https://paster.cloud",
            "https://www.paster.cloud"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    //configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
