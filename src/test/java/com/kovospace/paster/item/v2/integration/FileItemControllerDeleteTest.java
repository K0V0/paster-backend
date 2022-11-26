package com.kovospace.paster.item.v2.integration;

import com.kovospace.paster.item.models.Item;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.Assert.assertNotNull;

public class FileItemControllerDeleteTest extends FileItemControllerTest {

    @Test
    @Order(1)
    @DirtiesContext
    public void optimisticTest() throws Exception {
        Item item = fileInitiateTest();

        item = fileUploadTest(item);

        assertNotNull(item);
        testFinalFileIsPresent();
        testTempFileNotPresent();

        itemGetTest();

        itemDeleteTest(1);

        testFinalFileNotPresent();
        testTempFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void notFinalizedTest() throws Exception {
        Item item = fileInitiateTest();

        assertNotNull(item);
        testFinalFileNotPresent();
        testTempFileIsPresent();

        itemGetTest();

        itemDeleteTest(1);

        testFinalFileNotPresent();
        testTempFileNotPresent();

        itemGetTest(HttpStatus.NOT_FOUND.value());
    }
}
