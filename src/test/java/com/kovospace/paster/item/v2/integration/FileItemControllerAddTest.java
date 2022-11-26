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

public class FileItemControllerAddTest extends FileItemControllerTest {

    @Test
    @Order(1)
    @DirtiesContext
    public void initiateUploadTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");

        itemPostTest(item, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.fileName", is("1-1-1")))
                .andExpect(jsonPath("$.originalFileName", is("filename.txt")))
                .andExpect(jsonPath("$.chunksCount", is(1)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist())
                .andExpect(jsonPath("$.chunkSize", is(524288)))
                .andExpect(jsonPath("$.mimeType", is("text/plain")))
                .andExpect(jsonPath("$.extension", is("txt")))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.fileId", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.dataPreview").doesNotExist())
                //TODO opravit toto chovanie v dalsej verzii api preboha
                .andExpect(jsonPath("$.deviceName", is("")))
                .andExpect(jsonPath("$.platform", is("UNKNOWN")))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void initiateUploadFileExtensionMissingTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename");
        item.setMimeType("text/plain");

        itemPostTest(item, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.fileName", is("1-1-1")))
                .andExpect(jsonPath("$.originalFileName", is("filename")))
                .andExpect(jsonPath("$.chunksCount", is(1)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist())
                .andExpect(jsonPath("$.chunkSize", is(524288)))
                .andExpect(jsonPath("$.mimeType", is("text/plain")))
                .andExpect(jsonPath("$.extension").doesNotExist()) //NULL value
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.fileId", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest(HttpStatus.OK.value()) //200
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(3)
    @DirtiesContext
    public void initiateUploadChunksCountMissingTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setChunkSize(524288L);

        itemPostTest(item, HttpStatus.BAD_REQUEST.value()); // 400

        testTempFileNotPresent();
        testFinalFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value()); // 404
    }

    @Test
    @Order(4)
    @DirtiesContext
    public void initiateUploadChunkSizeMissingTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setChunksCount(1L);

        itemPostTest(item, HttpStatus.BAD_REQUEST.value()); // 400

