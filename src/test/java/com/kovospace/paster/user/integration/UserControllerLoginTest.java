package com.kovospace.paster.user.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.dtos.ErrorResponseDTO;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import java.util.Objects;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerLoginTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  //@MockBean
  //private UserControllerResponder responder;

  @Test
  @Order(1)
  public void endpointNotFound() throws Exception {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    ResponseEntity<ErrorResponseDTO> errorResponse = testRestTemplate
        .getForEntity("http://0.0.0.0:4004/user/logi", ErrorResponseDTO.class);
    System.out.println(Objects.requireNonNull(errorResponse.getBody()).getMessage());
    MvcResult res = mockMvc
        .perform(post("/user/logi"))
        .andDo(print())
        .andExpect(status().is(404))
        .andReturn();
    System.out.println("-------------------------");
    System.out.println(res.getResponse().getContentAsString());
        //.andExpect(jsonPath("$.message", is("Endpoint not found.")));
  }

  @Test
  @Order(2)
  public void getRequestNotAllowed() throws Exception {
    mockMvc
        .perform(get("/user/login"))
        .andExpect(status().is(405))
        .andExpect(jsonPath("$.message", is("Wrong HTTP method used.")));
  }

  @Test
  @Order(3)
  public void requestBodyEmpty() throws Exception {
    mockMvc
        .perform(post("/user/login"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyMalformed() throws Exception {
    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{kjhmbn}")
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(5)
  public void requestBodyWrongMediaType() throws Exception {
    mockMvc
        .perform(
            post("/user/login")
                .content("{\"name\":\"comrade Testovic\",\"pass\":\"AZ-5\"}")
        )
        .andExpect(status().is(415));
        //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(6)
  public void requestJsonEmpty() throws Exception {
    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

  @Test
  @Order(7)
  public void usernameAndPasswordNull() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

  @Test
  @Order(8)
  public void usernameAndPasswordEmpty() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("");
    user.setPass("");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username and password fields are empty.")));
  }

  @Test
  @Order(9)
  public void usernameNull() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  @Order(10)
  public void usernameEmpty() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username field is empty.")));
  }

  @Test
  @Order(11)
  public void passwordNull() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade Testovic");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Password is required.")));
  }

  @Test
  @Order(12)
  public void passwordEmpty() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade Testovic");
    user.setPass("");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Password field is empty.")));
  }

  @Test
  @Order(13)
  public void passwordShort() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade Testovic");
    user.setPass("1234567");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Password must have at least 8 characters.")));
  }

  @Test
  @Order(14)
  public void usernameAreSpaces() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("     ");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username field is empty.")));
  }

  @Test
  @Order(15)
  public void passwordAreSpaces() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade Testovic");
    user.setPass("       ");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Password field is empty.")));
  }

  @Test
  @Order(16)
  public void usernameBeginWithSpace() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName(" comrade_Testovic");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Whitespaces not allowed anywhere.")));
  }

  @Test
  @Order(17)
  public void usernameWithSpace() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade Testovic");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Whitespaces not allowed anywhere.")));
  }

  @Test
  @Order(18)
  public void usernameEndsWithSpace() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("comrade_Testovic ");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Whitespaces not allowed anywhere.")));
  }
}
