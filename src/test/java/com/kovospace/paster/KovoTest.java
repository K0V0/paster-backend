package com.kovospace.paster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.models.ApiKey;
import com.kovospace.paster.base.repositories.ApiKeyRepository;
import com.kovospace.paster.base.services.StringsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${app.api-key-token}")
  protected String DUMMY_API_KEY;
  @Value("${app.api-key-header}")
  protected String API_KEY_HEADER;

  protected String API_PREFIX;
  protected String ENDPOINT;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  @Autowired
  protected StringsService stringsService;

  public KovoTest() {
    this.ENDPOINT = getEndpoint();
    this.API_PREFIX = getApiPrefix();
  }

  @BeforeEach
  protected void onConstruct() {
    ApiKey aKey = new ApiKey();
    aKey.setToken(DUMMY_API_KEY);
    apiKeyRepository.save(aKey);
    stringsService.setLocale("en");
  }

  @AfterEach
  protected void onDestruct() {
    apiKeyRepository.deleteAll();
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
    String field = dtoPreparer.getField();
    postRequest()
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(dtoPreparer.apply(input)))
            .run()
            .andExpect(status().is(httpStatus))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages." + field + ".length()", is(1)))
            .andExpect(jsonPath("$.messages." + field + "[0].status", is("error")))
            .andExpect(jsonPath("$.messages." + field + "[0].code", is(message)))
            .andExpect(jsonPath("$.messages." + field + "[0].message", is(stringsService.getTranslation(message))));
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
            .andExpect(jsonPath("$.message", is(stringsService.getTranslation(message))));
  }

  protected void assertFormErrorMsg(String input, String message, DtoPreparer dtoPreparer)
            throws Exception
  {
    assertFormErrorMsg(input, message, dtoPreparer, 400);
  }

  protected MockMvcSarcophagus postRequest() {
    mocks();
    return request(HttpMethod.POST, 0);
  }

  protected MockMvcSarcophagus getRequest() {
    mocks();
    return request(HttpMethod.GET, 0);
  }

  protected MockMvcSarcophagus getRequest(long id) {
    mocks();
    return request(HttpMethod.GET, id);
  }

  protected MockMvcSarcophagus deleteRequest(long itemId) {
    mocks();
    return request(HttpMethod.DELETE, itemId);
  }

  protected MockMvcSarcophagus patchRequest(long itemId) {
    mocks();
    return request(HttpMethod.PATCH, itemId);
  }

  private void mocks() {
    //Mockito.when(apiKeyService.isValid(any())).thenReturn(true);
  }

  protected MockMvcSarcophagus request(HttpMethod httpMethod, long id) {
    return request(httpMethod, id, null);
  }

  protected MockMvcSarcophagus request(HttpMethod httpMethod, long id, String url) {
    return new MockMvcSarcophagus(mockMvc)
            .withHttpMethod(httpMethod)
            .withUrl(getUrl(url, id))
            .withApiKey(DUMMY_API_KEY, API_KEY_HEADER);
  }

  private String getUrl(String url, long id) {
    if (url != null && url.trim().length() > 0) {
      return url;
    }
    return String.format("%s%s%s", API_PREFIX, ENDPOINT, id == 0 ? "" : String.format("/%s", id));
  }

}
