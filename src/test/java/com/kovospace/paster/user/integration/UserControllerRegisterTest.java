package com.kovospace.paster.user.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerRegisterTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(1)
  public void endpointFound() throws Exception {
    mockMvc
        .perform(post("/user/register"))
        .andExpect(status().is(400));
  }

  @Test
  @Order(2)
  public void getRequestNotAllowed() throws Exception {
    mockMvc
        .perform(get("/user/register"))
        .andExpect(status().is(405))
        .andExpect(jsonPath("$.message", is("Wrong HTTP method used.")));
  }

  @Test
  @Order(3)
  public void requestBodyEmpty() throws Exception {
    mockMvc
        .perform(post("/user/register"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyMalformed() throws Exception {
    mockMvc
        .perform(
            post("/user/register")
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
            post("/user/register")
                .content("{\"name\":\"comrade Testovic\",\"pass\":\"AZ-5\",\"pass2\":\"AZ-5\"}")
        )
        .andExpect(status().is(415));
    //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(6)
  public void requestJsonEmpty() throws Exception {
    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

  @Test
  @Order(7)
  public void usernameAndPasswordsNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

}