        testTempFileNotPresent();
        testFinalFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value()); // 404
    }

    @Test
    @Order(5)
    @DirtiesContext
    public void initiateUploadCustomValuesTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setChunkSize(262144L);
        item.setChunksCount(2L);

        itemPostTest(item, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.chunkSize", is(262144)))
                .andExpect(jsonPath("$.chunksCount", is(2)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist());

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(6)
    @DirtiesContext
    public void initiateUploadFilenameMissingTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setMimeType("text/plain");

        itemPostTest(item, HttpStatus.BAD_REQUEST.value()) // 400
                .andExpect(jsonPath("$.messages.length()", is(1)));
                //.andExpect(jsonPath("$.messages.originalFileName[0].length", is(3)));
                //TODO co je s tymto testom ? v postMan vidno normalne

        testTempFileNotPresent();
        testFinalFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value()); // 404
    }

    @Test
    @Order(7)
    @DirtiesContext
    public void initiateUploadMimeTypeMissingTest() throws Exception {
        FileItemInitiateRequestDTO item = new FileItemInitiateRequestDTO();
        item.setOriginalFileName("filename.txt");

        itemPostTest(item, HttpStatus.BAD_REQUEST.value()) // 400
                .andExpect(jsonPath("$.messages.length()", is(1)));
                //.andExpect(jsonPath("$.messages.text[0].code", is("item.fileupload.initiation.mimetype.missing")));
                //TODO co je s tymto testom ? v postMan vidno normalne

        testTempFileNotPresent();
        testFinalFileNotPresent();

        itemGetTest( HttpStatus.NOT_FOUND.value()); // 404
    }

    @Test
    @Order(8)
    @DirtiesContext
    public void uploadOneChunkTest() throws Exception {

        /** initiate */
        FileItemInitiateRequestDTO itemInit = new FileItemInitiateRequestDTO();
        itemInit.setOriginalFileName("filename.txt");
        itemInit.setMimeType("text/plain");

        itemPostTest(itemInit, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.fileName", is("1-1-1")))
                .andExpect(jsonPath("$.originalFileName", is("filename.txt")))
                .andExpect(jsonPath("$.chunksCount", is(1)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist())
                .andExpect(jsonPath("$.chunkSize", is(524288)))
                .andExpect(jsonPath("$.mimeType", is("text/plain")))
                .andExpect(jsonPath("$.extension", is("txt")))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.fileId", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));

        /** upload */
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(prepareMockMultipartFile().getBytes());
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(0L);

        itemPutTest(item, HttpStatus.CREATED.value()); // 201

        testFinalFileIsPresent();
        testTempFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(9)
    @DirtiesContext
    public void uploadMoreChunksTest() throws Exception {

        /** initiate */

        MultipartFile mockMultipartFile = prepareMockMultipartFile();
        FileItemInitiateRequestDTO itemInit = new FileItemInitiateRequestDTO();
        itemInit.setOriginalFileName("filename.txt");
        itemInit.setMimeType("text/plain");
        itemInit.setChunkSize(4L);
        itemInit.setChunksCount(2L);

        itemPostTest(itemInit, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.fileName", is("1-1-1")))
                .andExpect(jsonPath("$.originalFileName", is("filename.txt")))
                .andExpect(jsonPath("$.chunksCount", is(2)))
                .andExpect(jsonPath("$.chunkNumber").doesNotExist())
                .andExpect(jsonPath("$.chunkSize", is(4)))
                .andExpect(jsonPath("$.mimeType", is("text/plain")))
                .andExpect(jsonPath("$.extension", is("txt")))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.fileId", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));

        /** first chunk */

        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(Arrays.copyOfRange(mockMultipartFile.getBytes(), 0, 4));
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(0L);

        itemPutTest(item, HttpStatus.ACCEPTED.value()) // 202
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.fileName", is("1-1-1")))
                .andExpect(jsonPath("$.originalFileName", is("filename.txt")))
                .andExpect(jsonPath("$.chunksCount", is(2)))
                .andExpect(jsonPath("$.chunkNumber", is(0)))
                .andExpect(jsonPath("$.chunkSize", is(4)))
                .andExpect(jsonPath("$.mimeType", is("text/plain")))
                .andExpect(jsonPath("$.extension", is("txt")))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.fileId", is(1)));

        testTempFileIsPresent();
        testTempFileContent("AAAA");
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));

        /** second chunk */

        item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename2.txt");
        item.setMimeType("text/plain");
        item.setFileContent(Arrays.copyOfRange(mockMultipartFile.getBytes(), 4, 8));
        item.setItemId(1L);
        item.setFileId(1L);
        item.setChunkNumber(1L);

        itemPutTest(item, HttpStatus.CREATED.value()) // 201
                .andExpect(jsonPath("$.chunkNumber", is(1)));

        testFinalFileIsPresent();
        testFinalFileContent("AAAABBBB");
        testTempFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(10)
    @DirtiesContext
    public void uploadChunkMissingFileNameTest() throws Exception {
        fileInitiateTest();
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setMimeType("text/plain");
        item.setFileContent(prepareMockMultipartFile().getBytes());
        item.setItemId(1L);
        item.setFileId(1L);

        itemPutTest(item, HttpStatus.BAD_REQUEST.value()); // 400
                //.andExpect(jsonPath("$.messages.length()", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(11)
    @DirtiesContext
    public void uploadChunkMissingMimeTypeTest() throws Exception {
        fileInitiateTest();
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setFileContent(prepareMockMultipartFile().getBytes());
        item.setItemId(1L);
        item.setFileId(1L);

        itemPutTest(item, HttpStatus.BAD_REQUEST.value()); // 400
                //.andExpect(jsonPath("$.messages.length()", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(12)
    @DirtiesContext
    public void uploadChunkMissingDataTest() throws Exception {
        fileInitiateTest();
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setItemId(1L);
        item.setFileId(1L);

        itemPutTest(item, HttpStatus.BAD_REQUEST.value()); // 400
                //.andExpect(jsonPath("$.messages.length()", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(13)
    @DirtiesContext
    public void uploadChunkMissingItemIDTest() throws Exception {
        fileInitiateTest();
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(prepareMockMultipartFile().getBytes());
        item.setFileId(1L);

        itemPutTest(item, HttpStatus.BAD_REQUEST.value()); // 400
                //.andExpect(jsonPath("$.messages.length()", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

    @Test
    @Order(14)
    @DirtiesContext
    public void uploadChunkMissingFileIDTest() throws Exception {
        fileInitiateTest();
        FileItemUploadChunkRequestDTO item = new FileItemUploadChunkRequestDTO();
        item.setOriginalFileName("filename.txt");
        item.setMimeType("text/plain");
        item.setFileContent(prepareMockMultipartFile().getBytes());
        item.setItemId(1L);

        itemPutTest(item, HttpStatus.BAD_REQUEST.value()); // 400
                //.andExpect(jsonPath("$.messages.length()", is(1)));

        testTempFileIsPresent();
        testFinalFileNotPresent();

        itemGetTest()
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.file", is(true)));
    }

}
