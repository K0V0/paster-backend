package com.kovospace.paster.user.integration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerGetProfileTest extends UserControllerProfileTest {

    @Test
    @Order(1)
    public void missingJwtToken() throws Exception {
        getRequest("/1")
                .run()
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.message", is("Missing Authentication header.")));
    }

    @Test
    @Order(2)
    public void requestParameterWrong() throws Exception {
        getRequest("/abc")
                .withJwtToken(super.jwtToken)
                .run()
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", is("Request parameters wrong or missing.")));
    }
}
