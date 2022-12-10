package com.kovospace.paster.item.v2.integration;

import com.kovospace.paster.KovoTest;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.item.dtos.v2.ItemRequestDTO;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.repositories.ItemRepository;
import com.kovospace.paster.item.services.FilesystemOperationsService;
import com.kovospace.paster.user.models.User;
import com.kovospace.paster.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ItemControllerTest extends KovoTest {

    @Override
    protected String getEndpoint() {
        return "/board/item";
    }

    @Override
    protected String getApiPrefix() {
        return "/api/v2";
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

    @Autowired
    protected FilesystemOperationsService filesystemOperationsService;

    protected <IN_DTO extends ItemRequestDTO> ResultActions itemPostTest(IN_DTO item, int expectedStatus) throws Exception {
        return itemTest(item, expectedStatus, HttpMethod.POST);
    }

    protected <IN_DTO extends ItemRequestDTO> ResultActions itemPatchTest(IN_DTO item, int expectedStatus) throws Exception {
        return itemTest(item, expectedStatus, HttpMethod.PATCH);
    }

    protected <IN_DTO extends ItemRequestDTO> ResultActions itemPutTest(IN_DTO item, int expectedStatus) throws Exception {
        return itemTest(item, expectedStatus, HttpMethod.PUT);
    }

    protected <IN_DTO extends ItemRequestDTO> ResultActions itemTest(IN_DTO item, int expectedStatus, HttpMethod httpMethod) throws Exception {
        return request(httpMethod, 0)
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

    protected ResultActions itemGetTest(long itemId, int httpStatus) throws Exception {
        assertNotNull(userRepository.findFirstByName(user.getName()));
        assertNotNull(itemRepository.findAllByUserOrderByCreatedAtDesc(user));
        List<Item> items = itemRepository.findAllByUserOrderByCreatedAtDesc(user);
        assertNotNull(items);
        //Item i = items.stream().findFirst().orElse(null);
        //assertNotNull(i);

        return getRequest()
                .withJwtToken(this.token)
                .withUrl(String.format("%s/%s/%s", getApiPrefix(), "/board/item", itemId))
                .run()
                .andExpect(status().is(httpStatus));
    }

    protected ResultActions itemGetTest(int httpStatus) throws Exception {
        return itemGetTest(1, httpStatus);
    }

    protected ResultActions itemGetTest() throws Exception {
        return itemGetTest(1, HttpStatus.OK.value());
    }

    protected ResultActions itemDeleteTest(long itemId) throws Exception {
        return deleteRequest(itemId)
                .withUrl(String.format("%s/%s/%s", getApiPrefix(), "/board/item", itemId))
                .withJwtToken(this.token)
                .run();
    }

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
        filesystemOperationsService.deleteAll();
    }
}
