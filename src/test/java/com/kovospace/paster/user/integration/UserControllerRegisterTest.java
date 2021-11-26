package com.kovospace.paster.user.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerRegisterTest extends KovoTest {

  private class UserRegisterDtoPreparer extends DtoPreparer<UserRegisterRequestDTO> {
    public UserRegisterDtoPreparer(String field) {
      super(field);
      dto = new UserRegisterRequestDTO();
      dto.setName("Comrade_Testovic");
      dto.setPass("12345678");
      dto.setPass2("12345678");
      dto.setEmail("comrade.testovic@dym.bar");
    }
    @Override
    protected void modify(UserRegisterRequestDTO dto) {
      // doing nothing, just wasted to not inherit it into childrens
    }
  }

  private final UserRegisterDtoPreparer emailDtoPreparer = new UserRegisterDtoPreparer("email");
  private final UserRegisterDtoPreparer nameDtoPreparer = new UserRegisterDtoPreparer("name");

  @Override
  protected String getEndpoint() {
    return "/user/register";
  }

  @Override
  protected String getApiPrefix() {
    return "/api/v1";
  }

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
        .perform(post(API_PREFIX + "/user/register"))
        .andExpect(status().is(400));
  }

  @Test
  @Order(2)
  public void getRequestNotAllowed() throws Exception {
    mockMvc
        .perform(get(API_PREFIX + "/user/register"))
        .andExpect(status().is(405))
        .andExpect(jsonPath("$.message", is("Wrong HTTP method used.")));
  }

  @Test
  @Order(3)
  public void requestBodyEmpty() throws Exception {
    mockMvc
        .perform(post(API_PREFIX + "/user/register"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyMalformed() throws Exception {
    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
            post(API_PREFIX + "/user/register")
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
            post(API_PREFIX + "/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(4)))
        .andExpect(jsonPath("$.messages.name", is("Username is required.")))
        .andExpect(jsonPath("$.messages.pass", is("Password is required.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation is required.")))
        .andExpect(jsonPath("$.messages.email", is("E-mail is required.")));
  }

  @Test
  @Order(7)
  public void usernameAndPasswordsAndEmailNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(4)))
        .andExpect(jsonPath("$.messages.name", is("Username is required.")))
        .andExpect(jsonPath("$.messages.pass", is("Password is required.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation is required.")))
        .andExpect(jsonPath("$.messages.email", is("E-mail is required.")));
  }

  @Test
  @Order(8)
  public void usernameAndPasswordsAndEmailEmpty() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("");
    user.setPass("");
    user.setPass2("");
    user.setEmail("");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(4)))
        .andExpect(jsonPath("$.messages.name", is("Username field is empty.")))
        .andExpect(jsonPath("$.messages.pass", is("Password field is empty.")))
        .andExpect(jsonPath("$.messages.pass2", is("Password confirmation field is empty.")))
        .andExpect(jsonPath("$.messages.email", is("E-mail field is empty.")));
  }

  @Test
  @Order(9)
  public void usernameNull() throws Exception {
    assertFieldErrorMsg(null, "Username is required.", nameDtoPreparer);
  }

  @Test
  @Order(10)
  public void usernameEmpty() throws Exception {
    assertFieldErrorMsg("", "Username field is empty.", nameDtoPreparer);
  }

  @Test
  @Order(11)
  public void passwordNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    assertFieldErrorMsg("     ", "Username field is empty.", nameDtoPreparer);
  }

  @Test
  @Order(16)
  public void passwordIsJustSpaces() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("       ");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    assertFieldErrorMsg(" comrade_testovic", "Your username begins with space.", nameDtoPreparer);
  }

  @Test
  @Order(19)
  public void usernameWithSpace() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade testovic");
    user.setPass("12345678");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
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
    user.setEmail("comrade.testovic@dym.bar");

    User dbUser = new User();
    dbUser.setName("comrade_testovic");
    dbUser.setPasword(bCryptPasswordEncoder.encode("12345678"));
    userRepository.save(dbUser);

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(403))
        .andExpect(jsonPath("$.message", is("Username is already taken.")));

    userRepository.deleteAll();
  }

  @Test
  @Order(27)
  public void userCreatedTokenObtained() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");
    String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.Puh4y7UM2bdCuqyQOK-iyOwloMGPOskNwfjjKZ2jDQ8";

    Mockito.when(timeService.getTime()).thenReturn(1234567890L);

    mockMvc
        .perform(
            post(API_PREFIX + "/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user))
        )
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.jwtToken", is(jwtToken)));

    userRepository.deleteAll();
  }

  @Test
  @Order(28)
  public void userEmailNull() throws Exception {
    assertFieldErrorMsg(null, "E-mail is required.", emailDtoPreparer);
  }

  @Test
  @Order(29)
  public void userEmailEmpty() throws Exception {
    assertFieldErrorMsg("", "E-mail field is empty.", emailDtoPreparer);
  }

  @Test
  @Order(30)
  public void emailTests() throws Exception {
    assertFieldErrorMsg("hello", "E-mail address is not valid.", emailDtoPreparer);
    /*assertEmailError("hello", "E-mail address is not valid.");
    assertEmailError("hello@", "E-mail address is not valid.");
    assertEmailError("hello@world", "E-mail address is not valid.");
    assertEmailError("hello@world.", "E-mail address is not valid.");
    assertEmailError(".hello@world.net", "E-mail address is not valid.");
    assertEmailError("hello.@world.net", "E-mail address is not valid.");
    assertEmailError("he..llo@world.net", "E-mail address is not valid.");
    assertEmailError("hello!+2020@example.com", "E-mail address is not valid.");
    assertEmailError("hello@example.a", "E-mail address is not valid.");
    assertEmailError("hello@example..com", "E-mail address is not valid.");
    assertEmailError("hello@.com", "E-mail address is not valid.");
    assertEmailError("hello@.example.", "E-mail address is not valid.");
    assertEmailError("hello@-example.com", "E-mail address is not valid.");
    assertEmailError("hello@example.com-", "E-mail address is not valid.");
    assertEmailError("hello@example_example.com", "E-mail address is not valid.");
    assertEmailError("1234567890123456789012345678901234567890123456789012345678901234xx@example.com", "E-mail address is not valid.");*/
  }

  //TODO testy pre e-mail adresy
  //TODO refaktor DRY

}
