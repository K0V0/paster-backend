package com.kovospace.paster.item.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

public class ItemControllerDeleteTest extends KovoTest {

  @Override
  protected String getEndpoint() {
    return "/board/item";
  }

  @Override
  protected String getApiPrefix() {
    return "/api/v1";
  }

  private String token;

  @Value("${jwt.prefix}")
  private String prefix;

  @Autowired
  private MockMvc mockMvc;

  //@Autowired
  //private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

}
