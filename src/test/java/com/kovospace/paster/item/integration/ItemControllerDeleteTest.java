package com.kovospace.paster.item.integration;

import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.user.models.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        itemDeleteTest(1)
                .andExpect(status().is(200));
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void deleteAlreadyDeletedUserItem() throws Exception {
        String tst = "test text";
        ItemRequestDTO item = new ItemRequestDTO();
        item.setText(tst);

        itemPostTest(item, 201);
        itemDbSaveTest();
        itemGetTest()
                .andExpect(jsonPath("$.text", is("test text")));

        itemRepository.deleteAll();

        itemDeleteTest(1)
                .andExpect(status().is(204));
    }

    @Test
    @Order(3)
    @DirtiesContext
    public void tryToDeleteNotOwningItem() throws Exception {
        String tst = "test text";
        ItemRequestDTO item = new ItemRequestDTO();
        item.setText(tst);

        itemPostTest(item, 201);
        itemDbSaveTest();
        itemGetTest()
                .andExpect(jsonPath("$.text", is("test text")));

        User victimUser = new User();
        victimUser.setName("Graphite Carbon");
        victimUser.setEmail("YouCannotSeeIt@becauseItsNot.there");
        victimUser.setPasword(bCryptPasswordEncoder.encode("C14"));
        userRepository.save(victimUser);
        victimUser.setJwtToken(jwtService.generate(victimUser));

        Item fraudedItem = new Item();
        fraudedItem.setUser(victimUser);
        fraudedItem.setText("not great not terrible");
        itemRepository.save(fraudedItem);

        assertEquals(2, itemRepository.findAll().size());

        itemDeleteTest(2)
                .andExpect(status().is(403));

        assertEquals(2, itemRepository.findAll().size());
    }

}
