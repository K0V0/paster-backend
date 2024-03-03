package com.kovospace.paster.item.v2.integration.v1compatibility;

import com.kovospace.paster.item.dtos.v2.FileItemInitiateRequestDTO;
import com.kovospace.paster.item.dtos.v2.FileItemUploadChunkRequestDTO;
import com.kovospace.paster.item.models.Item;
import org.junit.jupiter.api.Order;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FileItemControllerGetV1CompatibilityTest extends FileItemControllerV1CompatibilityTest {

    //TODO pre testy vytvarat zlozky ak niesu inak to jebne
    // pre testy spustene z idey musia byt v priecinku modulu (nie uplny root projektu)

    @org.junit.jupiter.api.Test
    @Order(1)
    @DirtiesContext
    public void optimisticTest() throws Exception {
        Item item = fileInitiateTest();
        item = fileUploadTest(item);

        assertNotNull(item);

        itemGetV1Test(200)
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("./testUploads/files/1-1-1.txt")))
                .andExpect(jsonPath("$.preview", is("./testUploads/files/1-1-1.txt")))
                .andExpect(jsonPath("$.timestamp").exists())
                //TODO false hodnoty nevyrenderuje podobne ako null ?? pozret vlastnosti springu
                //.andExpect(jsonPath("$.isLarge", is(false)))
                .andExpect(jsonPath("$.platform", is("UNKNOWN")))
                .andExpect(jsonPath("$.deviceName").doesNotExist());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    @DirtiesContext
    public void onlyInitiatedTest() throws Exception {
        fileInitiateTest();
        itemGetV1Test(404);
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    @DirtiesContext
    public void onlyOneChunkUploadedTest() throws Exception {

        /** initiate */

        MultipartFile mockMultipartFile = prepareMockMultipartFile();
        FileItemInitiateRequestDTO itemInit = new FileItemInitiateRequestDTO();
        itemInit.setOriginalFileName("filename.txt");
        itemInit.setMimeType("text/plain");
        itemInit.setChunkSize(4L);
        itemInit.setChunksCount(2L);

        itemPostTest(itemInit, HttpStatus.ACCEPTED.value()); // 202

        testTempFileIsPresent();
        testFinalFileNotPresent();

        /** first chunk upload */

        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(Arrays.copyOfRange(mockMultipartFile.getBytes(), 0, 4));
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(0L);

        itemPutTest(item, HttpStatus.ACCEPTED.value()); // 202

        testTempFileIsPresent();
        testTempFileContent("AAAA");
        testFinalFileNotPresent();

        /** verify */

        itemGetV1Test(404);
    }

}
