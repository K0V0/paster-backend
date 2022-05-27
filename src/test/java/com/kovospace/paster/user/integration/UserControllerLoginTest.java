package com.kovospace.paster.user.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.user.dtos.UserLoginRequestDTO;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerLoginTest extends KovoTest {

  private static User user;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @MockBean
  private TimeService timeService;

  private final UserLoginDtoPreparer namePreparer = new UserLoginDtoPreparer("name");

  private final UserLoginDtoPreparer passPreparer = new UserLoginDtoPreparer("pass");

  private class UserLoginDtoPreparer extends DtoPreparer<String, UserLoginRequestDTO> {
    public UserLoginDtoPreparer(String field) {
      super(field);
      dto = new UserLoginRequestDTO();
      dto.setName("Comrade_Testovic");
      dto.setPass("12345678");
    }
  }

  protected String getEndpoint() { return "/user/login"; }

  protected String getApiPrefix() { return "/api/v1"; }

  @Test
  @Order(1)
  public void endpointNotFound() throws Exception {
    postRequest()
            .withUrl(getApiPrefix() + "/user/logi")
            .run()
            .andExpect(status().is(404))
            .andReturn();
            //.andExpect(jsonPath("$.message", is("Endpoint not found.")));*/
  }

  @Test
  @Order(2)
  public void getRequestNotAllowed() throws Exception {
    getRequest().run()
            .andExpect(status().is(405))
            .andExpect(jsonPath("$.message", is("Wrong HTTP method used.")));
  }

  @Test
  @Order(3)
  public void requestBodyEmpty() throws Exception {
    postRequest().run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyMalformed() throws Exception {
    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent("{kjhmbn}")
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(5)
  public void requestBodyWrongMediaType() throws Exception {
    postRequest()
            .withMediaContent("{\"name\":\"comrade Testovic\",\"pass\":\"AZ-5\"}")
            .run()
            .andExpect(status().is(415));
            //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(6)
  public void requestJsonEmpty() throws Exception {
    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent("{}")
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(2)))
            .andExpect(jsonPath("$.messages.name.*", hasItem("Username is required.")))
            .andExpect(jsonPath("$.messages.pass.*", hasItem("Password is required.")));
  }

  @Test
  @Order(7)
  public void usernameAndPasswordNull() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(2)))
            .andExpect(jsonPath("$.messages.name.*", hasItem("Username is required.")))
            .andExpect(jsonPath("$.messages.pass.*", hasItem("Password is required.")));
  }

  @Test
  @Order(8)
  public void usernameAndPasswordEmpty() throws Exception {
    UserLoginRequestDTO user = new UserLoginRequestDTO();
    user.setName("");
    user.setPass("");

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(2)))
            .andExpect(jsonPath("$.messages.name.*", hasItem("Username field is empty.")))
            .andExpect(jsonPath("$.messages.pass.*", hasItem("Password field is empty.")));
  }

  @Test
  @Order(9)
  public void usernameNull() throws Exception {
    assertFieldErrorMsg((String) null, "Username is required.", namePreparer);
  }

  @Test
  @Order(10)
  public void usernameEmpty() throws Exception {
    assertFieldErrorMsg("", "Username field is empty.", namePreparer);
  }

  @Test
  @Order(11)
  public void passwordNull() throws Exception {
    assertFieldErrorMsg((String) null, "Password is required.", passPreparer);
  }

  @Test
  @Order(12)
  public void passwordEmpty() throws Exception {
    assertFieldErrorMsg("", "Password field is empty.", passPreparer);
  }

  @Test
  @Order(13)
  public void passwordShort() throws Exception {
    assertFieldErrorMsg("1234567", "Username or password is wrong.", passPreparer);
  }

  @Test
  @Order(14)
  public void usernameAreSpaces() throws Exception {
    assertFieldErrorMsg("     ", "Username field is empty.", namePreparer);
  }

  @Test
  @Order(15)
  public void passwordAreSpaces() throws Exception {
    assertFieldErrorMsg("     ", "Password field is empty.", passPreparer);
  }

  @Test
  @Order(16)
  public void usernameBeginWithSpace() throws Exception {
    assertFieldErrorMsg(" comrade_Testovic", "Whitespaces not allowed anywhere in username.", namePreparer);
  }

  @Test
  @Order(17)
  public void usernameWithSpace() throws Exception {
    assertFieldErrorMsg("comrade Testovic", "Whitespaces not allowed anywhere in username.", namePreparer);
  }

  @Test
  @Order(18)
  public void usernameEndsWithSpace() throws Exception {
    assertFieldErrorMsg("comrade_Testovic ", "Whitespaces not allowed anywhere in username.", namePreparer);
  }

  @Test
  @Order(19)
  public void userNotFound() throws Exception {
    assertFormErrorMsg("comrade_nonexistent", "Username or password is wrong.", namePreparer, 401);
  }

  @Test
  @Order(20)
  public void usersPasswordWrong() throws Exception {
    loadUserToDB();
    assertFormErrorMsg("neviemNepametam", "Username or password is wrong.", passPreparer, 401);
    destroyUsersInDB();
  }

  @Test
  @Order(21)
  public void userLoginOK() throws Exception {
    loadUserToDB();

    UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();
    userLoginRequestDTO.setName(namePreparer.getDto().getName());
    userLoginRequestDTO.setPass(namePreparer.getDto().getPass());

    Mockito.when(timeService.getTime()).thenReturn(1234567890L);
    String jwtToken = jwtService.generate(user);

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(userLoginRequestDTO))
            .run()
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.jwtToken", is(jwtToken)));

    destroyUsersInDB();
  }

  private void loadUserToDB() {
    user = modelMapper.map(namePreparer.getDto(), User.class);
    user.setPasword(bCryptPasswordEncoder.encode( namePreparer.getDto().getPass() ));
    userRepository.save(user);
  }

  private void destroyUsersInDB() {
    userRepository.deleteAll();
  }

}
