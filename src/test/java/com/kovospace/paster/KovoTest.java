package com.kovospace.paster;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.capitalize;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  protected abstract class DtoPreparer<T> implements Function<String, T> {
    protected T dto;
    protected String field;

    private DtoPreparer() {
      // TODO find out how to create instance here
    }

    public DtoPreparer(String field) {
      this();
      this.field = field;
    }

    protected abstract void modify(T dto);

    @Override
    public T apply(String s) {
      modify(dto);
      try {
        Method method = dto.getClass().getMethod("set" + capitalize(field), String.class);
        method.invoke(dto, s);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
      }
      return dto;
    }

    public String getField() {
      return field;
    }
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
        .andExpect(jsonPath("$.messages." + dtoPreparer.getField(), is(message)));
  }

}
