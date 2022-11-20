package com.kovospace.paster.item.v1.integration;

import com.kovospace.paster.item.dtos.v1.ItemRequestDTO;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.user.models.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TextItemControllerDeleteTest extends ItemControllerTest {

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

    @Test
    @Order(4)
    @DirtiesContext
    public void deleteOldItemsAfter20() throws Exception {
        for (int i = 0; i < 21; i++) {
            ItemRequestDTO item = new ItemRequestDTO();
            item.setText("test string " + (i+1));

            itemPostTest(item, 201);
        }

        assertEquals(20, itemRepository.findAll().size());
        assertEquals("test string 2", itemRepository
                .findAllByUserOrderByCreatedAtDesc(userRepository.findAll().get(0))
                .get(19).getText());
        assertEquals("test string 21", itemRepository
                .findAllByUserOrderByCreatedAtDesc(userRepository.findAll().get(0))
                .get(0).getText());
    }

}
