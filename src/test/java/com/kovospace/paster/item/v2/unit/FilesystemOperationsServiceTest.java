package com.kovospace.paster.item.v2.unit;

import com.kovospace.paster.item.exceptions.v2.FileCreationException;
import com.kovospace.paster.item.mappings.dtoConversions.ItemToFileNameConversion;
import com.kovospace.paster.item.models.Item;
import com.kovospace.paster.item.services.FilesystemOperationsService;
import com.kovospace.paster.item.services.FilesystemOperationsServiceImpl;
import com.kovospace.paster.user.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesystemOperationsServiceTest extends ItemUnitTest {

    private static FilesystemOperationsService filesystemOperationsService;

    @BeforeAll
    public static void init() {
        //Whitebox.setInternalState(FilesystemOperationsServiceImpl.class, "FILE_TEMP_DIR", FILE_TEMP_DIR);
        filesystemOperationsService = new FilesystemOperationsServiceImpl(
                new ItemToFileNameConversion(),
                FILE_UPLOAD_DIR,
                FILE_TEMP_DIR
        );
    }

    @Test
    public void testCreateEmptyFile() throws FileCreationException {
        //TODO odoperovat krizovu zavislost v buducnosti
        com.kovospace.paster.item.models.File file = new com.kovospace.paster.item.models.File();
        file.setId(1);
        User user = new User();
        user.setId(1);
        Item item = new Item();
        item.setId(1);
        item.setFile(file);
        file.setItem(item);
        item.setUser(user);

        filesystemOperationsService.createEmptyFile(file);
        testFilePresence("1-1-1");
    }

    private void testFilePresence(String filename) {
        String path = String.format("%s/%s", FILE_TEMP_DIR, filename);
        File file = new File(path);
        assertNotNull(file);
        assertEquals(path, file.getPath());
        assertTrue(file.exists());
    }
}
