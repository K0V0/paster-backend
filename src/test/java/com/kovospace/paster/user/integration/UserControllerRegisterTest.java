package com.kovospace.paster.user.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerRegisterTest extends KovoTest {

  private class UserRegisterDtoPreparer<T> extends DtoPreparer<T, UserRegisterRequestDTO> {
    public UserRegisterDtoPreparer(String field) {
      super(field);
      dto = new UserRegisterRequestDTO();
      dto.setName("Comrade_Testovic");
      dto.setPass("12345678");
      dto.setPass2("12345678");
      dto.setEmail("comrade.testovic@dym.bar");
      dto.setGdpr(true);
    }
  }

  private final UserRegisterDtoPreparer emailDtoPreparer = new UserRegisterDtoPreparer<String>("email");
  private final UserRegisterDtoPreparer nameDtoPreparer = new UserRegisterDtoPreparer<String>("name");
  private final UserRegisterDtoPreparer passDtoPreparer = new UserRegisterDtoPreparer<String>("pass");
  private final UserRegisterDtoPreparer pass2DtoPreparer = new UserRegisterDtoPreparer<String>("pass2");
  private final UserRegisterDtoPreparer gdprDtoPreparer = new UserRegisterDtoPreparer<Boolean>("gdpr");

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
    postRequest().run()
            .andExpect(status().is(400));
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
            .withMediaContent("{\"name\":\"comrade Testovic\",\"pass\":\"AZ-5\",\"pass2\":\"AZ-5\"}")
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
            .andExpect(jsonPath("$.messages.length()", is(5)))

            .andExpect(jsonPath("$.messages.name.length()", is(1)))
            .andExpect(jsonPath("$.messages.name[0].status", is("error")))
            .andExpect(jsonPath("$.messages.name[0].code", is("user.register.username.required")))
            .andExpect(jsonPath("$.messages.name[0].message", is("Username is required.")))

            .andExpect(jsonPath("$.messages.pass.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass[0].code", is("user.register.password.required")))
            .andExpect(jsonPath("$.messages.pass[0].message", is("Password is required.")))

            .andExpect(jsonPath("$.messages.pass2.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass2[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass2[0].code", is("user.register.passwordConfirmation.required")))
            .andExpect(jsonPath("$.messages.pass2[0].message", is("Password confirmation is required.")))

            .andExpect(jsonPath("$.messages.email.length()", is(1)))
            .andExpect(jsonPath("$.messages.email[0].status", is("error")))
            .andExpect(jsonPath("$.messages.email[0].code", is("user.register.email.required")))
            .andExpect(jsonPath("$.messages.email[0].message", is("E-mail is required.")))

            .andExpect(jsonPath("$.messages.gdpr.length()", is(1)))
            .andExpect(jsonPath("$.messages.gdpr[0].status", is("error")))
            .andExpect(jsonPath("$.messages.gdpr[0].code", is("user.register.gdpr.required")))
            .andExpect(jsonPath("$.messages.gdpr[0].message", is("GDPR accept field is required.")));
  }

  @Test
  @Order(7)
  public void usernameAndPasswordsAndEmailAndGdprNull() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(400))
            /*.andExpect(jsonPath("$.messages.length()", is(5)))
            .andExpect(jsonPath("$.messages.name.*", hasItem("Username is required.")))
            .andExpect(jsonPath("$.messages.pass.*", hasItem("Password is required.")))
            .andExpect(jsonPath("$.messages.pass2.*", hasItem("Password confirmation is required.")))
            .andExpect(jsonPath("$.messages.gdpr.*", hasItem("GDPR accept field is empty.")))
            .andExpect(jsonPath("$.messages.email.*", hasItem("E-mail is required.")));*/

            .andExpect(jsonPath("$.messages.name.length()", is(1)))
            .andExpect(jsonPath("$.messages.name[0].status", is("error")))
            .andExpect(jsonPath("$.messages.name[0].code", is("user.register.username.required")))
            .andExpect(jsonPath("$.messages.name[0].message", is("Username is required.")))

            .andExpect(jsonPath("$.messages.pass.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass[0].code", is("user.register.password.required")))
            .andExpect(jsonPath("$.messages.pass[0].message", is("Password is required.")))

            .andExpect(jsonPath("$.messages.pass2.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass2[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass2[0].code", is("user.register.passwordConfirmation.required")))
            .andExpect(jsonPath("$.messages.pass2[0].message", is("Password confirmation is required.")))

            .andExpect(jsonPath("$.messages.email.length()", is(1)))
            .andExpect(jsonPath("$.messages.email[0].status", is("error")))
            .andExpect(jsonPath("$.messages.email[0].code", is("user.register.email.required")))
            .andExpect(jsonPath("$.messages.email[0].message", is("E-mail is required.")))

            .andExpect(jsonPath("$.messages.gdpr.length()", is(1)))
            .andExpect(jsonPath("$.messages.gdpr[0].status", is("error")))
            .andExpect(jsonPath("$.messages.gdpr[0].code", is("user.register.gdpr.required")))
            .andExpect(jsonPath("$.messages.gdpr[0].message", is("GDPR accept field is required.")));
  }

  @Test
  @Order(8)
  public void usernameAndPasswordsAndEmailEmptyAndGdprFalse() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("");
    user.setPass("");
    user.setPass2("");
    user.setEmail("");
    user.setGdpr(false);

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(5)))

            .andExpect(jsonPath("$.messages.name.length()", is(1)))
            .andExpect(jsonPath("$.messages.name[0].status", is("error")))
            .andExpect(jsonPath("$.messages.name[0].code", is("user.register.username.empty")))
            .andExpect(jsonPath("$.messages.name[0].message", is("Username field is empty.")))

            .andExpect(jsonPath("$.messages.pass.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass[0].code", is("user.register.password.empty")))
            .andExpect(jsonPath("$.messages.pass[0].message", is("Password field is empty.")))

            .andExpect(jsonPath("$.messages.pass2.length()", is(1)))
            .andExpect(jsonPath("$.messages.pass2[0].status", is("error")))
            .andExpect(jsonPath("$.messages.pass2[0].code", is("user.register.passwordConfirmation.empty")))
            .andExpect(jsonPath("$.messages.pass2[0].message", is("Password confirmation field is empty.")))

            .andExpect(jsonPath("$.messages.email.length()", is(1)))
            .andExpect(jsonPath("$.messages.email[0].status", is("error")))
            .andExpect(jsonPath("$.messages.email[0].code", is("user.register.email.empty")))
            .andExpect(jsonPath("$.messages.email[0].message", is("E-mail field is empty.")))

            .andExpect(jsonPath("$.messages.gdpr.length()", is(1)))
            .andExpect(jsonPath("$.messages.gdpr[0].status", is("error")))
            .andExpect(jsonPath("$.messages.gdpr[0].code", is("user.register.gdpr.denied")))
            .andExpect(jsonPath("$.messages.gdpr[0].message", is("GDPR consent must be accepted.")));
  }

  @Test
  @Order(9)
  public void usernameNull() throws Exception {
    assertFieldErrorMsg((String) null, "user.register.username.required", nameDtoPreparer);
  }

  @Test
  @Order(10)
  public void usernameEmpty() throws Exception {
    assertFieldErrorMsg("", "user.register.username.empty", nameDtoPreparer);
  }

  @Test
  @Order(11)
  public void passwordNull() throws Exception {
    assertFieldErrorMsg((String) null, "user.register.password.required", passDtoPreparer);
  }

  @Test
  @Order(12)
  public void passwordEmpty() throws Exception {
    assertFieldErrorMsg("", "user.register.password.empty", passDtoPreparer);
  }

  @Test
  @Order(13)
  public void passwordConfirmationNull() throws Exception {
    assertFieldErrorMsg((String) null, "user.register.passwordConfirmation.required", pass2DtoPreparer);
  }

  @Test
  @Order(14)
  public void passwordConfirmationEmpty() throws Exception {
    assertFieldErrorMsg("", "user.register.passwordConfirmation.empty", pass2DtoPreparer);
  }

  @Test
  @Order(15)
  public void usernameIsJustSpaces() throws Exception {
    assertFieldErrorMsg("     ", "user.register.username.empty", nameDtoPreparer);
  }

  @Test
  @Order(16)
  public void passwordIsJustSpaces() throws Exception {
    assertFieldErrorMsg("     ", "user.register.password.empty", passDtoPreparer);
  }

  @Test
  @Order(17)
  public void passwordConfirmationIsJustSpaces() throws Exception {
    assertFieldErrorMsg("     ", "user.register.passwordConfirmation.empty", pass2DtoPreparer);
  }

  @Test
  @Order(18)
  public void usernameStartOrEndsWithSpace() throws Exception {
    assertFieldErrorMsg(" comrade_testovic", "user.register.username.format.whitespaces.beginning.denied", nameDtoPreparer);
    assertFieldErrorMsg("  comrade_testovic", "user.register.username.format.whitespaces.beginning.denied", nameDtoPreparer);
    assertFieldErrorMsg("comrade_testovic ", "user.register.username.format.whitespaces.end.denied", nameDtoPreparer);
    assertFieldErrorMsg("comrade_testovic  ", "user.register.username.format.whitespaces.end.denied", nameDtoPreparer);
    assertFieldErrorMsg(" comrade_testovic ", "user.register.username.format.whitespaces.around.denied", nameDtoPreparer);
  }

  @Test
  @Order(19)
  public void usernameWithOnlySpaces() throws Exception {
    assertFieldErrorMsg("   ", "user.register.username.empty", nameDtoPreparer);
  }

  @Test
  @Order(20)
  public void usernameWithSpace() throws Exception {
    assertFieldErrorMsg("comrade testovic", "user.register.username.format.whitespaces.denied", nameDtoPreparer);
  }

  @Test
  @Order(21)
  public void usernameContainsNotAllowedChars() throws Exception {
    assertFieldErrorMsg("com#rade_te/sto?vic", "user.register.username.format.characters.denied", nameDtoPreparer);
  }

  @Test
  @Order(22)
  public void passwordShort() throws Exception {
    assertFieldErrorMsg("1234567", "user.register.password.format.length.short", passDtoPreparer);
  }

  @Test
  @Order(23)
  public void passwordStartsOrEndsWithSpace() throws Exception {
    assertFieldErrorMsg(" 1234567", "user.register.password.format.whitespaces.around.denied", passDtoPreparer);
    assertFieldErrorMsg("  1234567", "user.register.password.format.whitespaces.around.denied", passDtoPreparer);
    assertFieldErrorMsg("1234567 ", "user.register.password.format.whitespaces.around.denied", passDtoPreparer);
    assertFieldErrorMsg("1234567  ", "user.register.password.format.whitespaces.around.denied", passDtoPreparer);
    assertFieldErrorMsg(" 1234567 ", "user.register.password.format.whitespaces.around.denied", passDtoPreparer);
  }

  @Test
  @Order(24)
  public void passwordAndConfirmationDidNotMatch() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345679");
    user.setEmail("comrade.testovic@dym.bar");
    user.setGdpr(true);

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(409))
            .andExpect(jsonPath("$.code", is("user.register.passwordConfirmation.nomatch")));
  }

  @Test
  @Order(25)
  public void usernameAlreadyTaken() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");
    user.setGdpr(true);

    User dbUser = new User();
    dbUser.setName("comrade_testovic");
    dbUser.setPasword(bCryptPasswordEncoder.encode("12345678"));
    userRepository.save(dbUser);

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(403))
            .andExpect(jsonPath("$.code", is("user.register.username.taken")))
            .andExpect(jsonPath("$.message", is("Username is already taken.")));

    userRepository.deleteAll();
  }

  //TODO pada v prode, prejde kazdi treti krat
  /*@Test
  @Order(26)
  public void userCreatedTokenObtained() throws Exception {
    UserRegisterRequestDTO user = new UserRegisterRequestDTO();
    user.setName("comrade_testovic");
    user.setPass("12345678");
    user.setPass2("12345678");
    user.setEmail("comrade.testovic@dym.bar");
    user.setGdpr(true);
    String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.Puh4y7UM2bdCuqyQOK-iyOwloMGPOskNwfjjKZ2jDQ8";

    Mockito.when(timeService.getTime()).thenReturn(1234567890L);

    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(user))
            .run()
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.jwtToken", is(jwtToken)));

    userRepository.deleteAll();
  }*/

  @Test
  @Order(27)
  public void userEmailNull() throws Exception {
    assertFieldErrorMsg((String) null, "user.register.email.required", emailDtoPreparer);
  }

  @Test
  @Order(28)
  public void userEmailEmpty() throws Exception {
    assertFieldErrorMsg("", "user.register.email.empty", emailDtoPreparer);
  }

  @Test
  @Order(29)
  public void emailTests() throws Exception {
    assertFieldErrorMsg("hello", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@world", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg(".hello@world.net", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello.@world.net", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("he..llo@world.net", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello!+2020@example.com", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@example.a", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@example..com", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@.com", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@.example.", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@-example.com", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@example.com-", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("hello@example_example.com", "user.register.email.format.wrong", emailDtoPreparer);
    assertFieldErrorMsg("1234567890123456789012345678901234567890123456789012345678901234xx@example.com",
        "user.register.email.format.wrong", emailDtoPreparer);
  }

  @Test
  @Order(30)
  public void gdprIsNull() throws Exception {
    assertFieldErrorMsg((Boolean) null, "user.register.gdpr.required", gdprDtoPreparer);
  }

  @Test
  @Order(31)
  public void gdprNotAccepted() throws Exception {
    assertFieldErrorMsg(false, "user.register.gdpr.denied", gdprDtoPreparer);
  }

}
