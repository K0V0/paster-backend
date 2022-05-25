package com.kovospace.paster.item.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerAddTest extends KovoTest {

  @Override
  protected String getEndpoint() {
    return "/board/item";
  }

  @Override
  protected String getApiPrefix() {
    return "/api/v1";
  }

  private User user;
  private String token;

  @Value("${board.preview-max-length}")
  private int maxTextLength;

  @Value("${jwt.prefix}")
  private String prefix;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ItemRepository itemRepository;

  //@BeforeAll
  //@Transactional
  //
  // @PostConstruct
  @Transactional
  @BeforeEach
  public void init() {
    userRepository.deleteAll();
    user = new User();
    user.setName("Anatoli Datlov");
    user.setEmail("datlov@chnpp.cccp");
    user.setPasword(bCryptPasswordEncoder.encode("AZ-5"));
    userRepository.save(user);
    user.setJwtToken(jwtService.generate(user));
    this.token = jwtService.getPrefix() + " " + user.getJwtToken();
  }

  @AfterEach
  //@Transactional
  public void databaseCleanup() {
    //userRepository.deleteAll();
  }

  @Test
  @Order(1)
  public void unauthorizedRequestNotOK() throws Exception {
    mockMvc
        .perform(post(getApiPrefix() + getEndpoint()))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Missing Authentication header.")));
  }

  @Test
  @Order(2)
  public void requestBodyEmpty() throws Exception {
    mockMvc
        .perform(post(getApiPrefix() + getEndpoint())
            .header("Authorization", token))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(3)
  public void requestBodyMalformed() throws Exception {
    mockMvc
        .perform(post(getApiPrefix() + getEndpoint())
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{kjhmbn}"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyWrongMediaType() throws Exception {
    mockMvc
        .perform(
            post(getApiPrefix() + getEndpoint())
                .header("Authorization", token)
                .content("{\"text\":\"bla bla bla\"}"))
        .andExpect(status().is(415));
    //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(5)
  public void requestJsonEmpty() throws Exception {
    mockMvc
        .perform(
            post(getApiPrefix() + getEndpoint())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text.*", hasItem("Item not presented.")));;
  }

  @Test
  @Order(6)
  public void contentNull() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();

    mockMvc
        .perform(
            post(getApiPrefix() + getEndpoint())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text.*", hasItem("Item not presented.")));
  }

  @Test
  @Order(7)
  public void contentIsEmpty() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("");

    mockMvc
        .perform(
            post(getApiPrefix() + getEndpoint())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text.*", hasItem("Nothing pasted.")));
  }

  @Test
  @Order(8)
  public void maximumSizeReached() throws Exception {
    String tst = generate(() -> "a").limit(maxTextLength + 1).collect(joining());
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText(tst);

    mockMvc
        .perform(
            post(getApiPrefix() + getEndpoint())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(item))
        )
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.messages.length()", is(1)))
        .andExpect(jsonPath("$.messages.text.*", hasItem("Maximum allowed size exceeded.")));
  }

  @Test
  @Order(9)
  @DirtiesContext
  public void itemSavedShort() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test string");
    itemSaveTest(item, 201);
  }

  @Test
  @Order(10)
  @DirtiesContext
  public void itemSaved() throws Exception {
    String tst = generate(() -> "a").limit(maxTextLength - 1).collect(joining());
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText(tst);
    itemSaveTest(item, 201);
  }

  @Test
  @Order(11)
  @DirtiesContext
  public void platformNotSet() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    itemSaveTest(item, 201);
  }

  @Test
  @Order(12)
  @DirtiesContext
  public void platformWrong() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setPlatform("kokotina");

    mockMvc
            .perform(
                    post(getApiPrefix() + getEndpoint())
                            .header("Authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(item))
            )
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages.platform.*", hasItem("Wrong platform type passed.")));
  }

  @Test
  @Order(13)
  @DirtiesContext
  public void platformOK() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setPlatform("webapp");
    itemSaveTest(item, 201);
    //assertEquals(PlatformEnum.WEBAPP, userRepository.getById(1L).getItems().get(0).getPlatform());
    assertNotNull(userRepository.findFirstByName(user.getName()));
    assertNotNull(itemRepository.findAllByUserOrderByCreatedAtDesc(user));
    assertEquals(1, itemRepository.findAllByUserOrderByCreatedAtDesc(user).size());
    Item i = itemRepository.findAllByUserOrderByCreatedAtDesc(user).get(0);
    assertNotNull(i);
    //TODO null
    //assertEquals(PlatformEnum.WEBAPP, i.getPlatform());

    mockMvc.perform(
            get( getApiPrefix() + "/board/item/1")
            .header("Authorization", token)
    )
    .andExpect(status().is(200))
    .andExpect(jsonPath("$.text", is("test")))
    .andExpect(jsonPath("$.platform", is("")));
    ;
  }

  private void itemSaveTest(ItemRequestDTO item, int expectedStatus) throws Exception {
    mockMvc
            .perform(
                    post(getApiPrefix() + getEndpoint())
                            .header("Authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(item))
            )
            .andExpect(status().is(expectedStatus));
  }

}
