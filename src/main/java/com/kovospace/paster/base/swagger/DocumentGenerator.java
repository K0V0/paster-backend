package com.kovospace.paster.base.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.oas.mappers.ServiceModelToOpenApiMapper;
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

@Component
public class DocumentGenerator implements CommandLineRunner {

    @Value("${app.swagger.generated.files.path}")
    private String storagePath;

    private DocumentationCache documentationCache;
    private ServiceModelToOpenApiMapper mapper;
    private ObjectMapper objectMapper;

    @Autowired
    public DocumentGenerator(
            final DocumentationCache documentationCache,
            final ServiceModelToOpenApiMapper mapper,
            final ObjectMapper objectMapper
    ) {
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

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
                ? String.format("%s.%s", documentation.getKey(), extension)
                : String.format("%s/%s.%s", storagePath, documentation.getKey(), extension);
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
                final OpenAPI swagger = mapper.mapDocumentation(
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