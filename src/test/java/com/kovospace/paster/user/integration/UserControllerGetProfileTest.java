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
        getRequest()
                .run()
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.message", is("Missing Authentication header.")));
    }

    @Test
    @Order(2)
    public void wrongJwtToken() throws Exception {
        getRequest()
                .withJwtToken("Bearer bad.Jwt.Token")
                .run()
                .andExpect(status().is(403));
                //.andExpect(jsonPath("$.message", is("Missing Authentication header.")));
    }

    @Test
    @Order(3)
    public void missingApiKey() throws Exception {
        getRequest()
                .withJwtToken(super.jwtToken)
                .withApiKey("wrongApiKey")
                .run()
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.message", is("API key is invalid.")));
    }

    @Test
    @Order(4)
    public void getProfileOk() throws Exception {
        getRequest()
                .withJwtToken(super.jwtToken)
                .run()
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.email", is("datlov@chnpp.cccp")))
                .andExpect(jsonPath("$.avatarFileName", is("grafit.gif")))
                .andExpect(jsonPath("$.name", is("Anatoli Datlov")));
    }
}
