package com.kovospace.paster.item.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO testy na problemy s jwtTokenom
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

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ItemRepository itemRepository;

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
  public void destruct() {
    itemRepository.deleteAll();
  }

  @Test
  @Order(1)
  public void unauthorizedRequestNotOK() throws Exception {
    postRequest()
            .run()
            .andExpect(status().is(401))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing Authentication header.")));
  }

  @Test
  @Order(2)
  public void requestBodyEmpty() throws Exception {
    postRequest()
            .withJwtToken(this.token)
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(3)
  public void requestBodyMalformed() throws Exception {
    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent("{kjhmbn}")
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
  }

  @Test
  @Order(4)
  public void requestBodyWrongMediaType() throws Exception {
    postRequest()
            .withJwtToken(this.token)
            .withMediaContent("{\"text\":\"bla bla bla\"}")
            .run()
            .andExpect(status().is(415));
            //.andExpect(jsonPath("$.message", is("Wrong request media type.")));
  }

  @Test
  @Order(5)
  public void requestJsonEmpty() throws Exception {
    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent("{}")
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages.text.*", hasItem("Item not presented.")));
  }

  @Test
  @Order(6)
  public void contentNull() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();

    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(item))
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages.text.*", hasItem("Item not presented.")));
  }

  @Test
  @Order(7)
  public void contentIsEmpty() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("");

    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(item))
            .run()
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

    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(item))
            .run()
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

    itemPostTest(item, 201);
  }

  @Test
  @Order(10)
  @DirtiesContext
  public void itemSaved() throws Exception {
    String tst = generate(() -> "a").limit(maxTextLength - 1).collect(joining());
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText(tst);

    itemPostTest(item, 201);

    /*itemDbSaveTest();

    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")));*/
  }

  @Test
  @Order(11)
  @DirtiesContext
  public void platformNotSet() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");

    itemPostTest(item, 201);

    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.platform", is("UNKNOWN")));
    ;
  }

  @Test
  @Order(12)
  @DirtiesContext
  public void platformWrong() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setPlatform("kokotina");

    postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(item))
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.messages.length()", is(1)))
            .andExpect(jsonPath("$.messages.platform.*", hasItem("Wrong platform type passed.")));

    assertNotNull(userRepository.findFirstByName(user.getName()));
    assertNotNull(itemRepository.findAllByUserOrderByCreatedAtDesc(user));
    assertTrue(itemRepository.findAllByUserOrderByCreatedAtDesc(user).isEmpty());
  }

  @Test
  @Order(13)
  @DirtiesContext
  public void platformOK() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setPlatform("webapp");

    itemPostTest(item, 201);
    itemDbSaveTest();
    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.platform", is("WEBAPP")));
  }

  //TODO preco kod 403
  @Test
  @Order(14)
  public void apiKeyNotIncluded() throws Exception {
    postRequest()
            .withApiKey(null)
            .run()
            .andExpect(status().is(403));
  }

  @Test
  @Order(15)
  public void apiKeyEmpty() throws Exception {
    postRequest()
            .withApiKey("")
            .run()
            .andExpect(status().is(401));
  }

  @Test
  @Order(16)
  public void apiKeyWrong() throws Exception {
    postRequest()
            .withApiKey("wrongApiKey")
            .run()
            .andExpect(status().is(401));
  }

  @Test
  @Order(17)
  @DirtiesContext
  public void deviceNameOK() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setDeviceName("dummyDevice");

    itemPostTest(item, 201);
    itemDbSaveTest();
    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.deviceName", is("dummyDevice")));
  }

  @Test
  @Order(18)
  @DirtiesContext
  public void deviceNameSetEmpty() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setDeviceName("");

    itemPostTest(item, 201);
    itemDbSaveTest();
    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.deviceName", is("")));
  }

  @Test
  @Order(19)
  @DirtiesContext
  public void deviceNameSetNull() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");
    item.setDeviceName("");

    itemPostTest(item, 201);
    itemDbSaveTest();
    itemGetTests()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.deviceName", is("")));
  }



  private ResultActions itemPostTest(ItemRequestDTO item, int expectedStatus) throws Exception {
    return postRequest()
            .withJwtToken(this.token)
            .withMediaType(MediaType.APPLICATION_JSON)
            .withMediaContent(objectMapper.writeValueAsBytes(item))
            .run()
            .andExpect(status().is(expectedStatus));
  }

  private Item itemDbSaveTest() {
    Item i = itemRepository.findAllByUserOrderByCreatedAtDesc(user).get(0);
    assertNotNull(i);
    return i;
  }

  private ResultActions itemGetTests() throws Exception {
    assertNotNull(userRepository.findFirstByName(user.getName()));
    assertNotNull(itemRepository.findAllByUserOrderByCreatedAtDesc(user));
    Item i = itemRepository.findAllByUserOrderByCreatedAtDesc(user).get(0);
    assertNotNull(i);
    //assertEquals(platformEnum, i.getPlatform());

    return getRequest()
            .withJwtToken(this.token)
            .withUrl(getApiPrefix() + "/board/item/1")
            .run()
            .andExpect(status().is(200));
  }

}
