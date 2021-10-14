package com.kovospace.paster.item.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ItemControllerAddTest {

  private String token;

  @Value("${board.preview-max-length}")
  private int maxTextLength;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void init() {
    userRepository.deleteAll();
    User user = new User();
    user.setName("Anatoli Datlov");
    user.setPasword(bCryptPasswordEncoder.encode("AZ-5"));
    userRepository.save(user);
    user.setJwtToken(jwtService.generate(user));
    this.token = user.getJwtToken();
  }

  @Test
  @Order(1)
  public void unauthorizedRequestNotOK() throws Exception {
    mockMvc
        .perform(post("/board/item"))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing Authentication header.")));
  }

  @Test
  @Order(2)
  public void requestBodyEmpty() throws Exception {
    mockMvc
        .perform(post("/board/item")
            .header("Authorization", token))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(3)
  public void requestBodyMalformed() throws Exception {
    mockMvc
        .perform(post("/board/item")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{kjhmbn}"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyWrongMediaType() throws Exception {
    mockMvc
        .perform(
            post("/board/item")
                .header("Authorization", token)
                .content("{\"text\":\"bla bla bla\"}"))
        .andExpect(status().is(415));
    //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(5)
  public void requestJsonEmpty() throws Exception {
    mockMvc
        .perform(
            post("/board/item")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text", is("Item not presented.")));;
  }

  @Test
  @Order(6)
  public void contentNull() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();

    mockMvc
        .perform(
            post("/board/item")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text", is("Item not presented.")));
  }

  @Test
  @Order(7)
  public void contentIsEmpty() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("");

    mockMvc
        .perform(
            post("/board/item")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text", is("Nothing pasted.")));
  }

  @Test
  @Order(8)
  public void maximumSizeReached() throws Exception {
    String tst = generate(() -> "a").limit(maxTextLength + 1).collect(joining());
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText(tst);

    mockMvc
        .perform(
            post("/board/item")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text", is("Maximum allowed size exceeded.")));
  }

}
