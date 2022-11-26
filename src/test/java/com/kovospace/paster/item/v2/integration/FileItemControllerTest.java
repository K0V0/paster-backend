package com.kovospace.paster.item.v2.integration;

import com.kovospace.paster.item.dtos.v2.FileItemInitiateRequestDTO;
import com.kovospace.paster.item.dtos.v2.FileItemUploadChunkRequestDTO;
import com.kovospace.paster.item.models.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class FileItemControllerTest extends ItemControllerTest {

    protected static final String FILE_TEMP_DIR = "./testUploads/temp";
    protected static final String FILE_UPLOAD_DIR = "./testUploads/files";

    @Override
    protected String getEndpoint() {
        return "/board/file";
    }

    protected void testTempFileIsPresent() {
       testFile(testFilePresentAsserts, true);
    }

    protected void testFinalFileIsPresent() {
        testFile(testFilePresentAsserts, false);
    }

    protected void testTempFileNotPresent() {
        testFile(testFileNotPresentAsserts, true);
    }

    protected void testFinalFileNotPresent() {
        testFile(testFileNotPresentAsserts, false);
    }

    protected void testTempFileContent(String content) throws IOException {
        testFile(testFileContentAsserts, true, content);
    }

    protected void testFinalFileContent(String content) throws IOException {
        testFile(testFileContentAsserts, false, content);
    }

    protected Item fileInitiateTest() throws Exception {
        FileItemInitiateRequestDTO itemInit = new FileItemInitiateRequestDTO();
        itemInit.setOriginalFileName("filename.txt");
        itemInit.setMimeType("text/plain");
        itemPostTest(itemInit, HttpStatus.ACCEPTED.value());
        List<Item> items = itemRepository.findAll();
        assertNotNull(items);
        assertEquals(1, items.size());
        Item item = items.get(0);
        assertNotNull(item);
        testTempFileIsPresent();
        return item;
    }

    protected Item fileUploadTest(Item initiatedItem) throws Exception {
        FileItemUploadChunkRequestDTO itemUpload = new FileItemUploadChunkRequestDTO();
        itemUpload.setOriginalFileName("filename.txt");
        itemUpload.setMimeType("text/plain");
        itemUpload.setFileContent(prepareMockMultipartFile().getBytes());
        itemUpload.setChunkNumber(0L);
        itemUpload.setItemId(initiatedItem.getId());
        itemUpload.setFileId(initiatedItem.getFile().getId());
        itemPutTest(itemUpload, HttpStatus.CREATED.value());
        Item item = itemRepository.findAll().get(0);
        testTempFileNotPresent();
        testFinalFileIsPresent();
        return item;
    }


    protected MultipartFile prepareMockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "filename.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "AAAABBBB".getBytes());
    }

    private Optional<String> getPath(boolean isTemp) {
        return itemRepository.findAll()
                .stream().findFirst()
                .map(Item::getFile)
                .map(file -> isTemp
                        ? String.format("%s/%s", FILE_TEMP_DIR, file.getFileName())
                        : String.format("%s/%s", FILE_UPLOAD_DIR, file.getFullFileName()));
    }

    private void testFile(Consumer<String> asserts, boolean isTemp) {
        Optional<String> path = getPath(isTemp);
        if (path.isPresent()) asserts.accept(path.get());
        else assertFalse(false);
    }

    private void testFile(BiConsumer<String, String> asserts, boolean isTemp, String content) {
        Optional<String> path = getPath(isTemp);
        if (path.isPresent()) asserts.accept(path.get(), content);
        else assertFalse(false);
    }

    private static Consumer<String> testFileNotPresentAsserts =
            path -> assertFalse(Files.isRegularFile(Paths.get(path)));

    private static Consumer<String> testFilePresentAsserts =
            path -> assertTrue(Files.isRegularFile(Paths.get(path)));

    private static BiConsumer<String, String> testFileContentAsserts =
            (path, expectedContent) -> {
                assertNotNull(path);
                Path actual = Paths.get(path);
                assertNotNull(actual);
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(actual);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //TODO checknut co pri multiline
                String fileContent = lines.stream().findFirst().orElse(null);
                assertEquals(expectedContent, fileContent);
            };
}
