package com.kovospace.paster.item.integration;

import com.kovospace.paster.item.dtos.ItemRequestDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO testy na problemy s jwtTokenom
public class ItemControllerAddTest extends ItemControllerTest {

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

            .andExpect(jsonPath("$.messages.text[0].length()", is(3)))
            .andExpect(jsonPath("$.messages.text[0].status", is("error")))
            .andExpect(jsonPath("$.messages.text[0].code", is("item.request.missing")))
            .andExpect(jsonPath("$.messages.text[0].message", is("Item not presented.")));
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
            .andExpect(jsonPath("$.messages.text[0].length()", is(3)))
            .andExpect(jsonPath("$.messages.text[0].status", is("error")))
            .andExpect(jsonPath("$.messages.text[0].code", is("item.request.missing")))
            .andExpect(jsonPath("$.messages.text[0].message", is("Item not presented.")));
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
            .andExpect(jsonPath("$.messages.text[0].length()", is(3)))
            .andExpect(jsonPath("$.messages.text[0].status", is("error")))
            .andExpect(jsonPath("$.messages.text[0].code", is("item.request.empty")))
            .andExpect(jsonPath("$.messages.text[0].message", is("Nothing pasted.")));
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
            .andExpect(jsonPath("$.messages.text[0].length()", is(3)))
            .andExpect(jsonPath("$.messages.text[0].status", is("error")))
            .andExpect(jsonPath("$.messages.text[0].code", is("item.request.maxsize.reached")))
            .andExpect(jsonPath("$.messages.text[0].message", is("Maximum allowed size exceeded.")));
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
    itemDbSaveTest();
    //TODO unable to run - freezes
    /*itemGetTests()
            .andExpect(jsonPath("$.text", is("test")));*/
  }

  @Test
  @Order(11)
  @DirtiesContext
  public void platformNotSet() throws Exception {
    ItemRequestDTO item = new ItemRequestDTO();
    item.setText("test");

    itemPostTest(item, 201);

    itemGetTest()
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
            .andExpect(jsonPath("$.messages.platform[0].length()", is(3)))
            .andExpect(jsonPath("$.messages.platform[0].status", is("error")))
            .andExpect(jsonPath("$.messages.platform[0].code", is("item.request.platform.wrong")))
            .andExpect(jsonPath("$.messages.platform[0].message", is("Wrong platform type passed.")));

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
    itemGetTest()
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
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("API key is missing.")));
  }

  @Test
  @Order(15)
  public void apiKeyEmpty() throws Exception {
    postRequest()
            .withApiKey("")
            .run()
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message", is("API key is missing.")));
  }

  @Test
  @Order(16)
  public void apiKeyWrong() throws Exception {
    postRequest()
            .withApiKey("wrongApiKey")
            .run()
            .andExpect(status().is(403))
            .andExpect(jsonPath("$.message", is("API key is invalid.")));
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
    itemGetTest()
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
    itemGetTest()
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
    itemGetTest()
            .andExpect(jsonPath("$.text", is("test")))
            .andExpect(jsonPath("$.deviceName", is("")));
  }

}
