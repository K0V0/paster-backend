package com.kovospace.paster.item.integration;

import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.dtos.ItemsResponseDTO;
import com.kovospace.paster.item.models.Item;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ItemControllerGetAllTest extends ItemControllerTest {

    @Override
    protected String getEndpoint() { return "/board/items"; }

    @Order(1)
    @Test
    @DirtiesContext
    public void getAllItems() throws Exception {
        List<ItemRequestDTO> itemRequestDTOs = generateItems(10);
        /** test POSTing items */
        itemsPostTest(itemRequestDTOs, HttpStatus.CREATED.value());
        /** test items are saved in DB */
        List<Item> savedItems = itemsDbSaveTest();
        assertEquals(10, savedItems.size());
        /** test retrieving items */
        MockHttpServletResponse response = itemsGetTest().andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ItemsResponseDTO responseDTO = objectMapper.readValue(response.getContentAsString(), ItemsResponseDTO.class);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getItems());
        assertEquals(10, responseDTO.getItems().size());
        assertTrue(responseDTO.getItems().stream().allMatch(Objects::nonNull));
    }

    @Order(2)
    @Test
    @DirtiesContext
    /** testing previous test */
    public void wrongHttpStatus() throws Exception {
        List<ItemRequestDTO> itemRequestDTOs = generateItems(10);
        try {
            itemsPostTest(itemRequestDTOs, HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            assertNotNull(e);
            assertNotNull(e.getMessage());
            assertEquals("Response status expected:<204> but was:<201>", e.getMessage());
        }

    }

    @Order(3)
    @Test
    @DirtiesContext
    public void testOldItemsScraping() throws Exception {
        List<ItemRequestDTO> itemRequestDTOs = generateItems(21);
        /** test POSTing items */
        itemsPostTest(itemRequestDTOs, HttpStatus.CREATED.value());
        /** test items are saved in DB */
        List<Item> savedItems = itemsDbSaveTest();
        assertEquals(20, savedItems.size());
        /** test retrieving items */
        MockHttpServletResponse response = itemsGetTest().andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ItemsResponseDTO responseDTO = objectMapper.readValue(response.getContentAsString(), ItemsResponseDTO.class);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getItems());
        assertEquals(20, responseDTO.getItems().size());
        assertTrue(responseDTO.getItems().stream().allMatch(Objects::nonNull));
        assertEquals("text item 20", responseDTO.getItems().get(0).getText());
        assertEquals("text item 1", responseDTO.getItems().get(19).getText());
    }

    private List<ItemRequestDTO> generateItems(int itemsCount) {
        return IntStream.range(0, itemsCount)
                .mapToObj(i -> {
                    ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
                    itemRequestDTO.setText(String.format("text item %s", i));
                    return itemRequestDTO;
                })
                .collect(toList());
    }

    private boolean itemsPostTest(List<ItemRequestDTO> items, int expectedHttpStatus) {
        return items.stream()
                .allMatch(item -> getPostRequestStatusCode(item) == expectedHttpStatus);
    }

    private int getPostRequestStatusCode(ItemRequestDTO item) {
        try {
            return postRequest()
                    .withUrl(String.format("%s%s", API_PREFIX, super.getEndpoint()))
                    .withJwtToken(this.token)
                    .withMediaType(MediaType.APPLICATION_JSON)
                    .withMediaContent(objectMapper.writeValueAsBytes(item))
                    .run()
                    .andReturn()
                    .getResponse()
                    .getStatus();
        } catch (Exception e) {
            return -1;
        }
    }
}
