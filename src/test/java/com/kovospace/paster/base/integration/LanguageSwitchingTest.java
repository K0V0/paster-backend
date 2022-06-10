package com.kovospace.paster.base.integration;

import com.kovospace.paster.KovoTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LanguageSwitchingTest extends KovoTest {

    protected String getEndpoint() { return "/user/login"; }

    protected String getApiPrefix() { return "/api/v1"; }

    @Test
    @Order(1)
    public void requestWithoutHeader() throws Exception {
        postRequest().run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
    }

    @Test
    @Order(2)
    public void requestWithEmptyLang() throws Exception {
        postRequest()
                .withLanguage("")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
    }

    @Test
    @Order(3)
    public void requestWithDefaultLang() throws Exception {
        postRequest()
                .withLanguage("en")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
    }

    @Test
    @Order(4)
    public void requestWithSupportedNonPrimaryLang() throws Exception {
        postRequest()
                .withLanguage("sk")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Telo dotazu je poškodené alebo chýba.")));
    }

    @Test
    @Order(5)
    public void requestWithCompatibleToNonPrimaryLang() throws Exception {
        postRequest()
                .withLanguage("cz")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Telo dotazu je poškodené alebo chýba.")));
    }

    @Test
    @Order(6)
    public void requestWithCompatibleToPrimaryLang() throws Exception {
        postRequest()
                .withLanguage("us")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
    }

    @Test
    @Order(7)
    public void requestWithUnsupportedLang() throws Exception {
        postRequest()
                .withLanguage("xx")
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request body malformed or missing.")));
    }
}
