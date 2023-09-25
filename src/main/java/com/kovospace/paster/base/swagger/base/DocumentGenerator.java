package com.kovospace.paster.base.swagger.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.swagger.documentationMapperAdapter.DocumentationMapperAdapter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.StringUtils;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.kovospace.paster.base.utils.Utils.exceptionHandler;

public abstract class DocumentGenerator<RESULT_OBJ> implements CommandLineRunner {

    protected enum DocumentVersion {
        V2, V3
    }

    @Value("${app.swagger.generated.files.path}")
    private String storagePath;

    @Autowired
    private DocumentationCache documentationCache;
    @Autowired
    private ObjectMapper objectMapper;

    protected abstract DocumentationMapperAdapter<RESULT_OBJ> getMapper();
    protected abstract DocumentVersion getDocumentVersion();

    @Override
    public void run(String... args) throws IOException {
    Optional.ofNullable(documentationCache.all())
                .map(docMap -> docMap.entrySet())
                .map(docEntries -> docEntries.stream())
                .orElseGet(Stream::empty)
                .forEach(docEntry -> generateAllexpectedDocumentTypes(docEntry));
    }

    private void generateAllexpectedDocumentTypes(final Map.Entry<String, Documentation> documentation) {
        Arrays.stream(ExpectedDocumentType.values())
                .forEach(documentType -> {
                    final String outputFilePath = getOutputFilePath(documentation, documentType.extension());
                    final String fileContent = converter.apply(documentation, documentType);
                    writeFile(fileContent, outputFilePath);
                });
    }

    private String getOutputFilePath(final Map.Entry<String, Documentation> documentation, final String extension) {
        return StringUtils.isEmpty(storagePath)
                ? String.format(
                        "%s-%s.%s", documentation.getKey(), getDocumentVersion().name(), extension)
                : String.format(
                        "%s/%s-%s.%s", storagePath, documentation.getKey(), getDocumentVersion().name(), extension);
    }

    private void writeFile(final String document, final String filePath) {
        final File outputFile = new File(filePath);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                //TODO
                throw new RuntimeException(e);
            }
        }
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(document);
        } catch (Exception e) {
            //TODO
        }
    }

    private BiFunction<Map.Entry<String, Documentation>, ExpectedDocumentType, String> converter =
            (documentationEntry, expectedDocumentType) -> {
                final RESULT_OBJ swagger = getMapper().mapDocumentation(
                        documentationCache.documentationByGroup(documentationEntry.getKey()));
                switch (expectedDocumentType) {
                    case JSON:
                        return exceptionHandler(() -> Json.pretty(swagger));
                    case YAML:
                    default:
                        return exceptionHandler(() -> Yaml.pretty().writeValueAsString(swagger));
                }
            };
}