package com.kovospace.paster.item.v2.integration;

import com.kovospace.paster.item.dtos.v2.FileItemInitiateRequestDTO;
import com.kovospace.paster.item.dtos.v2.FileItemUploadChunkRequestDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FileItemControllerContinueTest extends FileItemControllerTest {

    @Test
    @Order(1)
    @DirtiesContext
    public void resumeUploadTest() throws Exception {
        MultipartFile mockMultipartFile = prepareMockMultipartFile();
        String tempFilePath = String.format("%s/%s", FILE_TEMP_DIR, "1-1-1");
        String finalFilePath = String.format("%s/%s", FILE_UPLOAD_DIR, "1-1-1.txt");

        /** initiate - new file */
        FileItemInitiateRequestDTO itemInit = new FileItemInitiateRequestDTO();
        itemInit.setOriginalFileName("filename.txt");
        itemInit.setMimeType("text/plain");
        itemInit.setChunkSize(2L);
        itemInit.setChunksCount(4L);

        itemPostTest(itemInit, HttpStatus.ACCEPTED.value())
                .andExpect(jsonPath("$.chunksCount", is(4)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist());

        testTempFileIsPresent();
        testTempFileContent(null);
        testFinalFileNotPresent();

        /** upload first part */
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(Arrays.copyOfRange(mockMultipartFile.getBytes(), 0, 2));
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(0L);

        itemPutTest(item, HttpStatus.ACCEPTED.value())
                .andExpect(jsonPath("$.chunksCount", is(4)))
                .andExpect(jsonPath("$.chunkNumber", is(0)));

        testTempFileIsPresent();
        testTempFileContent("AA");
        testFinalFileNotPresent();

        /** initiate - continue */
        FileItemInitiateRequestDTO itemReInit = new FileItemInitiateRequestDTO();
        itemReInit.setOriginalFileName("filename.txt");
        itemReInit.setMimeType("text/plain");
        itemReInit.setFileId(1L);
        itemReInit.setItemId(1L);

        itemPostTest(itemReInit, HttpStatus.ACCEPTED.value())
                .andExpect(jsonPath("$.chunksCount", is(4)))
                .andExpect(jsonPath("$.chunkNumber", is(0)));

        /** upload second part */
        item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(Arrays.copyOfRange(mockMultipartFile.getBytes(), 2, 4));
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(1L);

        itemPutTest(item, HttpStatus.ACCEPTED.value())
                .andExpect(jsonPath("$.chunksCount", is(4)))
                .andExpect(jsonPath("$.chunkNumber", is(1)));

        testTempFileIsPresent();
        testTempFileContent("AAAA");
        testFinalFileNotPresent();

        /** initiate - continue */
        itemReInit = new FileItemInitiateRequestDTO();
        itemReInit.setOriginalFileName("filename.txt");
        itemReInit.setMimeType("text/plain");
        itemReInit.setFileId(1L);
        itemReInit.setItemId(1L);

        itemPostTest(itemReInit, HttpStatus.ACCEPTED.value())
                .andExpect(jsonPath("$.chunksCount", is(4)))
                .andExpect(jsonPath("$.chunkNumber", is(1)));

    }
}
