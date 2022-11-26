package com.kovospace.paster.item.v2.integration.v1compatibility;

import com.kovospace.paster.item.models.Item;
import org.junit.jupiter.api.Order;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.Assert.assertNotNull;

public class FileItemControllerDeleteV1CompatibilityTest extends FileItemControllerV1CompatibilityTest {

    @org.junit.jupiter.api.Test
    @Order(1)
    @DirtiesContext
    public void optimisticTest() throws Exception {
        Item item = fileInitiateTest();
        item = fileUploadTest(item);

        assertNotNull(item);
        testFinalFileIsPresent();
        testTempFileNotPresent();

        itemGetV1Test(200);

        itemDeleteTest(1);

        testTempFileNotPresent();
        testFinalFileNotPresent();
    }

}
