package com.kovospace.paster.user.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @MockBean
  private TimeService timeService;

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
        .andExpect(jsonPath("$.messages.length()", is(3)))
        .andExpect(jsonPath("$.messages.name", is("Username is required.")))
        .andExpect(jsonPath("$.messages.pass", is("Password is required.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation is required.")));
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
        .andExpect(jsonPath("$.messages.length()", is(3)))
        .andExpect(jsonPath("$.messages.name", is("Username is required.")))
        .andExpect(jsonPath("$.messages.pass", is("Password is required.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation is required.")));
  }

  @Test
  @Order(8)
  public void usernameAndPasswordsEmpty() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("");
    user.setPass("");
    user.setPass2("");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(3)))
        .andExpect(jsonPath("$.messages.name", is("Username field is empty.")))
        .andExpect(jsonPath("$.messages.pass", is("Password field is empty.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation field is empty.")));
  }

  @Test
  @Order(9)
  public void usernameNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Username is required.")));
  }

  @Test
  @Order(10)
  public void usernameEmpty() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Username field is empty.")));
  }

  @Test
  @Order(11)
  public void passwordNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Password is required.")));
  }

  @Test
  @Order(12)
  public void passwordEmpty() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Password field is empty.")));
  }

  @Test
  @Order(13)
  public void passwordConfirmationNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation is required.")));
  }

  @Test
  @Order(14)
  public void passwordConfirmationEmpty() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation field is empty.")));
  }

  @Test
  @Order(15)
  public void usernameIsJustSpaces() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("       ");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Username field is empty.")));
  }

  @Test
  @Order(16)
  public void passwordIsJustSpaces() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("       ");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Password field is empty.")));
  }

  @Test
  @Order(17)
  public void passwordConfirmationIsJustSpaces() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("       ");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation field is empty.")));
  }

  @Test
  @Order(18)
  public void usernameBeginWithSpace() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName(" comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Your username begins with space.")));
  }

  @Test
  @Order(19)
  public void usernameWithSpace() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade testovic");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Your username contains space(s).")));
  }

  @Test
  @Order(20)
  public void usernameEndsWithSpace() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic ");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Your username ends with space.")));
  }

  @Test
  @Order(21)
  public void usernameContainsNotAllowedChars() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("com#rade_te/sto?vic");
    user.setPass("12345678");
    user.setPass2("12345678");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.name", is("Your username contains not allowed characters.\n"
            + "Allowed characters are letters, numbers, underscores, dashes and dots.")));
  }

  @Test
  @Order(22)
  public void passwordShort() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("1234567");
    user.setPass2("1234567");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Password must have at least 8 characters.")));
  }

  @Test
  @Order(23)
  public void passwordStartsOrEndsWithSpace() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass(" 1234567");
    user.setPass2(" 1234567");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Your password is starting or ending with space(s).")));
  }

  @Test
  @Order(24)
  public void passwordStartsOrEndsWithSpace2() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("1234567 ");
    user.setPass2("1234567 ");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.pass", is("Your password is starting or ending with space(s).")));
  }

  @Test
  @Order(25)
  public void passwordAndConfirmationDidNotMatch() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345679");

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(409))
        .andExpect(jsonPath("$.message", is("Password and its confirmation did not match.")));
  }

  @Test
  @Order(26)
  public void usernameAlreadyTaken() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");

    User dbUser = new User();
    dbUser.setName("comrade_testovic");
    dbUser.setPasword(bCryptPasswordEncoder.encode("12345678"));
    userRepository.save(dbUser);

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(403))
        .andExpect(jsonPath("$.message", is("Username is already taken.")));

    userRepository.deleteAll();
  }

  @Test
  @Order(27)
  public void usernameSaved() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");

    Mockito.when(timeService.getTime()).thenReturn(1234567890L);

    /*User dbUser = new User();
    dbUser.setName("comrade_testovic");
    dbUser.setPasword(bCryptPasswordEncoder.encode("12345678"));
    userRepository.save(dbUser);*/

    mockMvc
        .perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.jwtToken", is("")));

    //userRepository.deleteAll();
  }

}
