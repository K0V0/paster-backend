package com.kovospace.paster.item.v2.integration.v1compatibility;

import com.kovospace.paster.item.v2.integration.FileItemControllerTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class FileItemControllerV1CompatibilityTest extends FileItemControllerTest {

    private static final String API_V1_PREFIX = "/api/v1";
    private static final String API_V1_ENDPOINT = "/board/item";

    protected ResultActions itemGetV1Test(int expectedStatus) throws Exception {
        return getRequest()
                .withJwtToken(this.token)
                .withUrl(String.format("%s/%s/%s", API_V1_PREFIX, API_V1_ENDPOINT, 1))
                .run()
                .andExpect(status().is(expectedStatus));
    }
}
