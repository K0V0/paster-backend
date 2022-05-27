package com.kovospace.paster.item.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.item.dtos.ItemRequestDTO;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ItemControllerTest extends KovoTest {

    @Override
    protected String getEndpoint() {
        return "/board/item";
    }

    @Override
    protected String getApiPrefix() {
        return "/api/v1";
    }

    protected User user;
    protected String token;

    @Value("${board.preview-max-length}")
    protected int maxTextLength;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected ItemRepository itemRepository;

    protected ResultActions itemPostTest(ItemRequestDTO item, int expectedStatus) throws Exception {
        return postRequest()
                .withJwtToken(this.token)
                .withMediaType(MediaType.APPLICATION_JSON)
                .withMediaContent(objectMapper.writeValueAsBytes(item))
                .run()
                .andExpect(status().is(expectedStatus));
    }

    protected Item itemDbSaveTest() {
        Item i = itemRepository.findAllByUserOrderByCreatedAtDesc(user).get(0);
        assertNotNull(i);
        return i;
    }

    protected ResultActions itemGetTests() throws Exception {
        assertNotNull(userRepository.findFirstByName(user.getName()));
        assertNotNull(itemRepository.findAllByUserOrderByCreatedAtDesc(user));
        Item i = itemRepository.findAllByUserOrderByCreatedAtDesc(user).get(0);
        assertNotNull(i);

        return getRequest()
                .withJwtToken(this.token)
                .withUrl(getApiPrefix() + "/board/item/1")
                .run()
                .andExpect(status().is(200));
    }
}
