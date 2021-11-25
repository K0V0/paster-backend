package com.kovospace.paster;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.user.dtos.UserRegisterRequestDTO;
import java.util.function.Function;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
public abstract class KovoTest {

  protected String API_PREFIX;
  protected String ENDPOINT;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  public KovoTest() {
    this.ENDPOINT = getEndpoint();
    this.API_PREFIX = getApiPrefix();
  }

  protected abstract String getEndpoint();
  protected abstract String getApiPrefix();

  protected abstract class DtoPreparer implements Function<String, Object> {
    protected Object dto;
    public DtoPreparer() { this.dto = new Object(); }
    public abstract String getFieldName();
  }

  protected void assertFieldErrorMsg(
      String input, String message, DtoPreparer dtoPreparer) throws Exception {
    mockMvc
        .perform(
            post(API_PREFIX + ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dtoPreparer.apply(input)))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages." + dtoPreparer.getFieldName(), is(message)));
  }

}
