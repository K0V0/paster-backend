package com.kovospace.paster.item.v2.integration;

import com.kovospace.paster.item.models.Item;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileItemControllerGetTest extends FileItemControllerTest {

    @Test
    @Order(1)
    @DirtiesContext
    public void getItemTest() throws Exception {
        Item item = fileInitiateTest();
        item = fileUploadTest(item);

        assertNotNull(item);

        itemGetTest()
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.dataPreview").doesNotExist())
                //TODO opravit toto chovanie v dalsej verzii api preboha
                .andExpect(jsonPath("$.deviceName").doesNotExist())
                .andExpect(jsonPath("$.platform", is("UNKNOWN")))
                .andExpect(jsonPath("$.data", is("./testUploads/files/1-1-1.txt")))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void directFileRequestForbiddenTest() throws Exception {
        Item item = fileInitiateTest();
        item = fileUploadTest(item);

        assertNotNull(item);

        request(HttpMethod.GET, 0, "https://0.0.0.0:4004/testUploads/files/1-1-1.txt")
                .withJwtToken(this.token)
                .run()
                .andExpect(status().is(404)); // OK, mimo endpointov by vsetko malo byt 404
                //.andExpect(jsonPath("$.message.length()", is(1)))
                //.andExpect(jsonPath("$.code", is("general.endpoint.missing")))
                //TODO co je s tymi chybovymi hlaskami v integracnych testoch DOPICE
                //  ^^^ doimplementovat vsade custom exceptions a odchytavanie v controllerAdvices
    }

    @Test
    @Order(3)
    @DirtiesContext
    public void requestCompletedFileTest() throws Exception {
        Item item = fileInitiateTest();
        item = fileUploadTest(item);

        assertNotNull(item);

        itemGetTest()
                .andExpect(jsonPath("$.data", is("./testUploads/files/1-1-1.txt")))
                .andExpect(jsonPath("$.file", is(true)));

        getRequest(1)
                .withJwtToken(this.token)
                .run()
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.filename", is("1-1-1.txt")))
                .andExpect(jsonPath("$.originalFilename", is("filename.txt")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @Order(4)
    @DirtiesContext
    public void requestUncompleteFileTest() throws Exception {
        Item item = fileInitiateTest();

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));

        getRequest(1)
                .withJwtToken(this.token)
                .run()
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.code", is("item.filerepo.file.completion")));
    }

    @Test
    @Order(5)
    @DirtiesContext
    public void requestNonExistentItemTest() throws Exception {
        testTempFileNotPresent();
        testFinalFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value())
                .andExpect(jsonPath("$.code", is("item.response.missing")));

        getRequest(1)
                .withJwtToken(this.token)
                .run()
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.code", is("item.response.missing")));
    }

    //TODO test kedy subor existuje v databaze ako skompletovany ale "stratil" sa

}
