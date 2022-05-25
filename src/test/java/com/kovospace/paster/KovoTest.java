package com.kovospace.paster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.configurations.strings.Strings;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.capitalize;

@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
// database and persistence
//@DataJpaTest
//@ActiveProfiles("test")
@AutoConfigureTestDatabase/*(replace = AutoConfigureTestDatabase.Replace.NONE)*/
// because websockets
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

  protected interface DtoModifier<T> {
    void modIt(T obj);
  }

  protected abstract class DtoPreparer<K, T> implements Function<K, T> {
    protected T dto;
    protected String field;

    private DtoPreparer() {
      // TODO find out how to create instance here
    }

    public DtoPreparer(String field) {
      this();
      this.field = field;
    }

    public void modify(DtoModifier<T> dtoModifier) {
      dtoModifier.modIt(dto);
    }

    public T getDto() {
      return dto;
    }

    @Override
    public T apply(K s) {
      try {
        Class klazz = dto.getClass().getDeclaredField(field).getType();
        Method method = dto.getClass().getMethod("set" + capitalize(field), klazz);
        method.invoke(dto, s);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
      return dto;
    }

    public String getField() {
      return field;
    }
  }

  protected void assertFieldErrorMsg(Object input, String message, DtoPreparer dtoPreparer, int httpStatus)
            throws Exception
  {
    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(dtoPreparer.apply(input)))
            .run()
            .andExpect(status().is(httpStatus))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages." + dtoPreparer.getField() + ".*", hasItem(Strings.s(message))));
  }

  protected void assertFieldErrorMsg(String input, String message, DtoPreparer dtoPreparer)
            throws Exception
  {
    assertFieldErrorMsg(input, message, dtoPreparer, 400);
  }

  protected void assertFieldErrorMsg(Boolean input, String message, DtoPreparer dtoPreparer)
            throws Exception
  {
    assertFieldErrorMsg(input, message, dtoPreparer, 400);
  }

  protected void assertFormErrorMsg(String input, String message, DtoPreparer dtoPreparer, int httpStatus)
            throws Exception
  {
    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(dtoPreparer.apply(input)))
            .run()
            .andExpect(status().is(httpStatus))
            .andExpect(jsonPath("$.message", is(Strings.s(message))));
  }

  protected void assertFormErrorMsg(String input, String message, DtoPreparer dtoPreparer)
            throws Exception
  {
    assertFormErrorMsg(input, message, dtoPreparer, 400);
  }

  protected MockMvcSarcophagus postRequest() {
    return new MockMvcSarcophagus(mockMvc)
            .withHttpMethod(HttpMethod.POST)
            .withUrl(API_PREFIX + ENDPOINT)
            .withApiKey("dummyApiKey");
  }

  protected MockMvcSarcophagus getRequest() {
     return new MockMvcSarcophagus(mockMvc)
             .withHttpMethod(HttpMethod.GET)
             .withUrl(API_PREFIX + ENDPOINT)
             .withApiKey("dummyApiKey");
  }

}
