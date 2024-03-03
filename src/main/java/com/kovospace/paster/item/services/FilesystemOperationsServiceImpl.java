package com.kovospace.paster.item.services;

import com.kovospace.paster.item.exceptions.v2.FileCreationException;
import com.kovospace.paster.item.exceptions.v2.FileException;
import com.kovospace.paster.item.exceptions.v2.FileNotCompleteException;
import com.kovospace.paster.item.mappings.dtoConversions.ItemToFileNameConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.kovospace.paster.item.services.FilesystemOperationsServiceUtils.getFinalFileLocation;
import static com.kovospace.paster.item.services.FilesystemOperationsServiceUtils.getFinalFilePath;
import static com.kovospace.paster.item.services.FilesystemOperationsServiceUtils.getTempFileLocation;
import static com.kovospace.paster.item.services.FilesystemOperationsServiceUtils.getTempFilePath;

@Service
@PropertySource("classpath:application.properties")
public class FilesystemOperationsServiceImpl implements FilesystemOperationsService {

    private final ItemToFileNameConversion itemToFileNameConversion;
    private final String FILE_UPLOADS_DIR;
    private final String FILE_TEMP_DIR;

    @Autowired
    public FilesystemOperationsServiceImpl(
            ItemToFileNameConversion itemToFileNameConversion,
            @Value("${item.file-uploads-dir}") String uploadsDir,
            @Value("${item.file-temp-dir}") String tempDir
    ) {
        this.itemToFileNameConversion = itemToFileNameConversion;
        this.FILE_UPLOADS_DIR = uploadsDir;
        this.FILE_TEMP_DIR = tempDir;
    }

    @Override
    public com.kovospace.paster.item.models.File createEmptyFile(com.kovospace.paster.item.models.File fileEntity) throws FileCreationException {
        String fileName = itemToFileNameConversion.apply(fileEntity.getItem());
        File file = new File(String.format("%s/%s", FILE_TEMP_DIR, fileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new FileCreationException(e.getMessage());
        }
        fileEntity.setFileName(fileName);
        return fileEntity;
    }

    @Override
    public com.kovospace.paster.item.models.File addChunkToFile(com.kovospace.paster.item.models.File fileEntity, byte[] chunk, long chunkNumber) {
        File file = new File(getTempFileLocation(fileEntity, FILE_TEMP_DIR));
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(chunk);
        } catch (FileNotFoundException e) {
            //TODO custom exception fileoutputstreAM
        } catch (IOException e) {
            //TODO custom exception chunk
            throw new RuntimeException(e);
        }
        fileEntity.setChunkNumber(chunkNumber);
        return fileEntity;
    }

    @Override
    public byte[] getFileData(com.kovospace.paster.item.models.File file) throws FileException {
        if (file == null) throw new com.kovospace.paster.item.exceptions.v2.FileNotFoundException();
        try {
            return Files.readAllBytes(getFinalFilePath(file, FILE_UPLOADS_DIR));
        } catch (IOException e) {
            if (Files.exists(getTempFilePath(file, FILE_TEMP_DIR))) {
                throw new FileNotCompleteException();
            }
            throw new com.kovospace.paster.item.exceptions.v2.FileNotFoundException();
        }
    }

    @Override
    public com.kovospace.paster.item.models.File moveFromTempToFinal(com.kovospace.paster.item.models.File fileEntity) {
        String source = getTempFileLocation(fileEntity, FILE_TEMP_DIR);
        String destination = getFinalFileLocation(fileEntity, FILE_UPLOADS_DIR);
        //TODO kolizia ak subor existuje
        try {
            Path move = Files.move(Paths.get(source), Paths.get(destination));
            if (move == null) {
                //TODO custom exception fileEntity not moved
            }
        } catch (IOException e) {
            //TODO custom exception
            throw new RuntimeException(e);
        }
        fileEntity.setFilePath(destination);
        return fileEntity;
    }

    @Override
    public void delete(com.kovospace.paster.item.models.File fileEntity) {
        if (fileEntity == null) return;
        File tempFile = new File(getTempFileLocation(fileEntity, FILE_TEMP_DIR));
        if (!tempFile.delete()) {
            //TODO throw custom exception
        }
        File finalFile = new File(getFinalFileLocation(fileEntity, FILE_UPLOADS_DIR));
        if (!finalFile.delete()) {
            //TODO throw custom exception
        }
    }

    @Override
    public void deleteAll() {
       Stream.of(FILE_TEMP_DIR, FILE_UPLOADS_DIR)
               .map(File::new)
               .map(File::listFiles)
               .filter(Objects::nonNull)
               .flatMap(file -> Arrays.stream(file).sequential())
               .forEach(File::delete);
    }

}
