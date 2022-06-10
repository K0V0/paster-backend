package com.kovospace.paster.item.integration;

import com.kovospace.paster.item.dtos.ItemRequestDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ItemControllerDeleteTest extends ItemControllerTest {

    @Test
    @Order(1)
    @DirtiesContext
    public void deleteUserItem() throws Exception {
        String tst = "test text";
        ItemRequestDTO item = new ItemRequestDTO();
        item.setText(tst);

        itemPostTest(item, 201);
        itemDbSaveTest();
        itemGetTest()
            .andExpect(jsonPath("$.text", is("test text")));
        itemDeleteTest(1);
    }

}
